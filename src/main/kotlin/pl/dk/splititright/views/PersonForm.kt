package pl.dk.splititright.views

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import pl.dk.splititright.Currency
import pl.dk.splititright.Money
import pl.dk.splititright.Person
import java.math.BigDecimal

internal class PersonForm : VerticalLayout() {

    private val nameField = TextField("Name")
    private val incomeField = NumberField("Income")
    private val currencyCombo = ComboBox("Currency", Currency.values().map { it.name })

    fun whenChanged(callback: () -> Unit) {
        this.nameField.addValueChangeListener { callback.invoke() }
        this.incomeField.addValueChangeListener { callback.invoke() }
    }

    val isFilled: Boolean
        get() {
            return !(nameField.value.isNullOrBlank()) && incomeField.value != null
        }

    init {
        this.addClassName("shadowBox")
        this.add(personForm())
    }

    fun focus() {
        this.nameField.focus()
    }

    fun person(): Person {
        return Person(
            name = nameField.value,
            capital = Money(
                denomination = BigDecimal.valueOf(incomeField.value.toLong() * 100),
                currency = Currency.valueOf(currencyCombo.value)
            ),
        )
    }

    private fun personForm(): Component {
        val form = FormLayout()
        nameField.isRequired = true
        form.add(nameField)
        incomeField.isRequired = true
        form.add(incomeField)
        currencyCombo.isRequired = true
        currencyCombo.value = Currency.PLN.toString()
        form.add(currencyCombo)
        return form
    }
}
