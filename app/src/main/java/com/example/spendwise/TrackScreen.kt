package com.example.spendwise

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TrackScreen(
    expenses: List<Transaction>,
    onAddExpense: (Transaction) -> Unit,
    incomes: List<Transaction>,
    onAddIncome: (Transaction) -> Unit,
    budget: Double?,
    onBack: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }  // 0 = Expenses, 1 = Income
    var label by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())

    val backgroundBrown = Color(0xFF4E342E)
    val beige = Color(0xFFF5F5DC)
    val engravedTextColor = Color(0xFFD7C4B3)

    BackHandler(onBack = onBack)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrown)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Row with menu icon and month text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = engravedTextColor)
                }
                Text(
                    text = currentMonth,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = engravedTextColor,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tabs for Expenses / Income
            val tabs = listOf("Expenses", "Income")
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = beige,
                contentColor = backgroundBrown,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            label = ""
                            amount = ""
                        },
                        text = { Text(title, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Label",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = beige,
                modifier = Modifier.align(Alignment.Start)
            )

            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                textStyle = LocalTextStyle.current.copy(color = beige),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Amount",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = beige,
                modifier = Modifier.align(Alignment.Start)
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                textStyle = LocalTextStyle.current.copy(color = beige),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    if (label.isNotBlank() && amountDouble > 0) {
                        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val transaction = Transaction(
                            type = if (selectedTab == 0) "expense" else "income",
                            amount = amountDouble,
                            label = label,
                            date = dateStr,
                            note = ""
                        )
                        if (selectedTab == 0) {
                            onAddExpense(transaction)
                        } else {
                            onAddIncome(transaction)
                        }
                        label = ""
                        amount = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("✔️")
            }

        }
    }
}
