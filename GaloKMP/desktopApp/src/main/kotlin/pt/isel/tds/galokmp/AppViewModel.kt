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


open class AppViewModel(val st: GameStorage) {

    //////////
    // Game Management
    var match by mutableStateOf(Match(st))
        private set
    val matchRun get() = (match as MatchRun)
    val isMatchRunning get() = (match is MatchRun)
    fun play(pos: Position) {
        if (isMatchRunning && matchRun.game.state is Run) match = match.play(pos)
    }
    fun newBoard() { match = match.new() }
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

    fun start(name: Name) {
        match = match.start(name)
    }

    fun join(name: Name) {
        match = match.join(name)
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