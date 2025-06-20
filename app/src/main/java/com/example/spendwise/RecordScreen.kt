@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.spendwise

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import com.example.spendwise.Transaction


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordScreen(
    expenses: List<Transaction>,
    incomes: List<Transaction>,
    onMenuClick: () -> Unit
) {
    var selectedMonth by remember { mutableStateOf(LocalDate.now().monthValue) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val transactionsOnDate = remember(selectedDate, expenses, incomes) {
        (expenses + incomes).filter {
            it.date == selectedDate.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Records") },
                actions = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            MonthPicker(selectedMonth) { selectedMonth = it }
            Spacer(modifier = Modifier.height(8.dp))
            DatePicker(selectedMonth, onDateSelected = { selectedDate = it })

            Spacer(modifier = Modifier.height(16.dp))
            Text("Transactions on $selectedDate", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(transactionsOnDate) { transaction ->
                    TransactionCard(transaction)
                }
            }
        }
    }
}

@Composable
fun MonthPicker(selectedMonth: Int, onMonthChange: (Int) -> Unit) {
    val months = (1..12).map { Month.of(it).name.lowercase().replaceFirstChar { c -> c.uppercase() } }
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(months[selectedMonth - 1])
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            months.forEachIndexed { index, month ->
                DropdownMenuItem(
                    text = { Text(month) },
                    onClick = {
                        onMonthChange(index + 1)
                        expanded = false
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(month: Int, onDateSelected: (LocalDate) -> Unit) {
    val today = LocalDate.now()
    val year = today.year
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(daysInMonth) { day ->
            val date = LocalDate.of(year, month, day + 1)
            Text(
                text = date.dayOfMonth.toString(),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onDateSelected(date) },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = transaction.type, style = MaterialTheme.typography.bodyLarge)
            Text(text = "$${transaction.amount}", style = MaterialTheme.typography.bodyMedium)
            Text(text = transaction.date, style = MaterialTheme.typography.bodySmall)
        }
    }
}
