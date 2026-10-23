package model

import storage.*

sealed class GameState

// Classes concretas que representam os estados
data class Run(val turn: Player): GameState()
data class Win(val winner: Player): GameState()
object Draw : GameState()

object GameStateSerializer: Serializer<GameState> {
    override fun serialize(data: GameState) = when (data) {
        is Run -> "Run:${data.turn.name}"
        is Win -> "Win:${data.winner.name}"
        Draw -> "Draw:"
    }

    override fun deserialize(text: String): GameState =
        text.split(':').let { (type, player) ->
            when (type) {
                "Run" -> Run(player.toPlayer())
                "Win" -> Win(player.toPlayer())
                "Draw" -> Draw
                else -> error("Unknown game state: $type")
            }
        }
}