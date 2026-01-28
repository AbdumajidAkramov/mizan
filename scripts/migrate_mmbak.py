#!/usr/bin/env python3
"""
Mizan Migration Script
Migrates legacy Money Manager SQLite (backfile.mmbak) to Mizan schema.
Usage: python scripts/migrate_mmbak.py
Output: mizan_populated.db in project root
"""

import sqlite3
import uuid
from datetime import datetime
from pathlib import Path

# Paths
PROJECT_ROOT = Path(__file__).parent.parent
LEGACY_DB = PROJECT_ROOT / "app/src/main/assets/backfile.mmbak"
OUTPUT_DB = PROJECT_ROOT / "mizan_populated.db"

def migrate():
    # Connect to legacy and new databases
    legacy_conn = sqlite3.connect(str(LEGACY_DB))
    new_conn = sqlite3.connect(str(OUTPUT_DB))

    # Enable foreign keys in new DB
    new_conn.execute("PRAGMA foreign_keys = ON")

    # Create Mizan schema (read from generated file)
    schema_path = PROJECT_ROOT / "readme/db_queries.txt"
    with open(schema_path, "r", encoding="utf-8") as f:
        schema_sql = f.read()
    # Remove comments and execute as a script
    cleaned = "\n".join(line for line in schema_sql.splitlines() if not line.strip().startswith("--"))
    try:
        new_conn.executescript(cleaned)
        print("Schema created.")
    except sqlite3.Error as e:
        print(f"Schema exec error: {e}")
    # List tables to verify
    tables = new_conn.execute("SELECT name FROM sqlite_master WHERE type='table'").fetchall()
    print("Tables created:", [t[0] for t in tables])
    new_conn.commit()

    # ---------- 1. MIGRATE CURRENCIES ----------
    print("Migrating currencies...")
    currencies = legacy_conn.execute("""
        SELECT ZISO, ZSYMBOL, ZRATE, ZISMAINCURRENCY
        FROM ZCURRENCY
        WHERE ZISO IS NOT NULL AND ZISO != ''
    """).fetchall()
    for iso, symbol, rate, is_main in currencies:
        # Use iso as code; if missing, generate placeholder
        code = iso if iso else f"CURRENCY_{uuid.uuid4().hex[:6].upper()}"
        name = iso if iso else "Unknown"
        new_conn.execute("""
            INSERT OR IGNORE INTO currencies (code, name, symbol, rateToBase, isBaseCurrency)
            VALUES (?, ?, ?, ?, ?)
        """, (code, name, symbol or code, rate or 1.0, bool(is_main)))
    new_conn.commit()

    # ---------- 2. MIGRATE ACCOUNT GROUPS ----------
    print("Migrating account groups...")
    groups = legacy_conn.execute("""
        SELECT ZASSETGROUPNAME, ZUID
        FROM ZASSETGROUP
        WHERE ZASSETGROUPNAME IS NOT NULL
    """).fetchall()
    for name, uid in groups:
        # Use existing uid or generate new
        group_id = uid if uid else str(uuid.uuid4())
        new_conn.execute("""
            INSERT OR IGNORE INTO account_groups (id, name, iconName, orderIndex)
            VALUES (?, ?, ?, ?)
        """, (hash(group_id) % (2**31), name, None, 0))
    new_conn.commit()

    # ---------- 3. MIGRATE CATEGORIES ----------
    print("Migrating categories...")
    categories = legacy_conn.execute("""
        SELECT ZNAME, ZDOTYPE, ZUID
        FROM ZCATEGORY
        WHERE ZNAME IS NOT NULL
    """).fetchall()
    for name, do_type, uid in categories:
        # ZDOTYPE: 1 = Expense, 0 = Income (based on sample)
        type_str = "EXPENSE" if do_type == 1 else "INCOME"
        new_conn.execute("""
            INSERT OR IGNORE INTO categories (id, name, type, parentId, iconName, color, budgetLimit, isArchived, orderIndex)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (hash(uid) % (2**31) if uid else None, name, type_str, None, "ic_category", "#FF5722", None, False, 0))
    new_conn.commit()

    # ---------- 4. MIGRATE ACCOUNTS ----------
    print("Migrating accounts...")
    accounts = legacy_conn.execute("""
        SELECT ZNICNAME, ZCURRENCYUID, ZLEFTMONEY, ZGROUPUID
        FROM ZASSET
        WHERE ZNICNAME IS NOT NULL
    """).fetchall()
    for name, currency_uid, balance, group_uid in accounts:
        # Resolve currency code from UID
        currency_code = "UZS"  # default
        if currency_uid:
            cur = legacy_conn.execute(
                "SELECT ZISO FROM ZCURRENCY WHERE ZUID = ?", (currency_uid,)
            ).fetchone()
            if cur and cur[0]:
                currency_code = cur[0]
        # Resolve group id
        group_id = None
        if group_uid:
            group_id = hash(group_uid) % (2**31)
        new_conn.execute("""
            INSERT OR IGNORE INTO accounts (id, groupId, name, type, balance, currencyCode, iconName, color, isArchived, excludeFromTotal, description)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (None, group_id, name, "ASSET", balance or 0.0, currency_code, None, None, False, False, None))
    new_conn.commit()

    # ---------- 5. MIGRATE TRANSACTIONS ----------
    print("Migrating transactions...")
    transactions = legacy_conn.execute("""
        SELECT ZDATE, ZAMOUNT, ZDO_TYPE, ZASSETUID, ZCATEGORYUID, ZCONTENT, ZCURRENCY
        FROM ZINOUTCOME
        WHERE ZDATE IS NOT NULL
    """).fetchall()
    for ts_ms, amount, do_type, asset_uid, cat_uid, content, currency in transactions:
        # Convert timestamp from milliseconds to seconds (Unix epoch)
        ts_sec = int(ts_ms / 1000) if ts_ms else 0
        # Type: 1 = Expense, 3 = Income (based on sample)
        type_str = "EXPENSE" if do_type == 1 else "INCOME"
        # Resolve account id
        account_id = None
        if asset_uid:
            acc = legacy_conn.execute(
                "SELECT Z_PK FROM ZASSET WHERE ZUID = ?", (asset_uid,)
            ).fetchone()
            if acc:
                # Find matching account in new DB by name or UID mapping
                account_name = legacy_conn.execute(
                    "SELECT ZNICNAME FROM ZASSET WHERE ZUID = ?", (asset_uid,)
                ).fetchone()
                if account_name:
                    acc_match = new_conn.execute(
                        "SELECT id FROM accounts WHERE name = ?", (account_name[0],)
                    ).fetchone()
                    if acc_match:
                        account_id = acc_match[0]
        # Resolve category id
        category_id = None
        if cat_uid:
            cat = legacy_conn.execute(
                "SELECT Z_PK FROM ZCATEGORY WHERE ZUID = ?", (cat_uid,)
            ).fetchone()
            if cat:
                # Find matching category in new DB by name
                cat_name = legacy_conn.execute(
                    "SELECT ZNAME FROM ZCATEGORY WHERE ZUID = ?", (cat_uid,)
                ).fetchone()
                if cat_name:
                    cat_match = new_conn.execute(
                        "SELECT id FROM categories WHERE name = ?", (cat_name[0],)
                    ).fetchone()
                    if cat_match:
                        category_id = cat_match[0]
        print(f"Transaction: {content[:30]}... account_id={account_id} category_id={category_id}")
        # Resolve currency code
        currency_code = "UZS"
        if currency:
            # Try to map legacy currency string to ISO
            if "USD" in currency.upper():
                currency_code = "USD"
            elif "UZS" in currency.upper():
                currency_code = "UZS"
        # Get exchange rate at time (use current rate from currencies table as snapshot)
        exchange_rate = 1.0
        rate_row = new_conn.execute(
            "SELECT rateToBase FROM currencies WHERE code = ?", (currency_code,)
        ).fetchone()
        if rate_row:
            exchange_rate = rate_row[0]
        new_conn.execute("""
            INSERT OR IGNORE INTO transactions (
                id, type, amount, currencyCode, exchangeRate, date, note, description, accountId, categoryId
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (None, type_str, amount or 0.0, currency_code, exchange_rate, ts_sec, content, None, account_id, category_id))
    new_conn.commit()

    # ---------- 6. TEMPLATES (optional, skip for now) ----------
    print("Migration complete. Output:", OUTPUT_DB)
    legacy_conn.close()
    new_conn.close()

if __name__ == "__main__":
    if not LEGACY_DB.exists():
        print(f"Legacy DB not found at {LEGACY_DB}")
        exit(1)
    migrate()
