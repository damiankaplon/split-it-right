package pl.dk.splititright

import java.math.BigDecimal
import kotlin.math.round

class CurrencyConverter(
    private val ratings: Map<Currency, Float>,
    val baseCurrency: Currency
) {

    fun convert(money: Money, to: Currency = baseCurrency): Money {
        val rating = rating(money.currency, to)
        val denomination = money.denomination.toLong() * rating
        return Money(BigDecimal.valueOf(round(denomination).toLong()), to)
    }

    private fun rating(from: Currency, to: Currency): Float {
        if (from == this.baseCurrency)
            return this.ratings[to]
                ?: throw IllegalArgumentException("Rating from ${this.baseCurrency} to $to is unknown")
        return 1.0F / (this.ratings[from]
            ?: throw IllegalArgumentException("Rating from ${this.baseCurrency} to $to is unknown"))
    }
}
