package pt.isel.tds.galokmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform