fun main() {

    val stk = MutableStack<Int>()
    stk.push(1)
    stk.push(2)
    print(stk.top)
    stk.push(3)
    while(!stk.isEmpty()) {
        val elem = stk.pop()
        print(elem)
    }
}