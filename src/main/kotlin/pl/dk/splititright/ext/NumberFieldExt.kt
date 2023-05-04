package pl.dk.splititright.ext

import com.vaadin.flow.component.textfield.NumberField

fun NumberField.required(): NumberField {
    this.isRequired = true
    return this
}