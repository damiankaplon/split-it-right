package pl.dk.splititright

data class Person(
    val name: String,
    val capital: Money
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Person) return false
        return this.name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}