/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jetlagged
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


val itemsList = listOf(
    ListItem(
        id = "coffee",
        emoji = "☕",
        title = "Tomar café",
        description = "Tazas consumidas al día",
        usageCount = 0,
        timesUsed = listOf(2, 3, 1, 2, 2, 3, 1) // Ejemplo de última semana
    ),
    ListItem(
        id = "gym",
        emoji = "🏋️",
        title = "Ir al gimnasio",
        description = "Sesiones de entrenamiento semanales",
        usageCount = 0,
        timesUsed = listOf(4, 3, 5, 4, 6, 5, 4)
    ),
    ListItem(
        id = "journal",
        emoji = "📔",
        title = "Diario",
        description = "Entradas diarias completadas",
        usageCount = 0,
        timesUsed = listOf(1, 1, 1, 1, 0, 1, 1)
    ),
    ListItem(
        id = "weight",
        emoji = "⚖️",
        title = "Peso",
        description = "Registro diario (kg)",
        usageCount = 0,
        timesUsed = listOf(72, 72, 71, 71, 71, 70, 70)
    ),
    ListItem(
        id = "meals",
        emoji = "🍎",
        title = "Comidas",
        description = "Comidas balanceadas consumidas",
        usageCount = 0,
        timesUsed = listOf(3, 2, 3, 3, 4, 3, 2)
    ),
    ListItem(
        id = "mood",
        emoji = "😊",
        title = "Ánimo",
        description = "Puntuación diaria (1-5)",
        usageCount = 0,
        timesUsed = listOf(4, 3, 5, 4, 4, 2, 4)
    ),
    ListItem(
        id = "reading",
        emoji = "📚",
        title = "Lectura",
        description = "Minutos diarios de lectura",
        usageCount = 0,
        timesUsed = listOf(30, 45, 20, 60, 15, 25, 40)
    )
)

data class ListItem(
    val id: String,
    val emoji: String,
    val title: String,
    val description: String,
    var usageCount: Int = 0,
    var timesUsed: List<Int> = emptyList() // Para la gráfica
)

/**
 * ESTO SOLO SE VE AL CLICAR UN ITEM:
 */
@Composable
fun ExpandedListItemDetails(item: ListItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize()
        ) {
            // Sección de descripción
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            //Spacer(modifier = Modifier.height(12.dp))

            // Estadísticas
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Veces usado:",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = item.usageCount.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Gráfica simple de barras
                SimpleUsageChart(timesUsed = item.timesUsed)
            }
        }
    }
}

@Composable
fun SimpleUsageChart(timesUsed: List<Int>, modifier: Modifier = Modifier) {
    val maxValue = timesUsed.maxOrNull() ?: 1
    val barColor = MaterialTheme.colorScheme.primary

    Column(modifier = modifier.width(100.dp)) {
        Text(
            text = "Últimos 7 días:",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        ) {
            timesUsed.takeLast(7).forEach { value ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp)
                        .height((value.toFloat() / maxValue * 40).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(barColor)
                )
            }
        }
    }
}

@Composable
fun ListItemRow(item: ListItem, expanded: Boolean, onExpand: (Boolean) -> Unit) {
    var localExpanded by remember { mutableStateOf(expanded) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                localExpanded = !localExpanded
                onExpand(localExpanded)
            }
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Contenido principal igual que antes
            Text(
                text = item.emoji,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        AnimatedVisibility(
            visible = localExpanded,
            enter = expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300))
        ) {
            ExpandedListItemDetails(item = item)
        }
    }
}

// En MainScreen
@Composable
fun MainActivityScreen() {
    var expandedItemId by remember { mutableStateOf<String?>(null) }
    val items = remember { mutableStateListOf(*itemsList.toTypedArray()) }

    LazyColumn {
        items(items) { item ->
            ListItemRow(
                item = item,
                expanded = item.id == expandedItemId,
                onExpand = { isExpanded ->
                    expandedItemId = if (isExpanded) {
                        // Actualizar estadísticas al expandir
                        val updatedItem = item.copy(
                            usageCount = item.usageCount + 1,
                            timesUsed = item.timesUsed + (1..7).random() // Datos de ejemplo
                        )
                        items[items.indexOf(item)] = updatedItem
                        updatedItem.id
                    } else {
                        null
                    }
                }
            )
        }
    }
}