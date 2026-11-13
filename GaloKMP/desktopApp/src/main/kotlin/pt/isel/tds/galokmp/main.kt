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
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.compose.ui.window.MenuBar

import pt.isel.tds.galokmp.model.*
import galokmp.desktopapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

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

class AppViewModel {

    //////////
    // Game Management
    var game by mutableStateOf(Game())
        private set
    fun play(pos: Position) {
        if (game.state is Run) game = game.play(pos)
    }
    fun newBoard() { game = game.new() }

    //////////
    // ScoreDialog Management
    var scoreDialogVisible by mutableStateOf(false)
        private set
    fun showScoreDialog() { scoreDialogVisible = true }
    fun hideScoreDialog() { scoreDialogVisible = false }
}
@Composable
private fun FrameWindowScope.App(onExit: () -> Unit) {
    val viewModel = remember { AppViewModel() }

    MenuBar {
        Menu("Game") {
            Item("New Game") { viewModel.newBoard() }
            Item("Score") { viewModel.showScoreDialog() }
            Item("Exit", onClick = onExit)
        }
    }
    MaterialTheme {
        Column {
            BoardView(viewModel.game.board, viewModel::play)
            StatusBar(viewModel.game.state)
        }
        if (viewModel.scoreDialogVisible)
            ScoreDialog(viewModel.game.score, { viewModel.hideScoreDialog() })
    }

}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Jogo do Galo",
        icon = painterResource(Res.drawable.X),
        state = WindowState(size = DpSize.Unspecified),
        resizable = false
    ) {
        App(::exitApplication)
    }
}
