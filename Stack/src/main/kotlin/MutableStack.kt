// MutableStack com Mutable List

class MutableStack<T> {

    val values: MutableList<T> = mutableListOf()

    fun push(elem: T) {
       values.add(elem)
    }
    fun pop(): T = top.also { values.removeLast() }
    fun isEmpty() = values.isEmpty()

    val top: T get() = values.last()
}