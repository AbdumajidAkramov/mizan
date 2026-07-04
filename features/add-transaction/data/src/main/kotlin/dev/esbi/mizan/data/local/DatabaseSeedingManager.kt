package dev.esbi.mizan.data.local

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeedingManager @Inject constructor(
    private val database: MizanDatabase
) {
    
    private val tag = "DatabaseSeedingManager"
    private var isSeedingChecked = false
    
    fun ensureDatabaseSeeded(context: Context) {
        if (!isSeedingChecked) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val categoryCount = database.categoryDao().getCategoryCount()
                    val accountCount = database.accountDao().getAccountCount()
                    
                    Log.d(tag, "Category count: $categoryCount, Account count: $accountCount")
                    
                    if (categoryCount == 0 || accountCount == 0) {
                        Log.d(tag, "Database is empty, starting seeding...")
                        val callback = MizanDatabaseCallback()
                        callback.populateDatabase(context, database)
                        
                        // Verify seeding worked
                        val newCategoryCount = database.categoryDao().getCategoryCount()
                        val newAccountCount = database.accountDao().getAccountCount()
                        Log.d(tag, "After seeding - Category count: $newCategoryCount, Account count: $newAccountCount")
                    } else {
                        Log.d(tag, "Database already seeded, skipping...")
                    }
                } catch (e: Exception) {
                    Log.e(tag, "Error during database seeding check", e)
                } finally {
                    isSeedingChecked = true
                }
            }
        }
    }
}
