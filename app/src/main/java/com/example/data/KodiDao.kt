package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface KodiDao {
    // Categories
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategoriesFlow(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    // Income Entries
    @Query("SELECT * FROM income_entries ORDER BY date DESC")
    fun getAllIncomeFlow(): Flow<List<IncomeEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: IncomeEntryEntity)

    @Delete
    suspend fun deleteIncome(income: IncomeEntryEntity)

    // Expense Entries
    @Query("SELECT * FROM expense_entries ORDER BY date DESC")
    fun getAllExpensesFlow(): Flow<List<ExpenseEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntryEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntryEntity)

    // Fixed Assets
    @Query("SELECT * FROM fixed_assets ORDER BY id DESC")
    fun getAllAssetsFlow(): Flow<List<FixedAssetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: FixedAssetEntity)

    @Delete
    suspend fun deleteAsset(asset: FixedAssetEntity)
}
