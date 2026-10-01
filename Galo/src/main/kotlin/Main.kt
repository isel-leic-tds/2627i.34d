import model.Game
import model.canPlay
import model.new
import model.play
import model.show

fun main() {

    var game: Game? = null
    while(true) {
        // Ler o comando
        print("# ")
        val cmd = readln().uppercase().split(" ")
        when (cmd[0]) {

            "NEW" -> game = game?.new() ?: Game()
            "PLAY" -> if (game != null) {
                val pos = cmd[1].toInt()
                if (game.canPlay(pos)) game = game.play(pos)
            }
            "EXIT" -> break
            else -> println("Unknown command")
        }
        game?.show()
    }
}