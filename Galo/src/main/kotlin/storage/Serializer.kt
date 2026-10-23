package storage

interface Serializer<Data> {

    fun serialize(date: Data): String
    fun deserialize(text: String): Data
}