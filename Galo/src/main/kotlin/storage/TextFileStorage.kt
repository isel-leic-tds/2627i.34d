package storage

import okio.*
import okio.Path.Companion.toPath

// baseDir/abc.txt <- data
class TextFileStorage<Key,Data>(
    baseFolderName: String,
    private val serializer: Serializer<Data>
) : Storage<Key,Data> {
    private val fs = FileSystem.SYSTEM
    private val basePath: Path = baseFolderName.toPath()

    private fun Path.exists() = fs.exists(this)
    private fun Path.delete() = fs.delete(this)
    private fun Path.createDirectory() = fs.createDirectories(this)
    private fun Path.isDirectory() = fs.metadata(this).isDirectory
    private fun Path.writeText(data: Data) {
        fs.write(this) { writeUtf8(serializer.serialize(data)) }
    }
    private fun Path.readText(): Data =
        fs.read(this) { serializer.deserialize(readUtf8()) }

    private inline fun <R> withPath(key: Key, fx: Path.() -> R): R =
        (basePath / "$key.txt").fx()

    init {
        if (!fs.exists(basePath))
            fs.createDirectory(basePath)
        else check(fs.metadata(basePath).isDirectory)
    }
    override fun create(key: Key, data: Data) = withPath(key) {
        check(!exists()) { "Key $key already exists."}
        writeText(data)
    }

    override fun read(key: Key): Data? = withPath(key) {
        return try {
            readText()
        }
        catch(_: IOException) { null }
    }

    override fun update(key: Key, data: Data) = withPath(key) {
        check(exists()) { "Key $key does not exist."}
        writeText(data)
    }

    override fun delete(key: Key) = withPath(key) {
        check(exists()) { "Key $key does not exist."}
        delete()
    }
}
