package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KodiViewModel(private val repository: KodiRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.checkAndPrepopulateCategories()
        }
    }

    val categories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val incomeEntries: StateFlow<List<IncomeEntryEntity>> = repository.allIncomeEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val expenseEntries: StateFlow<List<ExpenseEntryEntity>> = repository.allExpenseEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val fixedAssets: StateFlow<List<FixedAssetEntity>> = repository.allAssets
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addIncome(category: String, amount: Double) {
        viewModelScope.launch {
            repository.insertIncome(
                IncomeEntryEntity(
                    category = category,
                    amount = amount,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteIncome(income: IncomeEntryEntity) {
        viewModelScope.launch {
            repository.deleteIncome(income)
        }
    }

    fun addExpense(mainCategory: String, subCategory: String, amount: Double) {
        viewModelScope.launch {
            repository.insertExpense(
                ExpenseEntryEntity(
                    mainCategory = mainCategory,
                    subCategory = subCategory,
                    amount = amount,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteExpense(expense: ExpenseEntryEntity) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    fun addAsset(name: String, cost: Double, rate: Double, isLand: Boolean) {
        viewModelScope.launch {
            repository.insertAsset(
                FixedAssetEntity(
                    name = name,
                    cost = cost,
                    rate = if (isLand) 0.0 else rate,
                    isLand = isLand
                )
            )
        }
    }

    fun deleteAsset(asset: FixedAssetEntity) {
        viewModelScope.launch {
            repository.deleteAsset(asset)
        }
    }

    fun addCategory(name: String, type: String) {
        viewModelScope.launch {
            repository.insertCategory(
                CategoryEntity(
                    name = name.trim(),
                    type = type
                )
            )
        }
    }
}

class KodiViewModelFactory(private val repository: KodiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KodiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KodiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
