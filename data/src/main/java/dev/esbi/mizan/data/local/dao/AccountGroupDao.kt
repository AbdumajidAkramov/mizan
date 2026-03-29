package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.esbi.mizan.data.local.entity.account.AccountGroupEntity
import dev.esbi.mizan.data.local.entity.account.AccountGroupWithAccounts
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountGroupDao {

    // --- Create / Update ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: AccountGroupEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<AccountGroupEntity>)

    @Update
    suspend fun updateGroup(group: AccountGroupEntity)

    // --- Delete ---
    @Delete
    suspend fun deleteGroup(group: AccountGroupEntity)

    @Query("DELETE FROM account_groups WHERE id = :groupId")
    suspend fun deleteGroupById(groupId: Long)

    // --- Query ---
    @Query("SELECT * FROM account_groups ORDER BY orderIndex ASC")
    fun getAllGroups(): Flow<List<AccountGroupEntity>>

    @Query("SELECT * FROM account_groups WHERE id = :groupId")
    suspend fun getGroupById(groupId: Long): AccountGroupEntity?

    // --- Relation Query (Guruhlarni hisoblari bilan birga olish) ---
    @Transaction
    @Query("SELECT * FROM account_groups ORDER BY orderIndex ASC")
    fun getGroupsWithAccounts(): Flow<List<AccountGroupWithAccounts>>

    // Guruhlar sonini tekshirish (Seeding uchun kerak bo'lishi mumkin)
    @Query("SELECT COUNT(*) FROM account_groups")
    suspend fun getGroupCount(): Int
}
