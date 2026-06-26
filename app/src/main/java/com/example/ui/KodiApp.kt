package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CategoryEntity
import com.example.data.ExpenseEntryEntity
import com.example.data.FixedAssetEntity
import com.example.data.IncomeEntryEntity
import java.text.SimpleDateFormat
import java.util.*

fun formatCurrency(amount: Double): String {
    return String.format(Locale.US, "Rs. %,.2f", amount)
}

fun getCurrentMonthYearString(): String {
    val sdf = SimpleDateFormat("MMMM yyyy", Locale.US)
    return sdf.format(Date())
}

fun formatDateOnly(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
    return sdf.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KodiApp(viewModel: KodiViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val incomeEntries by viewModel.incomeEntries.collectAsStateWithLifecycle()
    val expenseEntries by viewModel.expenseEntries.collectAsStateWithLifecycle()
    val fixedAssets by viewModel.fixedAssets.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kodi Business Book",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Kodi Solutions Navigation", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Kodi Solutions Premium Account", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Account",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    modifier = Modifier.testTag("nav_dashboard")
                )
                NavigationBarItem(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Income") },
                    label = { Text("Income") },
                    modifier = Modifier.testTag("nav_income")
                )
                NavigationBarItem(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    icon = { Icon(Icons.Default.TrendingDown, contentDescription = "Expenses") },
                    label = { Text("Expenses") },
                    modifier = Modifier.testTag("nav_expenses")
                )
                NavigationBarItem(
                    selected = selectedTabIndex == 3,
                    onClick = { selectedTabIndex = 3 },
                    icon = { Icon(Icons.Default.AccountBalance, contentDescription = "Assets") },
                    label = { Text("Assets") },
                    modifier = Modifier.testTag("nav_assets")
                )
                NavigationBarItem(
                    selected = selectedTabIndex == 4,
                    onClick = { selectedTabIndex = 4 },
                    icon = { Icon(Icons.Default.Analytics, contentDescription = "Report") },
                    label = { Text("P&L Report") },
                    modifier = Modifier.testTag("nav_report")
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedTabIndex) {
                0 -> DashboardScreen(incomeEntries, expenseEntries, fixedAssets)
                1 -> IncomeScreen(
                    categories = categories.filter { it.type == "INCOME" },
                    recentIncomes = incomeEntries,
                    onSaveIncome = { category, amount ->
                        viewModel.addIncome(category, amount)
                        Toast.makeText(context, "Income Added Successfully!", Toast.LENGTH_SHORT).show()
                    },
                    onDeleteIncome = { viewModel.deleteIncome(it) },
                    onAddCategory = { name -> viewModel.addCategory(name, "INCOME") }
                )
                2 -> ExpenseScreen(
                    categories = categories,
                    recentExpenses = expenseEntries,
                    onSaveExpense = { mainType, subType, amount ->
                        viewModel.addExpense(mainType, subType, amount)
                        Toast.makeText(context, "Expense Saved!", Toast.LENGTH_SHORT).show()
                    },
                    onDeleteExpense = { viewModel.deleteExpense(it) },
                    onAddCategory = { name, mainType ->
                        viewModel.addCategory(name, mainType)
                    }
                )
                3 -> AssetScreen(
                    assets = fixedAssets,
                    onRegisterAsset = { name, cost, rate, isLand ->
                        viewModel.addAsset(name, cost, rate, isLand)
                        Toast.makeText(context, "Asset Registered!", Toast.LENGTH_SHORT).show()
                    },
                    onDeleteAsset = { viewModel.deleteAsset(it) }
                )
                4 -> ProfitLossScreen(incomeEntries, expenseEntries, fixedAssets)
            }
        }
    }
}

// --- 1. DASHBOARD SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    incomeList: List<IncomeEntryEntity>,
    expenseList: List<ExpenseEntryEntity>,
    assetList: List<FixedAssetEntity>
) {
    val totalIncome = incomeList.sumOf { it.amount }
    val totalExpense = expenseList.sumOf { it.amount }

    var totalDepreciation = 0.0
    for (asset in assetList) {
        if (!asset.isLand) {
            totalDepreciation += (asset.cost * (asset.rate / 100.0)) / 12.0
        }
    }
    val netProfit = totalIncome - totalExpense - totalDepreciation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Branding Header
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "KODI SOLUTIONS",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 2.sp
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Smart Business Accounting for SMEs",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.outline,
                fontStyle = FontStyle.Italic
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Monthly Summary",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = getCurrentMonthYearString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Income Card
        DashboardSummaryCard(
            title = "Total Income",
            amount = totalIncome,
            icon = Icons.Default.TrendingUp,
            color = Color(0xFF4CAF50),
            testTag = "summary_card_income"
        )

        // Expense Card
        DashboardSummaryCard(
            title = "Total Expenses",
            amount = totalExpense,
            icon = Icons.Default.TrendingDown,
            color = Color(0xFFFF9800),
            testTag = "summary_card_expenses"
        )

        // Net Profit Card
        DashboardSummaryCard(
            title = "Net Profit / Loss (Est.)",
            amount = netProfit,
            icon = Icons.Default.Payments,
            color = if (netProfit >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            testTag = "summary_card_net_profit",
            isFeatured = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Support Hotline banner
        val dialerContext = LocalContext.current
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE2E8F0).copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneEnabled,
                        contentDescription = "Support Phone",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "SUPPORT HOTLINE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp
                        ),
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "071 515 0509",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF334155)
                    )
                }
                Button(
                    onClick = {
                        try {
                            val intent = android.content.Intent(android.content.Intent.ACTION_DIAL).apply {
                                data = android.net.Uri.parse("tel:0715150509")
                            }
                            dialerContext.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(dialerContext, "Dialer could not be opened", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF334155)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = "CALL",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black)
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardSummaryCard(
    title: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    testTag: String,
    isFeatured: Boolean = false
) {
    if (isFeatured) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = color),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .testTag(testTag)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(color = Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = formatCurrency(amount),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    } else {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .testTag(testTag)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(color = color.copy(alpha = 0.08f), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = formatCurrency(amount),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E293B)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View detail",
                    tint = Color(0xFFCBD5E1),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// --- 2. INCOME SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(
    categories: List<CategoryEntity>,
    recentIncomes: List<IncomeEntryEntity>,
    onSaveIncome: (String, Double) -> Unit,
    onDeleteIncome: (IncomeEntryEntity) -> Unit,
    onAddCategory: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var showCustomCategoryDialog by remember { mutableStateOf(false) }
    var customCategoryName by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Daily Income Entry",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Dropdown Selection for Item
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Income Item / Product") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown Arrow",
                        modifier = Modifier.clickable { dropdownExpanded = true }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dropdownExpanded = true }
                    .testTag("income_category_dropdown")
            )
            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat.name) },
                        onClick = {
                            selectedCategory = cat.name
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { showCustomCategoryDialog = true },
            modifier = Modifier
                .align(Alignment.Start)
                .testTag("create_custom_income_item"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create Custom Income Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Income Amount (Rs.)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("income_amount_input")
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val amount = amountText.toDoubleOrNull()
                if (selectedCategory.isNotEmpty() && amount != null && amount > 0) {
                    onSaveIncome(selectedCategory, amount)
                    amountText = ""
                    selectedCategory = ""
                }
            },
            enabled = selectedCategory.isNotEmpty() && amountText.toDoubleOrNull() != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("save_income_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Save Income Entry")
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recent Income Entries",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (recentIncomes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No income entries yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(recentIncomes) { entry ->
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = entry.category,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = formatDateOnly(entry.date),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                            }
                            Text(
                                text = formatCurrency(entry.amount),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF16A34A)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(onClick = { onDeleteIncome(entry) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete entry",
                                    tint = Color(0xFFFDA4AF)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Custom Category dialog
    if (showCustomCategoryDialog) {
        AlertDialog(
            onDismissRequest = {
                showCustomCategoryDialog = false
                customCategoryName = ""
            },
            title = { Text("Add Custom Product/Item") },
            text = {
                OutlinedTextField(
                    value = customCategoryName,
                    onValueChange = { customCategoryName = it },
                    label = { Text("Enter Name (e.g. Item X Sales)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("custom_income_category_input")
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (customCategoryName.trim().isNotEmpty()) {
                            onAddCategory(customCategoryName.trim())
                            selectedCategory = customCategoryName.trim()
                            customCategoryName = ""
                            showCustomCategoryDialog = false
                        }
                    },
                    enabled = customCategoryName.trim().isNotEmpty()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showCustomCategoryDialog = false
                    customCategoryName = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// --- 3. EXPENSE SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    categories: List<CategoryEntity>,
    recentExpenses: List<ExpenseEntryEntity>,
    onSaveExpense: (String, String, Double) -> Unit,
    onDeleteExpense: (ExpenseEntryEntity) -> Unit,
    onAddCategory: (String, String) -> Unit
) {
    var mainCategory by remember { mutableStateOf("Material") } // "Material" or "Operating"
    var selectedSubCategory by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }

    val filteredCategories = categories.filter {
        if (mainCategory == "Material") it.type == "MATERIAL" else it.type == "OPERATING"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Daily Expense Entry",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Tab selection for expense class
        TabRow(
            selectedTabIndex = if (mainCategory == "Material") 0 else 1,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .testTag("expense_type_tabs")
        ) {
            Tab(
                selected = mainCategory == "Material",
                onClick = {
                    mainCategory = "Material"
                    selectedSubCategory = ""
                },
                text = { Text("Material Cost", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = mainCategory == "Operating",
                onClick = {
                    mainCategory = "Operating"
                    selectedSubCategory = ""
                },
                text = { Text("Operating Exp", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subcategory dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedSubCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select $mainCategory Type") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown Arrow",
                        modifier = Modifier.clickable { dropdownExpanded = true }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dropdownExpanded = true }
                    .testTag("expense_subcategory_dropdown")
            )
            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                filteredCategories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat.name) },
                        onClick = {
                            selectedSubCategory = cat.name
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Custom Category Button
        Button(
            onClick = { showAddCategoryDialog = true },
            modifier = Modifier
                .align(Alignment.Start)
                .testTag("create_custom_expense_category"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (mainCategory == "Material") "Add Custom Material" else "Add Custom Op Category"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Expense Cost (Rs.)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("expense_amount_input")
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val amount = amountText.toDoubleOrNull()
                if (selectedSubCategory.isNotEmpty() && amount != null && amount > 0) {
                    onSaveExpense(mainCategory, selectedSubCategory, amount)
                    amountText = ""
                    selectedSubCategory = ""
                }
            },
            enabled = selectedSubCategory.isNotEmpty() && amountText.toDoubleOrNull() != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("save_expense_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary) // Red color for expense!
        ) {
            Text("Save Expense Entry")
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recent Expense Entries",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (recentExpenses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No expense entries yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(recentExpenses) { entry ->
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = entry.subCategory,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${entry.mainCategory} • ${formatDateOnly(entry.date)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                            }
                            Text(
                                text = formatCurrency(entry.amount),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFEF4444)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(onClick = { onDeleteExpense(entry) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete entry",
                                    tint = Color(0xFFFDA4AF)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddCategoryDialog) {
        val typeLabel = if (mainCategory == "Material") "MATERIAL" else "OPERATING"
        AlertDialog(
            onDismissRequest = {
                showAddCategoryDialog = false
                newCategoryName = ""
            },
            title = {
                Text(
                    if (mainCategory == "Material") "Type Custom Material Name" else "Add Custom Operating Expense"
                )
            },
            text = {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    label = { Text("e.g. Material D, Cleaning, Spares") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("custom_expense_category_input")
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newCategoryName.trim().isNotEmpty()) {
                            onAddCategory(newCategoryName.trim(), typeLabel)
                            selectedSubCategory = newCategoryName.trim()
                            newCategoryName = ""
                            showAddCategoryDialog = false
                        }
                    },
                    enabled = newCategoryName.trim().isNotEmpty()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddCategoryDialog = false
                    newCategoryName = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// --- 4. FIXED ASSETS SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetScreen(
    assets: List<FixedAssetEntity>,
    onRegisterAsset: (String, Double, Double, Boolean) -> Unit,
    onDeleteAsset: (FixedAssetEntity) -> Unit
) {
    var nameText by remember { mutableStateOf("") }
    var costText by remember { mutableStateOf("") }
    var rateText by remember { mutableStateOf("") }
    var isLand by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Fixed Assets (Depreciation)",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = nameText,
            onValueChange = { nameText = it },
            label = { Text("Asset Name (e.g. Machinery, Land, Vehicle)") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("asset_name_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = costText,
            onValueChange = { costText = it },
            label = { Text("Purchase Cost (Rs.)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("asset_cost_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Is this Asset a Land?",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isLand,
                onCheckedChange = { isLand = it },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.testTag("asset_island_switch")
            )
        }

        if (!isLand) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = rateText,
                onValueChange = { rateText = it },
                label = { Text("Annual Depreciation Rate (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("asset_rate_input")
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val cost = costText.toDoubleOrNull()
                val rate = if (isLand) 0.0 else (rateText.toDoubleOrNull() ?: 0.0)
                if (nameText.isNotEmpty() && cost != null && cost > 0) {
                    onRegisterAsset(nameText, cost, rate, isLand)
                    nameText = ""
                    costText = ""
                    rateText = ""
                    isLand = false
                }
            },
            enabled = nameText.isNotEmpty() && costText.toDoubleOrNull() != null && (isLand || rateText.toDoubleOrNull() != null),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("register_asset_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Register Asset")
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registered Assets List",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (assets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No assets registered yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(assets) { asset ->
                    val monthlyDep = if (asset.isLand) 0.0 else (asset.cost * (asset.rate / 100.0)) / 12.0
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = asset.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                )
                                Text(
                                    text = if (asset.isLand) "Land (No Depreciation)" else "Depreciation Rate: ${asset.rate}%",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = "Purchase Cost: ${formatCurrency(asset.cost)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF334155)
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text(
                                    text = "Monthly Dep:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = formatCurrency(monthlyDep),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                )
                            }
                            IconButton(onClick = { onDeleteAsset(asset) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete asset",
                                    tint = Color(0xFFFDA4AF)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 5. PROFIT & LOSS REPORT SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfitLossScreen(
    incomeList: List<IncomeEntryEntity>,
    expenseList: List<ExpenseEntryEntity>,
    assetList: List<FixedAssetEntity>
) {
    val totalIncome = incomeList.sumOf { it.amount }
    val totalMaterialCost = expenseList.filter { it.mainCategory == "Material" }.sumOf { it.amount }
    val totalOperatingCost = expenseList.filter { it.mainCategory == "Operating" }.sumOf { it.amount }

    var totalDepreciation = 0.0
    for (asset in assetList) {
        if (!asset.isLand) {
            totalDepreciation += (asset.cost * (asset.rate / 100.0)) / 12.0
        }
    }

    val totalExpenses = totalMaterialCost + totalOperatingCost + totalDepreciation
    val netProfit = totalIncome - totalExpenses

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Month-End Profit & Loss",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Start
        )

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .testTag("pl_statement_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Statement Title Header
                Text(
                    text = "KODI BUSINESS BOOK",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.5.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Profit & Loss Statement",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "For Month Ended: ${getCurrentMonthYearString()}",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))

                // Revenue Row
                PLRow(
                    title = "Total Revenue / Income",
                    amount = totalIncome,
                    isBold = true,
                    textColor = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Less: Operating Expenses",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Expense Subcategories
                PLSubRow(title = "Material Cost Total", amount = totalMaterialCost)
                PLSubRow(title = "Fixed Operating Expenses", amount = totalOperatingCost)
                PLSubRow(title = "Fixed Asset Depreciation (Land Excl.)", amount = totalDepreciation)

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Total Expense Row
                PLRow(
                    title = "Total Expenses",
                    amount = totalExpenses,
                    isBold = true,
                    textColor = Color(0xFFFF9800)
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Net Profit / Loss Row
                val label = if (netProfit >= 0) "NET PROFIT" else "NET LOSS"
                val finalColor = if (netProfit >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                PLRow(
                    title = label,
                    amount = netProfit,
                    isBold = true,
                    textColor = finalColor,
                    textSize = 16
                )

                Spacer(modifier = Modifier.height(40.dp))
                HorizontalDivider(thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Generated Automatically by KODI SOLUTIONS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PLRow(
    title: String,
    amount: Double,
    isBold: Boolean = false,
    textColor: Color = Color.Unspecified,
    textSize: Int = 14
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                fontSize = textSize.sp
            ),
            color = if (textColor != Color.Unspecified) textColor else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatCurrency(amount),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                fontSize = textSize.sp
            ),
            color = if (textColor != Color.Unspecified) textColor else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PLSubRow(
    title: String,
    amount: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "• $title",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatCurrency(amount),
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
