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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import galokmp.desktopapp.generated.resources.O
import galokmp.desktopapp.generated.resources.Res
import galokmp.desktopapp.generated.resources.X
import org.jetbrains.compose.resources.painterResource
import pt.isel.tds.galokmp.model.BOARD_SIZE
import pt.isel.tds.galokmp.model.Board
import pt.isel.tds.galokmp.model.Draw
import pt.isel.tds.galokmp.model.GameState
import pt.isel.tds.galokmp.model.Player
import pt.isel.tds.galokmp.model.Position
import pt.isel.tds.galokmp.model.Position.Companion.invoke
import pt.isel.tds.galokmp.model.Run
import pt.isel.tds.galokmp.model.Win


@Composable
fun Cell(modifier: Modifier, player: Player?, onClick: () -> Unit = {}) {

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

val STATUS_HEIGHT = 70.dp

@Composable
fun StatusBar(state: GameState) {

    Row (
        modifier = Modifier.background(Color.White).width(BOARD_SIDE.dp).height(STATUS_HEIGHT),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        val (text, player) = when (state) {
            is Run -> "Turn:" to state.turn
            is Win -> "Winner:" to state.winner
            Draw -> "Draw" to null
        }
        LabeledCell(text, player)
    }
}

@Composable
fun LabeledCell(label: String, player: Player?) {
    Text(label, style = MaterialTheme.typography.h3)
    Cell(Modifier.padding(5.dp), player)
}
