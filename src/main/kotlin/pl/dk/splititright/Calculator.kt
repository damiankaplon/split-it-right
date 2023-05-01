package pl.dk.splititright

import java.math.BigDecimal.valueOf


object Calculator {

    fun split(
        people: Set<Person>,
        amount: Money,
        currencyConverter: CurrencyConverter
    ): Map<Person, Money> {
        val capitalInBaseCurrency = people.map {
            val rating = currencyConverter.ratingToBase(it.capital.currency)
            it.capital.denomination.toLong() * rating
        }.reduce { acc, fl -> acc + fl }

        val personToCapitalInBaseCurrency = people.associateWith {
            it.capital.denomination.toLong() * currencyConverter.ratingToBase(it.capital.currency)
        }

        val amountInBaseCurrency = amount.denomination.toLong() * currencyConverter.ratingToBase(amount.currency)

        val personCapitalShare = personToCapitalInBaseCurrency.map {
            it.key to (it.value / capitalInBaseCurrency)
        }.toMap()
        return personCapitalShare.map {
            it.key to Money(
                denomination = valueOf((it.value * amountInBaseCurrency).toLong()),
                currency = currencyConverter.baseCurrency
            )
        }.toMap()
    }
}
