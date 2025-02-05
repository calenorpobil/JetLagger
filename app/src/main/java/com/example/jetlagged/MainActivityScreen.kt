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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data class ListItem(
    val emoji: String,
    val title: String,
    val description: String
)

val itemsList = listOf(
    ListItem("☕", "Tomar café", "Registro de consumo diario"),
    ListItem("🏋️", "Ir al gimnasio", "Sesiones de entrenamiento"),
    ListItem("📔", "Diario", "Registro diario personal"),
    ListItem("⚖️", "Peso", "Control de peso corporal"),
    ListItem("🍎", "Comidas", "Registro nutricional"),
    ListItem("😊", "Ánimo", "Estado emocional diario"),
    ListItem("📚", "Lectura", "Tiempo de lectura diario")
)

@Composable
fun MainActivityScreen() {
    //Gestionar la pila de navegación (qué pantalla está visible, historial, etc.):
    val navController = rememberNavController()
    //Mantener los valores ingresados por el usuario en todas las pantallas:
    var savedValues by rememberSaveable { mutableStateOf(emptyMap<String, Int>()) }

    //Contenedor que define todas las pantallas/rutas posibles
    NavHost(
        navController = navController,
        //Pantalla inicial:
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                savedValues = savedValues,
                //Navegar a la pantalla:
                onItemClick = { item -> navController.navigate("detail/${item.title}") },
                //Actualiza el mapa global cuando se guardan nuevos valores:
                onValuesUpdate = { newValues -> savedValues = newValues }
            )
        }
        composable("detail/{title}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            DetailScreen(
                //Parámetro dinámico en la ruta:
                title = title,
                currentValue = savedValues[title]?.toString() ?: "",
                onSave = { newValue ->
                    //Actualizar mapa:
                    if(newValue!="")
                    savedValues = savedValues + (title to newValue.toInt())
                    //Volver atras:
                    navController.popBackStack()
                },
                //Volver sin guardar:
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    savedValues: Map<String, Int>,       // Valores guardados (ej: "Tomar café" → 2)
    onItemClick: (ListItem) -> Unit,     // Callback al hacer clic en un ítem
    onValuesUpdate: (Map<String, Int>) -> Unit // Callback para actualizar valores
)
{
    Scaffold(
    //Componente de Material 3 que estructura la pantalla en secciones
    // (AppBar, contenido, etc.).
        topBar = {
            TopAppBar(
                //BARRA SUPERIOR
                title = { Text("CORREL JOURNAL") }
            )
        }
    ) { padding ->
        // Contenido principal aquí:
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(itemsList) { item ->
                ListItemRow(
                    item = item,
                    value = savedValues[item.title],
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

@Composable
fun ListItemRow(item: ListItem, value: Int?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.emoji,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall
            )
        }
        //El weight(1f) hace que se muestre a la derecha:
        Spacer(modifier = Modifier.weight(1f))
        //Solo ejecuta el bloque si value no es null:
        value?.let {
            Text(
                text = "$it",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    title: String,
    currentValue: String,
    onSave: (String) -> Unit,
    onBack: () -> Unit
) {
    var inputValue by remember { mutableStateOf(currentValue) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = inputValue,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\$"))) {
                        inputValue = newValue
                    }
                },
                label = { Text("Ingrese un número") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if(inputValue!=null){
                        onSave(inputValue)
                    }
                          },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Guardar")
            }
        }
    }
}