package model

sealed class GameState

// Classes concretas que representam os estados
data class Run(val turn: Player): GameState()
data class Win(val winner: Player): GameState()
object Draw : GameState()