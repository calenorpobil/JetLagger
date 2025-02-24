import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class FormItem(
    var dato: String = "",
    var descripcion: String = "",
    var tipo: String = "Número"
)

@Composable
fun EditarPantalla() {
    var items by remember { mutableStateOf(listOf(FormItem())) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "TOMAR CAFÉ",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Cabecera
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "☕",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Tazas consumidas al día",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Sección de datos
        Text(
            text = "Datos recogidos:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = { items = items + FormItem() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Añadir dato")
        }

        // Lista de items
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(items) { index, item ->
                DataItem(
                    item = item,
                    onUpdate = { updatedItem ->
                        items = items.toMutableList().apply { set(index, updatedItem) }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        // Botones inferiores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { /* Cancelar */ }) {
                Text("Cancelar")
            }

            Button(onClick = { /* Guardar */ }) {
                Text("Guardar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataItem(item: FormItem, onUpdate: (FormItem) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val tipos = listOf("Número", "Fecha", "Texto")

    Column(modifier = modifier) {
        // Fila 1: Dato y Descripción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Dato", style = MaterialTheme.typography.labelMedium)
                TextField(
                    value = item.dato,
                    onValueChange = { onUpdate(item.copy(dato = it)) },
                    singleLine = true
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("Descripción", style = MaterialTheme.typography.labelMedium)
                TextField(
                    value = item.descripcion,
                    onValueChange = { onUpdate(item.copy(descripcion = it)) },
                    singleLine = true
                )
            }
        }

        // Fila 2: Tipo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Tipo", style = MaterialTheme.typography.labelMedium)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = item.tipo,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        tipos.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    onUpdate(item.copy(tipo = selectionOption))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Espacio para mantener la misma estructura de columnas
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}