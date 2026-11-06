package pt.isel.tds.galokmp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

import pt.isel.tds.galokmp.model.*
import galokmp.desktopapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun Cell(modifier: Modifier, player: Player?, onClick: () -> Unit) {

    if (player == null) Box(modifier.clickable(onClick = onClick))
    else {
        val resource = when(player) {
            Player.X -> Res.drawable.X
            Player.O -> Res.drawable.O
        }
        Image(
            painterResource(resource),
            contentDescription = null,
            modifier
        )
    }
}

val CELL_SIDE = 100
val CELL_PADDING = 5
val BOARD_SIDE = BOARD_SIZE * CELL_SIDE + CELL_PADDING * (BOARD_SIZE - 1)

@Composable
fun BoardView(board: Board, onClick: (Position) -> Unit) {

    Column(
        modifier = Modifier.background(Color.Black).size(BOARD_SIDE.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(BOARD_SIZE) { row ->

            Row (
                modifier = Modifier.fillMaxWidth().height(CELL_SIDE.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                repeat(BOARD_SIZE) { col ->

                    val pos = Position(row, col)
                    Cell(Modifier.background(Color.White).size(CELL_SIDE.dp),
                        board[pos],
                        {onClick(pos)})
                }
            }
        }
    }
}

@Composable
private fun App() {
    //var text by remember { mutableStateOf("Hello, World!") }
    var player by mutableStateOf(Player.X)
    MaterialTheme {
        Row(verticalAlignment = Alignment.CenterVertically) {
            //Cell(250.dp, player)
            Button(onClick = {
                player = player.other
            }) {
                Text("Other player")
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
