package com.example.spendwise

import android.content.Context
import android.util.Log

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.spendwise.ui.theme.SpendwiseTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.spendwise.Transaction



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpendwiseTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val currentUser = prefs.getString("current_user", null)

                var budget by remember { mutableStateOf(0.0) }
                val expenses = remember { mutableStateListOf<Transaction>() }
                val incomes = remember { mutableStateListOf<Transaction>() }

                var isLoggedIn by remember {
                    mutableStateOf(
                        prefs.getBoolean(
                            "is_logged_in",
                            false
                        )
                    )
                }
                var drawerVisible by remember { mutableStateOf(false) }

                fun loadUserData(username: String) {
                    val dataPrefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                    val gson = Gson()
                    val type = object : TypeToken<List<Transaction>>() {}.type

                    budget = dataPrefs.getFloat("${username}_budget", 0f).toDouble()

                    val expensesJson = dataPrefs.getString("${username}_expenses", "[]")
                    Log.d("Spendwise", "Expenses JSON loaded: $expensesJson")
                    try {
                        val loadedExpenses: List<Transaction> = gson.fromJson(expensesJson, type)
                        expenses.clear()
                        expenses.addAll(loadedExpenses)
                    } catch (e: Exception) {
                        Log.e(
                            "Spendwise",
                            "Failed to parse expenses JSON, resetting to empty list",
                            e
                        )
                        expenses.clear()
                    }

                    val incomesJson = dataPrefs.getString("${username}_incomes", "[]")
                    Log.d("Spendwise", "Incomes JSON loaded: $incomesJson")
                    try {
                        val loadedIncomes: List<Transaction> = gson.fromJson(incomesJson, type)
                        incomes.clear()
                        incomes.addAll(loadedIncomes)
                    } catch (e: Exception) {
                        Log.e(
                            "Spendwise",
                            "Failed to parse incomes JSON, resetting to empty list",
                            e
                        )
                        incomes.clear()
                    }
                }

                fun saveUserData(username: String) {
                    val dataPrefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                    val editor = dataPrefs.edit()
                    editor.putFloat("${username}_budget", budget.toFloat())

                    val gson = Gson()
                    editor.putString("${username}_expenses", gson.toJson(expenses))
                    editor.putString("${username}_incomes", gson.toJson(incomes))
                    editor.apply()
                }

                fun saveIfUserExists() {
                    val user = prefs.getString("current_user", null)
                    if (user != null) saveUserData(user)
                }

                LaunchedEffect(isLoggedIn, currentUser) {
                    if (isLoggedIn && currentUser != null) {
                        loadUserData(currentUser)
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) "home" else "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                isLoggedIn = true
                                val loggedInUser = prefs.getString("current_user", null)
                                if (loggedInUser != null) {
                                    loadUserData(loggedInUser)
                                }
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToSignup = { navController.navigate("signup") }
                        )
                    }

                    composable("signup") {
                        SignupScreen(
                            onSignupSuccess = {
                                isLoggedIn = true
                                val signedUpUser = prefs.getString("current_user", null)
                                if (signedUpUser != null) {
                                    loadUserData(signedUpUser)
                                }
                                navController.navigate("home") {
                                    popUpTo("signup") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = { navController.popBackStack() }
                        )
                    }

                    composable("budgetInput") {
                        BudgetInputScreen(
                            onBudgetSet = { newBudget ->
                                budget = newBudget
                                val user = prefs.getString("current_user", null)
                                if (user != null) saveUserData(user)
                                navController.navigate("home") {
                                    popUpTo("budgetInput") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("home") {
                        SpendwiseApp(
                            expenses = expenses,
                            incomes = incomes,
                            modifier = Modifier.fillMaxSize(),
                            onMenuClick = { drawerVisible = true },
                            budget = budget,
                            onBudgetSet = { newBudget ->
                                budget = newBudget
                                saveIfUserExists()
                            },
                            onSaveUserData = { saveIfUserExists() },
                            screen = "home",
                            navController = navController
                        )
                    }

                    composable("track") {
                        SpendwiseApp(
                            expenses = expenses,
                            incomes = incomes,
                            modifier = Modifier.fillMaxSize(),
                            onMenuClick = { drawerVisible = true },
                            budget = budget,
                            onBudgetSet = { newBudget ->
                                budget = newBudget
                                saveIfUserExists()
                            },
                            onSaveUserData = { saveIfUserExists() },
                            screen = "track",
                            navController = navController
                        )
                    }

                    composable("record") {
                        SpendwiseApp(
                            expenses = expenses,
                            incomes = incomes,
                            modifier = Modifier.fillMaxSize(),
                            onMenuClick = { drawerVisible = true },
                            budget = budget,
                            onBudgetSet = { newBudget ->
                                budget = newBudget
                                saveIfUserExists()
                            },
                            onSaveUserData = { saveIfUserExists() },
                            screen = "record",
                            navController = navController
                        )
                    }

                    composable("profile") {
                        ProfileScreen(
                            navController = navController,
                            onLogout = {
                                prefs.edit().apply {
                                    putBoolean("is_logged_in", false)
                                    putString("current_user", null)
                                    apply()
                                }
                                isLoggedIn = false
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onMenuClick = { drawerVisible = true }
                        )
                    }
                }

                Menu(
                    visible = drawerVisible,
                    onDismiss = { drawerVisible = false },
                    onOptionSelected = { option ->
                        navController.navigate(option) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        }
                        drawerVisible = false
                    }
                )
            }
        }

    }
}
