package pt.isel.tds.galokmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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


open class AppViewModel(val st: GameStorage, val cs: CoroutineScope) {

    //////////
    // Match Management
    var match by mutableStateOf(Match(st))
        private set
    val matchRun get() = (match as MatchRun)
    val isMatchRunning get() = (match is MatchRun)
    fun play(pos: Position) = execOrError {
        if (isMatchRunning && matchRun.game.state is Run) {
            match = match.play(pos)
            asyncRefresh()
        }
    }
    fun newBoard() = execOrError { match = match.new() }
    fun start(name: Name) = execOrError {
        cancelAsyncRefresh()
        match = match.start(name)
    }
    fun join(name: Name) = execOrError {
        cancelAsyncRefresh()
        match = match.join(name)
        asyncRefresh()
    }

    private var refreshJob: Job? = null
    private fun asyncRefresh() {
        dialogs[WAITING_DIALOG_ID]?.showDialog()
        refreshJob = cs.launch {
            while(!matchRun.canNew) {
                try {
                    withContext(Dispatchers.IO) {
                        match = match.refresh()
                    }
                    break
                }
                catch(e: NoChangesException) {/* Ignore */}
                catch (e: Exception) {
                    showError(e.message ?: "Unknown error")
                    if (e is GameDeletedException) match = Match(match.gs)
                    break
                }

                delay(1.seconds)
            }
            refreshJob = null
            dialogs[WAITING_DIALOG_ID]?.hideDialog()
        }
    }
    private fun cancelAsyncRefresh() {
        refreshJob?.cancel()
        refreshJob = null
        dialogs[WAITING_DIALOG_ID]?.hideDialog()
    }
    open fun exit() {
        cancelAsyncRefresh()
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
        dialogs[ERROR_DIALOG_ID]?.showDialog()  // Only called if user defined an "error" dialog.
    }
    fun execOrError(action: () -> Unit) {
        try {
            action()
        }
        catch (e: Exception) {
            showError(e.message ?: "Unknown error")
        }
    }

    companion object {
        val WAITING_DIALOG_ID = "waiting"
        val ERROR_DIALOG_ID = "error"
    }
}

const val STORAGE_BASE_NAME = "savedGames"

class AppViewModelTextFile(cs: CoroutineScope) : AppViewModel(
    TextFileStorage(STORAGE_BASE_NAME, GameSerializer),
    cs
)

class AppViewModelMongo(
    val driver: MongoDriver = MongoDriver("Galo"),
    cs: CoroutineScope
) : AppViewModel(
    MongoStorage(STORAGE_BASE_NAME, driver, GameSerializer),
    cs
) {
    override fun exit() {
        super.exit()
        driver.close()
    }
}