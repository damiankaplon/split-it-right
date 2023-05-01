package pl.dk.splititright

import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.*
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.router.Route
import java.math.BigDecimal
import kotlin.math.round


@Route("")
internal class MainView(private val splittingService: SplittingService) : VerticalLayout() {

  private companion object {
    val FORMS: MutableSet<PersonForm> = mutableSetOf()
  }

  private val amountToSplitField = NumberField("How much to split?")
  private val amountCurrencyBox = ComboBox("Currency", Currency.values().map { it.name })

  init {
    this.setSizeFull()
    this.alignItems = FlexComponent.Alignment.CENTER

    amountCurrencyBox.value = "PLN"

    val moneyToSplitLayout = HorizontalLayout()
    moneyToSplitLayout.add(amountToSplitField)
    moneyToSplitLayout.add(amountCurrencyBox)

    val peopleScroller = Scroller()
    peopleScroller.maxWidth = this.width
    peopleScroller.addThemeVariants(ScrollerVariant.LUMO_OVERFLOW_INDICATORS)
    peopleScroller.scrollDirection = Scroller.ScrollDirection.HORIZONTAL
    val peopleFormsLayout = HorizontalLayout()
    peopleScroller.content = peopleFormsLayout


    FORMS.add(addPersonForm(peopleFormsLayout))

    val addButton = Button("Add")
    addButton.addClickListener {
      val personForm = addPersonForm(peopleFormsLayout)
      FORMS.add(personForm)
      personForm.focus()
    }

    val splitButton = Button("Split it!")
    splitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)

    splitButton.addClickListener {
      val split = splittingService.splitItRight(
        people = FORMS.map { it.person() }.toSet(),
        amount = Money(
          denomination = (this.amountToSplitField.value * 100).toBigDecimal(),
          currency = Currency.valueOf(this.amountCurrencyBox.value)
        )
      )
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
    this.add(H1("Here is your fair split!"))
    split.forEach {
      val layout = VerticalLayout()
      layout.style.set("box-shadow", "rgba(0, 0, 0, 0.24) 0px 3px 8px")
      layout.add("<b> ${it.key.name} </b>")
      val amount: Float = round(it.value.denomination.div(BigDecimal.valueOf(100)).toFloat())
      layout.add(" fairly pays: <b> $amount ${it.value.currency.name} </b>")
      this.add(layout)
    }
  }

  private fun addPersonForm(parent: HasComponents): PersonForm {
    val layout = VerticalLayout()
    layout.alignItems = FlexComponent.Alignment.CENTER
    val removeButton = Button("Remove")
    removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR)
    val personForm = PersonForm()
    layout.add(removeButton, personForm)
    removeButton.addClickListener { parent.remove(layout) }
    parent.add(layout)
    return personForm
  }
}
