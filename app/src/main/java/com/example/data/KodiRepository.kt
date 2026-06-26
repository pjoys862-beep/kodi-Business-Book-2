package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class KodiRepository(private val kodiDao: KodiDao) {
    val allCategories: Flow<List<CategoryEntity>> = kodiDao.getAllCategoriesFlow()
    val allIncomeEntries: Flow<List<IncomeEntryEntity>> = kodiDao.getAllIncomeFlow()
    val allExpenseEntries: Flow<List<ExpenseEntryEntity>> = kodiDao.getAllExpensesFlow()
    val allAssets: Flow<List<FixedAssetEntity>> = kodiDao.getAllAssetsFlow()

    suspend fun insertIncome(income: IncomeEntryEntity) {
        kodiDao.insertIncome(income)
    }

    suspend fun deleteIncome(income: IncomeEntryEntity) {
        kodiDao.deleteIncome(income)
    }

    suspend fun insertExpense(expense: ExpenseEntryEntity) {
        kodiDao.insertExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntryEntity) {
        kodiDao.deleteExpense(expense)
    }

    suspend fun insertAsset(asset: FixedAssetEntity) {
        kodiDao.insertAsset(asset)
    }

    suspend fun deleteAsset(asset: FixedAssetEntity) {
        kodiDao.deleteAsset(asset)
    }

    suspend fun insertCategory(category: CategoryEntity) {
        kodiDao.insertCategory(category)
    }

    suspend fun checkAndPrepopulateCategories() {
        // Query once to see if it is empty
        val currentCategories = allCategories.first()
        if (currentCategories.isEmpty()) {
            val defaults = listOf(
                CategoryEntity("Product A", "INCOME"),
                CategoryEntity("Product B", "INCOME"),
                CategoryEntity("Material A", "MATERIAL"),
                CategoryEntity("Material B", "MATERIAL"),
                CategoryEntity("Material C", "MATERIAL"),
                CategoryEntity("Employee Salary", "OPERATING"),
                CategoryEntity("Electricity", "OPERATING"),
                CategoryEntity("Water", "OPERATING"),
                CategoryEntity("Gas", "OPERATING"),
                CategoryEntity("Rent", "OPERATING"),
                CategoryEntity("Freight charges", "OPERATING"),
                CategoryEntity("Transport", "OPERATING"),
                CategoryEntity("Marketing & Advertising", "OPERATING"),
                CategoryEntity("Packaging", "OPERATING"),
                CategoryEntity("Maintenance", "OPERATING"),
                CategoryEntity("Telephone", "OPERATING"),
                CategoryEntity("Stationery", "OPERATING"),
                CategoryEntity("Entertainment", "OPERATING"),
                CategoryEntity("Welfare", "OPERATING"),
                CategoryEntity("Financial expenses", "OPERATING"),
                CategoryEntity("Other", "OPERATING")
            )
            kodiDao.insertCategories(defaults)
        }
    }
}
