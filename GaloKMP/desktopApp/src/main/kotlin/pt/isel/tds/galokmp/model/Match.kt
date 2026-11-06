package pt.isel.tds.galokmp.model


open class Match(val gs: GameStorage) {

    fun deleteIfIsOwner() {
        if (this is MatchRun && localPlayer == Player.X) gs.delete(id)
    }
    fun start(name: Name): MatchRun {
        deleteIfIsOwner()
        return MatchRun(gs, name, Player.X, Game().also {
            gs.create(name, it)
        })
    }

    fun join(name: Name): MatchRun  = gs.read(name)?.let {
        deleteIfIsOwner()
        MatchRun(gs, name, Player.O, it)
    } ?: error("Match $name not found")

    fun exit() = Match(gs).also {
        deleteIfIsOwner()
    }

    fun ensureMatchRun(actions: MatchRun.()-> Game): Match {
        check(this is MatchRun) { "Match not started" }
        return MatchRun(gs, id, localPlayer, actions())
    }
    fun play(pos: Position): Match = ensureMatchRun {
        check((game.state as Run).turn == localPlayer) { "Not your turn" }
        game.play(pos).also {
            gs.update(id, it)
        }
    }

    fun new(): Match = ensureMatchRun {
        check(canNew) { "Cannot create a new board" }
        game.new().also {
            gs.update(id, it)
        }
    }
/*
    fun showScore() = ensureMatchRun {
        game.also { it.showScore() }
    }
*/
    fun refresh() = ensureMatchRun {
        gs.read(id)?.also {
            check(it != game) { "No changes" }
        } ?: error("Game not found in the storage")
    }


}

class MatchRun(
    gs: GameStorage,
    val id: Name,
    val localPlayer: Player,
    val game: Game
) : Match(gs) {

    val canNew get() =
        localPlayer == when (game.state) {
            is Run -> game.state.turn
            else -> game.first.other
        }
}