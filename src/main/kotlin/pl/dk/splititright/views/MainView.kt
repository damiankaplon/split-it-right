package pl.dk.splititright.views

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.*
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.router.Route
import pl.dk.splititright.Currency
import pl.dk.splititright.Money
import pl.dk.splititright.Person
import pl.dk.splititright.SplittingService
import pl.dk.splititright.ext.Bd
import pl.dk.splititright.ext.required


@Route("")
internal class MainView(private val splittingService: SplittingService) : VerticalLayout() {

  private companion object {
    val FORMS: MutableSet<PersonForm> = mutableSetOf()
  }

  private val amountToSplitField = NumberField("How much to split?").required()
  private val amountCurrencyBox = ComboBox("Currency", Currency.values().map { it.name })
  private val splitButton = Button("Split it!")

  init {
    this.setSizeFull()
    this.alignItems = FlexComponent.Alignment.CENTER
    this.splitButton.isEnabled = false
    amountCurrencyBox.value = Currency.PLN.name

    val moneyToSplitLayout = HorizontalLayout()
    moneyToSplitLayout.add(amountToSplitField)
    moneyToSplitLayout.add(amountCurrencyBox)

    val peopleScroller = Scroller()
    peopleScroller.maxWidth = this.width
    peopleScroller.addThemeVariants(ScrollerVariant.LUMO_OVERFLOW_INDICATORS)
    peopleScroller.scrollDirection = Scroller.ScrollDirection.HORIZONTAL
    val peopleFormsLayout = HorizontalLayout()
    peopleScroller.content = peopleFormsLayout


    addPersonForm(peopleFormsLayout)

    val addButton = Button("Add")
    addButton.addClickListener { addPersonForm(peopleFormsLayout) }

    splitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)

    splitButton.addClickListener {
      val split = splittingService.splitItRight(
        people = FORMS.map { it.person() }.toSet(),
        amount = Money(
          denomination = if (this.amountToSplitField.value == null || this.amountToSplitField.value == 0.0) Bd("0")
          else (this.amountToSplitField.value * 100).toBigDecimal(),
          currency = Currency.valueOf(this.amountCurrencyBox.value)
        )
      )
      FORMS.clear()
      presentSplit(split)
    }

    this.add(
      H1("Split it right!"),
      moneyToSplitLayout,
      peopleScroller,
      HorizontalLayout(addButton, splitButton)
    )
  }

  private fun presentSplit(split: Map<Person, Money>) {
    this.removeAll()
    this.setSizeFull()
    this.alignItems = FlexComponent.Alignment.CENTER
    this.add(ResultView(split))
  }

  private fun addPersonForm(parent: HasComponents) {
    this.splitButton.isEnabled = false
    val layout = VerticalLayout()
    layout.alignItems = FlexComponent.Alignment.CENTER
    val removeButton = Button("Remove")
    removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR)
    val personForm = PersonForm()
    FORMS.add(personForm)
    personForm.whenChanged { splitButton.isEnabled = FORMS.all { it.isFilled } }
    layout.add(removeButton, personForm)
    removeButton.addClickListener {
      FORMS.remove(personForm)
      parent.remove(layout)
      splitButton.isEnabled = FORMS.all { it.isFilled }
    }
    parent.add(layout)
  }
}
