package storage.mongo

import org.bson.codecs.pojo.annotations.BsonProperty
import storage.Serializer
import storage.Storage
import javax.xml.crypto.Data

class MongoStorage<Key,Data>(
    collectionName: String,
    driver: MongoDriver,
    private val serializer: Serializer<Data>
) : Storage<Key,Data> {

    data class Doc(val _id: String, val value: String)
    private fun Doc(key: Key, data: Data) =
        Doc(key.toString(), serializer.serialize(data))

    val docs = driver.getCollection<Doc>(collectionName)

    // Interface Storage
    override fun create(key: Key, data: Data) {

        try {
            docs.insertDocument(Doc(key, data))
        }
        catch (e: Exception) {
            error("Document $key already exists.")
        }
    }

    override fun read(key: Key): Data? {

        return docs.getDocument(key.toString())?.let {
            serializer.deserialize(it.value)
        }
    }

    override fun update(key: Key, data: Data) {

        check(docs.replaceDocument(key.toString(), Doc(key, data))) {
            "Document $key does not exist." }
    }

    override fun delete(key: Key) {

        check(docs.deleteDocument(key.toString())) {
            "Document $key does not exist."
        }
    }
}
