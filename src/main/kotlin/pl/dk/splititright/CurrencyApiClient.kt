package pl.dk.splititright

import com.fasterxml.jackson.databind.JsonNode
import feign.RequestInterceptor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam


@FeignClient(name = "currency-api", url = "\${client.currency-api.url}")
internal interface CurrencyApiClient {

    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java)
    }

    @GetMapping("/latest")
    fun getCurrencyRatingsPlain(
        @RequestParam(value = "currencies") currencies: String,
        @RequestParam(value = "base_currency") baseCurrency: String
    ): JsonNode

    fun getCurrencies(
        currencies: Set<Currency>,
        baseCurrency: Currency
    ): Map<Currency, Float> {
        val currenciesRateJson = getCurrencyRatingsPlain(
            currencies = currencies.joinToString(separator = ",") { it.name },
            baseCurrency = baseCurrency.toString()
        ).get("data")
        LOG.debug("Fetched currencies: ${currenciesRateJson.toPrettyString()}")
        return currencies.associateWith { currenciesRateJson.get(it.name).floatValue() }
    }
}

@Configuration
internal class CurrencyApiAuthInterceptor(
    @Value("\${client.currency-api.key}") private val apiKey: String
){

    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { it.query("apikey", apiKey) }
    }
}
