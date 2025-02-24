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
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
 * FILAS (TIPOS DE OCURRENCIAS)
 */
@Composable
fun ListItemRow(
    item: ListItem,
    isExpanded: Boolean,
    onExpand: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onView: () -> Unit,
    onAdd: () -> Unit) { // Nuevo callback para el botón "+"
    var localExpanded by remember { mutableStateOf(isExpanded) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                localExpanded = !localExpanded
                onExpand(localExpanded)
            }
            .padding(24.dp, 12.dp, 12.dp, 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // EMOJI
            Text(
                text = item.emoji,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.size(54.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                //TÍTULO
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                //DESCRIPCIÓN
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            IconButton(
                onClick = onAdd,
                modifier = Modifier
                    .size(40.dp) // Tamaño del botón
                    .background(
                        color = Color(219, 180, 125),
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,  // horizontal center of the text
                    modifier = Modifier
                        .padding(bottom = 4.dp) // Ajuste visual del símbolo
                )
            }
        }

        // AL HACER CLICK (BOTONES)
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botones izquierdos
                Row {
                    ActionButton(
                        text = "Borrar",
                        color = MaterialTheme.colorScheme.error,
                        onClick = onDelete
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    ActionButton(
                        text = "Editar",
                        color = MaterialTheme.colorScheme.primary,
                        onClick = onEdit
                    )
                }

                // Botón derecho
                ActionButton(
                    text = "Ver",
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = onView
                )
            }
        }
    }
}


@Composable
private fun ActionButton(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(color.copy(alpha = 0.1f))
            .padding(vertical = 8.dp, horizontal = 12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen() {
    var expandedItemId by remember { mutableStateOf<String?>(null) }
    val items = remember { mutableStateListOf(*itemsList.toTypedArray()) }

    Scaffold (
        topBar = {
            TopAppBar(
                //BARRA SUPERIOR
                title = { Text("DIARIO ESTADÍSTICO") }
            )
        }
    ){ padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            items(items) { item ->
                ListItemRow(
                    item = item,
                    onDelete = { /* Lógica borrar */ },
                    onEdit = { /* Lógica editar */ },
                    onView = { /* Lógica ver */ },
                    onAdd = {},
                    isExpanded = item.id == expandedItemId,
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
}