package pt.isel.tds.galokmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.isel.tds.galokmp.model.Game
import pt.isel.tds.galokmp.model.GameSerializer
import pt.isel.tds.galokmp.model.GameStorage
import pt.isel.tds.galokmp.model.Match
import pt.isel.tds.galokmp.model.Name
import pt.isel.tds.galokmp.model.Position
import pt.isel.tds.galokmp.model.Run
import pt.isel.tds.galokmp.model.*
import pt.isel.tds.galokmp.model.play
import pt.isel.tds.galokmp.storage.TextFileStorage
import pt.isel.tds.galokmp.storage.mongo.MongoDriver
import pt.isel.tds.galokmp.storage.mongo.MongoStorage
import kotlin.time.Duration.Companion.seconds


open class AppViewModel(val st: GameStorage) {

    //////////
    // Match Management
    var match by mutableStateOf(Match(st))
        private set
    val matchRun get() = (match as MatchRun)
    val isMatchRunning get() = (match is MatchRun)
    fun play(pos: Position) = execOrError {
        if (isMatchRunning && matchRun.game.state is Run) {
            match = match.play(pos)
        }
    }
    fun newBoard() = execOrError { match = match.new() }
    fun start(name: Name) = execOrError { match = match.start(name) }
    fun join(name: Name) = execOrError { match = match.join(name) }
    fun refresh() = execOrError { match = match.refresh() }
    open fun exit() {
        match.deleteIfIsOwner()
    }
    //////////
    // Dialog Management
    class DialogManagement {
        var dialogVisible by mutableStateOf(false)
            private set
        fun showDialog() { dialogVisible = true }
        fun hideDialog() { dialogVisible = false }
        var dialog: @Composable ()-> Unit = {}
    }

    val dialogs = mutableMapOf<String,DialogManagement>()

    fun addDialog(name: String) =
        DialogManagement().also {
            dialogs[name] = it
        }

    fun getDialog(name: String) = dialogs[name] ?: error("Dialog with name $name not found")

    //////////
    // Error Management
    var errorMessage = ""
    fun showError(message: String) {
        errorMessage = message
        dialogs["error"]?.showDialog()  // Only called if user defined an "error" dialog.
    }
    fun execOrError(action: () -> Unit) {
        try {
            action()
        }
        catch (e: Exception) {
            showError(e.message ?: "Unknown error")
        }
    }
}

const val STORAGE_BASE_NAME = "savedGames"

class AppViewModelTextFile : AppViewModel(
    TextFileStorage(STORAGE_BASE_NAME, GameSerializer)
)

class AppViewModelMongo(
    val driver: MongoDriver = MongoDriver("Galo")
) : AppViewModel(
    MongoStorage(STORAGE_BASE_NAME, driver, GameSerializer)
) {
    override fun exit() {
        super.exit()
        driver.close()
    }
}