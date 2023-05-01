package pl.dk.splititright

import org.springframework.stereotype.Service

@Service
internal class SplittingService(
    private val currencyApi: CurrencyApiClient
) {

    fun splitItRight(people: Set<Person>, amount: Money): Map<Person, Money> {
        val mostCommonCurrency: Currency = people.groupBy { it.capital.currency }.maxByOrNull { it.value.size }?.key
            ?: Currency.PLN
        val allCurrencies: Set<Currency> = people.map { it.capital.currency }.toSet()
        val currencyRatings = currencyApi.getCurrencies(
            currencies = allCurrencies,
            baseCurrency = mostCommonCurrency
        )
        val currencyConverter = CurrencyConverter(currencyRatings, mostCommonCurrency)
        return Calculator.split(people, amount, currencyConverter)
    }
}