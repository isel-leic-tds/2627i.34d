package pt.isel.tds.galokmp.model

@JvmInline
value class Name(val name: String) {
    init {
        require(isValid(name)) { "Name $name is not valid" }
    }
    override fun toString() = name
    companion object {
        fun isValid(name: String): Boolean {

            return name.isNotBlank() && name.all { it.isLetterOrDigit() }
        }
    }
}