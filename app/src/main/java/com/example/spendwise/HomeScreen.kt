package com.example.spendwise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.spendwise.Transaction

import java.util.*

@Composable
fun HomeScreen(
    expenses: SnapshotStateList<Transaction>,
    incomes: SnapshotStateList<Transaction>,
    budget: Double,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    val backgroundBrown = Color(0xFF4E342E)
    val beige = Color(0xFFF5F5DC)
    val textDark = Color(0xFF2E2E2E)
    val engravedTextColor = Color(0xFFD7C4B3)

    // Formatters
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDateObj = Date()
    val currentDateStr = dateFormatter.format(currentDateObj)

    // Filter expenses and incomes to only those that start with current date
    val todayExpenses = expenses.filter { it.date.startsWith(currentDateStr) }
    val todayIncomes = incomes.filter { it.date.startsWith(currentDateStr) }

    val totalExpense = todayExpenses.sumOf { it.amount ?: 0.0 }
    val totalIncome = todayIncomes.sumOf { it.amount ?: 0.0 }

    val currentBalance = budget - totalExpense

    val currentDateDisplay = SimpleDateFormat("EEEE • MMMM dd, yyyy", Locale.getDefault()).format(currentDateObj)
    val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(currentDateObj)

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
            // Top Row with menu icon and month
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

            // Current Balance Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = beige),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Current Balance",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium,
                        color = textDark.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${"%.2f".format(currentBalance)}",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Expenses and Budget Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = beige),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Expenses",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = textDark.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${"%.2f".format(totalExpense)}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = Color(0xFF800000)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = beige),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Budget",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = textDark.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${"%.2f".format(budget)}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = Color(0xFF388E3C)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Current date text
            Text(
                text = currentDateDisplay,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp,
                color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Transaction List (filtered to today's transactions)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 80.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val beige = Color(0xFFF5F5DC)
                    val maroon = Color(0xFF800000)
                    val green = Color(0xFF388E3C)
                    val textDark = Color(0xFF2E2E2E)

                    val allEntries = todayExpenses.map { it to "expense" } + todayIncomes.map { it to "income" }

                    allEntries.forEach { (transaction, type) ->
                        val amount = transaction.amount ?: 0.0
                        val label = transaction.label
                        val signedAmount = if (type == "expense") "-${"%.2f".format(amount)}" else "+${"%.2f".format(amount)}"
                        val amountColor = if (type == "expense") maroon else green

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(beige, RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textDark,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = signedAmount,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = amountColor,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Floating Action Button (overlays bottom)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = Color.White,
                    contentColor = backgroundBrown,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }
}
