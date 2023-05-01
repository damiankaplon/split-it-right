package pl.dk.splititright

class CurrencyConverter(
    private val ratings: Map<Currency, Float>,
    val baseCurrency: Currency
) {

    fun ratingToBase(currency: Currency): Float {
        return this.ratings[currency] ?: throw IllegalArgumentException("Rating of $currency is unknown")
    }
}
