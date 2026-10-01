package console

data class CommandLine(val name: String, val args: List<String>)

fun readCommand(): CommandLine {

    while(true) {
        print("# ")
        val line = readln().uppercase().split(" ").filter { it != "" }
        if (!line.isEmpty()) return CommandLine(line[0], line.drop(1))
    }
}