package pt.isel.tds.galokmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isel.tds.galokmp.model.Name
import pt.isel.tds.galokmp.model.Player
import pt.isel.tds.galokmp.model.Score


@Composable
fun ScoreDialog(score: Score, onClose: ()->Unit) = AlertDialog(
    onDismissRequest = onClose,
    title = { Text("Score") },
    confirmButton = { TextButton(onClick = onClose) { Text("Close") } },
    modifier = Modifier.fillMaxWidth(0.8f),
    text = {
        Column {
            val modifier = Modifier.padding(5.dp)
            Player.entries.forEach { player ->
                Row (modifier) {
                    Cell(Modifier.size(20.dp), player)
                    Text(" - ${score[player]}")
                }
            }
            Row (modifier) {
                Text("Draws - ${score[null]}")
            }
        }
    },
)

@Composable
fun MatchNameDialog(purpose: String, onCancel: ()->Unit, onConfirm: (Name)->Unit) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onCancel,
        dismissButton = {
            TextButton(onClick = onCancel) { Text("Cancel") }
        },
        title = { Text("Name the Match to $purpose", style=MaterialTheme.typography.h5) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name of Match") },
            )
        },
        confirmButton = {
            TextButton(enabled = Name.isValid(name),
                onClick = { onConfirm(Name(name)) }) { Text(purpose) }
        }
    )
}