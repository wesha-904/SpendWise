package com.example.spendwise

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Menu(
    visible: Boolean,
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit,
) {
    if (!visible) return

    val darkBrown = Color(0xFF4E342E) // Dark brown color

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300)
            ),
            modifier = Modifier
                .fillMaxHeight()
                .width(180.dp) // Thinner drawer
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                )
                .clickable(enabled = false) {} // Prevent dismiss on internal clicks
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close Drawer", tint = darkBrown)
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Vertical column for options
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DrawerOption(icon = Icons.Default.Home, label = "Home", darkBrown = darkBrown) {
                        onOptionSelected("home")
                    }
                    DrawerOption(icon = Icons.Default.Assignment, label = "Records", darkBrown = darkBrown) {
                        onOptionSelected("Records")
                    }
                    DrawerOption(icon = Icons.Default.Person, label = "Profile", darkBrown = darkBrown) {
                        onOptionSelected("profile")
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerOption(icon: ImageVector, label: String, darkBrown: Color, onClick: () -> Unit) {
    Column(
        Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(40.dp), tint = darkBrown)
        Spacer(Modifier.height(8.dp))
        Text(label, fontSize = 16.sp, color = darkBrown)
    }
}
