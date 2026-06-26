package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val name: String,
    val type: String // "INCOME", "MATERIAL", "OPERATING"
)

@Entity(tableName = "income_entries")
data class IncomeEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val amount: Double,
    val date: Long // Timestamp in milliseconds
)

@Entity(tableName = "expense_entries")
data class ExpenseEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mainCategory: String, // "Material" or "Operating"
    val subCategory: String,
    val amount: Double,
    val date: Long // Timestamp in milliseconds
)

@Entity(tableName = "fixed_assets")
data class FixedAssetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val cost: Double,
    val rate: Double, // annual depreciation rate (%)
    val isLand: Boolean
)
