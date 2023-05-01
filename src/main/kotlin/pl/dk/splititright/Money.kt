package pl.dk.splititright

import java.math.BigDecimal

data class Money(
  val denomination: BigDecimal,
  val currency: Currency
)

enum class Currency {
  EUR,
  PLN
}