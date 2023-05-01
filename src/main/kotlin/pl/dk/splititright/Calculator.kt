package pl.dk.splititright

import java.math.BigDecimal.valueOf


object Calculator {

    fun split(
        people: Set<Person>,
        amount: Money,
        currencyConverter: CurrencyConverter
    ): Map<Person, Money> {
        val capitalInBaseCurrency = people.map {
            currencyConverter.convert(it.capital).denomination
        }.reduce { acc, fl -> acc + fl }

        val personToCapitalInBaseCurrency = people.associateWith {
            currencyConverter.convert(it.capital).denomination
        }

        val amountInBaseCurrency = currencyConverter.convert(amount).denomination

        val personCapitalShare = personToCapitalInBaseCurrency.map {
            it.key to (it.value.toFloat() / capitalInBaseCurrency.toFloat())
        }.toMap()
        return personCapitalShare.map {
            it.key to Money(
                denomination = valueOf((it.value * amountInBaseCurrency.toLong()).toLong()),
                currency = currencyConverter.baseCurrency
            )
        }.toMap()
    }
}
