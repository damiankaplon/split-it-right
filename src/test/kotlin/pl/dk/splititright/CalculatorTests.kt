package pl.dk.splititright

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.dk.splititright.ext.Bd
import java.util.stream.Stream

internal class CalculatorTests {

    @ParameterizedTest
    @MethodSource("provideDataSet")
    fun `should split right`(
        people: Set<Person>,
        amountToSplit: Money,
        currencyConverter: CurrencyConverter,
        expected: Map<Person, Money>
    ) {
        // when
        val result = Calculator.split(people, amountToSplit, currencyConverter)

        // then
        assertThat(result).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun provideDataSet(): Stream<Arguments> {
            return Stream.of(
                DataSet.simple(),
                DataSet.crossCurrency(),
                DataSet.crossCurrencyThreePeople(),
                DataSet.crossCurrencyBaseCurrencyEur()
            )
        }
    }
}

private object DataSet {

    fun simple(): Arguments {
        val damian = Person("Damian", Money(Bd("700000"), Currency.PLN))
        val wojtek = Person("Wojtek", Money(Bd("300000"), Currency.PLN))
        return Arguments.of(
            setOf(damian, wojtek),
            Money(denomination = Bd("1000000"), currency = Currency.PLN),
            CurrencyConverter(
                ratings = mapOf(
                    Currency.PLN to 1F,
                    Currency.EUR to 2F
                ),
                baseCurrency = Currency.PLN
            ),
            mapOf(
                damian to Money(Bd("700000"), Currency.PLN),
                wojtek to Money(Bd("300000"), Currency.PLN)
            )
        )
    }

    fun crossCurrency(): Arguments {
        val damian = Person("Damian", Money(Bd("100000"), Currency.EUR))
        val wojtek = Person("Wojtek", Money(Bd("300000"), Currency.PLN))
        return Arguments.of(
            setOf(damian, wojtek),
            Money(denomination = Bd("1000000"), currency = Currency.PLN),
            CurrencyConverter(
                ratings = mapOf(
                    Currency.PLN to 1F,
                    Currency.EUR to 0.14285715F
                ),
                baseCurrency = Currency.PLN
            ),
            mapOf(
                damian to Money(Bd("700000"), Currency.PLN),
                wojtek to Money(Bd("300000"), Currency.PLN)
            )
        )
    }

    fun crossCurrencyThreePeople(): Arguments {
        val damian = Person("Damian", Money(Bd("100000"), Currency.EUR))
        val wojtek = Person("Wojtek", Money(Bd("200000"), Currency.EUR))
        val paulina = Person("Paulina", Money(Bd("500000"), Currency.PLN))
        return Arguments.of(
            setOf(damian, wojtek, paulina),
            Money(denomination = Bd("10000000"), currency = Currency.PLN),
            CurrencyConverter(
                ratings = mapOf(
                    Currency.PLN to 1F,
                    Currency.EUR to 0.2F
                ),
                baseCurrency = Currency.PLN
            ),
            mapOf(
                damian to Money(Bd("2500000"), Currency.PLN),
                wojtek to Money(Bd("5000000"), Currency.PLN),
                paulina to Money(Bd("2500000"), Currency.PLN)
            )
        )
    }

    fun crossCurrencyBaseCurrencyEur(): Arguments {
        val damian = Person("Damian", Money(Bd("100000"), Currency.EUR))
        val wojtek = Person("Wojtek", Money(Bd("200000"), Currency.EUR))
        val paulina = Person("Paulina", Money(Bd("400000"), Currency.PLN))
        return Arguments.of(
            setOf(damian, wojtek, paulina),
            Money(denomination = Bd("2000000"), currency = Currency.PLN),
            CurrencyConverter(
                ratings = mapOf(
                    Currency.PLN to 4F,
                    Currency.EUR to 1F
                ),
                baseCurrency = Currency.EUR
            ),
            mapOf(
                damian to Money(Bd("125000"), Currency.EUR),
                wojtek to Money(Bd("250000"), Currency.EUR),
                paulina to Money(Bd("125000"), Currency.EUR)
            )
        )
    }
}

