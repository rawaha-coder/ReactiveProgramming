

fun exampleOf(description: String, action: ()-> Unit){
    println("\n--- Example of: $description ---\n")
    action()
}

fun <T> printWithLabel(label: String, element: T?) {
    println("$label $element")
}

sealed class Quote: Throwable() {
    class NeverSaidThat : Quote()
}