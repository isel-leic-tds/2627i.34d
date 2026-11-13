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
import org.jetbrains.compose.resources.painterResource

@Composable
private fun FrameWindowScope.App(onExit: () -> Unit) {
    val viewModel = remember {
        AppViewModelMongo().also { vm ->    /* AppViewModelTextFile() */
            vm.addDialog("start").apply {
                this.dialog = {
                    MatchNameDialog(
                        "Start",
                        { this.hideDialog() },
                        { name -> vm.start(name) })

                }
            }
            vm.addDialog("join").apply {
                this.dialog = {
                    MatchNameDialog(
                        "Join",
                        { this.hideDialog() },
                        { name -> vm.join(name) })
                }
            }
            vm.addDialog("score").apply {
                this.dialog = {
                    ScoreDialog(vm.matchRun.game.score, { this.hideDialog() })
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
            BoardView(viewModel.matchRun.game.board, viewModel::play)
            StatusBar(viewModel.matchRun.game.state)
        }

        for ((name, d) in viewModel.dialogs) {
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
