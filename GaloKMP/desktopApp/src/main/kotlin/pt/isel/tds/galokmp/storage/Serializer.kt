package pt.isel.tds.galokmp.storage

interface Serializer<Data> {

    fun serialize(data: Data): String
    fun deserialize(text: String): Data
}