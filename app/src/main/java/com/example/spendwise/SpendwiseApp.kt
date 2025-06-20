package com.example.spendwise

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.spendwise.Transaction



@Composable
fun SpendwiseApp(
    navController: NavHostController,
    expenses: SnapshotStateList<Transaction>,
    incomes: SnapshotStateList<Transaction>,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    screen: String,
    budget: Double?,
    onBudgetSet: (Double) -> Unit,
    onSaveUserData: () -> Unit  // <-- add this parameter
) {
    when (screen) {
        "budgetInput" -> {
            BudgetInputScreen(onBudgetSet = onBudgetSet)
        }
        "home" -> {
            HomeScreen(
                onAddClick = { navController.navigate("track") },
                onMenuClick = onMenuClick,
                expenses = expenses,
                incomes = incomes,
                budget = budget ?: 0.0
            )
        }
        "track" -> {
            TrackScreen(
                expenses = expenses,
                incomes = incomes,
                budget = budget,
                onAddExpense = { newExpense ->
                    expenses.add(newExpense)
                    onSaveUserData()  // save after add
                },
                onAddIncome = { newIncome ->
                    incomes.add(newIncome)
                    onBudgetSet((budget ?: 0.0) + newIncome.amount)
                    onSaveUserData()  // save after add
                },
                onBack = { navController.popBackStack() },
                onMenuClick = onMenuClick
            )
        }
        "record" -> {
            RecordScreen(
                expenses = expenses,
                incomes = incomes,
                onMenuClick = onMenuClick
            )
        }
        "profile" -> {
            ProfileScreen()
        }
    }
}

@Composable
fun RecordScreen(
    expenses: SnapshotStateList<Transaction>,
    incomes: SnapshotStateList<Transaction>,
    onMenuClick: () -> Unit
) {
    // Just a placeholder showing total expenses and incomes count
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Record Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("Total expenses: ${expenses.size}")
        Text("Total incomes: ${incomes.size}")
    }
}


@Composable
fun ProfileScreen() {
    Text("Profile Screen")
}

@Composable
fun BudgetInputScreen(onBudgetSet: (Double) -> Unit) {
    var input by remember { mutableStateOf("") }
    val isValid = input.toDoubleOrNull()?.let { it > 0 } ?: false

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Enter your budget:", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Budget") },
                singleLine = true,
                isError = !isValid && input.isNotBlank()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onBudgetSet(input.toDouble()) },
                enabled = isValid
            ) {
                Text("Set Budget")
            }
        }
    }
}
