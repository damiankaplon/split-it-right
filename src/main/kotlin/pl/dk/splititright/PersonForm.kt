package pl.dk.splititright

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import java.math.BigDecimal

internal class PersonForm : VerticalLayout() {

    private val nameField = TextField("Name")
    private val incomeField = TextField("Income")
    private val currencyCombo = ComboBox("Currency", Currency.values().map { it.name })


    init {
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
        val layout = VerticalLayout()
        layout.style.set("box-shadow", "rgba(0, 0, 0, 0.24) 0px 3px 8px")
        nameField.isRequired = true
        layout.add(nameField)
        incomeField.pattern = "\\d+"
        incomeField.isRequired = true
        layout.add(incomeField)
        currencyCombo.isRequired = true
        layout.add(currencyCombo)
        return layout
    }
}
