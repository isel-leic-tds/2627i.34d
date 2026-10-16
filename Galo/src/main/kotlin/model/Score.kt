package model

typealias Score = Map<Player?, Int>

fun emptyScore() : Score =
    (Player.entries + null).associateWith { 0 }