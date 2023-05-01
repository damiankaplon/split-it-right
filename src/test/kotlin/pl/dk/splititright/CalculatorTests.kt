package pl.dk.splititright

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CalculatorTests {

    @Test
    fun `should split right`() {
        // given
        val people = setOf(
            Person("Damian", Money(BigDecimal.valueOf(800000), Currency.PLN)),
            Person("Wojtek", Money(BigDecimal.valueOf(200000), Currency.PLN))
        )
        val amountToSplit = Money(BigDecimal.valueOf(1000000), Currency.PLN)
        val currencyConverter = CurrencyConverter(
            mapOf(Currency.PLN to 1.0F),
            Currency.PLN
        )

        // when
        val result = Calculator.split(people, amountToSplit, currencyConverter)

        // then
        assertThat(result).anySatisfy { person, money ->
            person.name == "Damian" && money.denomination == BigDecimal.valueOf(800000) && money.currency == Currency.PLN
        }
        assertThat(result).anySatisfy { person, money ->
            person.name == "Wojtek" && money.denomination == BigDecimal.valueOf(200000) && money.currency == Currency.PLN
        }

    }
}