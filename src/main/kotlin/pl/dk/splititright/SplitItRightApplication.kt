package pl.dk.splititright

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class SplitItRightApplication

fun main(args: Array<String>) {
    runApplication<SplitItRightApplication>(*args)
}
