package pt.isel.tds.galokmp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
private fun FrameWindowScope.App(onExit: () -> Unit) {
    val cs = rememberCoroutineScope()
    val viewModel = remember {
        AppViewModelMongo(cs = cs).also { vm ->    /* AppViewModelTextFile() */
            vm.addDialog("start").apply {
                this.dialog = {
                    MatchNameDialog(
                        "Start",
                        { this.hideDialog() },
                        { name -> this.hideDialog() ; vm.start(name) })
                }
            }
            vm.addDialog("join").apply {
                this.dialog = {
                    MatchNameDialog(
                        "Join",
                        { this.hideDialog() },
                        { name -> this.hideDialog() ; vm.join(name) })
                }
            }
            vm.addDialog("score").apply {
                this.dialog = {
                    ScoreDialog(vm.matchRun.game.score, vm.matchRun.id, { this.hideDialog() })
                }
            }
            vm.addDialog(AppViewModel.ERROR_DIALOG_ID).apply {
                this.dialog = {
                    ErrorDialog(vm.errorMessage, { this.hideDialog() })
                }
            }
            vm.addDialog(AppViewModel.WAITING_DIALOG_ID).apply {
                this.dialog = {
                    WaitingIndicatorDialog()
                }
            }
        }
    }

    MenuBar {
        Menu("Game") {
            Item("Start") { viewModel.getDialog("start").showDialog() }
            Item("Join") { viewModel.getDialog("join").showDialog() }
            Item("New Board") { viewModel.newBoard() }
            Item("Score") { viewModel.getDialog("score").showDialog() }
            Item("Exit", onClick = { viewModel.exit() ; onExit() })
        }
    }
    MaterialTheme {
        Column {
            if (viewModel.isMatchRunning) {
                BoardView(viewModel.matchRun.game.board, viewModel::play)
                StatusBar(viewModel.matchRun.game.state, viewModel.matchRun.localPlayer)
            }
            else
                Box(Modifier.size(BOARD_SIDE.dp, BOARD_SIDE.dp + STATUS_HEIGHT))
        }

        for ((_, d) in viewModel.dialogs) {
            if (d.dialogVisible) d.dialog()
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = {},
        title = "Jogo do Galo",
        icon = painterResource(Res.drawable.X),
        state = WindowState(size = DpSize.Unspecified),
        //resizable = false
    ) {
        App(::exitApplication)
    }
}
