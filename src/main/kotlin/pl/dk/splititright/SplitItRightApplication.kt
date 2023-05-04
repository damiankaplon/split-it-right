package pl.dk.splititright

import com.vaadin.flow.component.dependency.JsModule
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.theme.Theme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
@Theme("app")
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
class SplitItRightApplication : AppShellConfigurator

fun main(args: Array<String>) {
    runApplication<SplitItRightApplication>(*args)
}
