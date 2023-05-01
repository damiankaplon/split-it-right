package pl.dk.splititright.views

import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import pl.dk.splititright.Money
import pl.dk.splititright.Person
import java.math.BigDecimal
import kotlin.math.round

internal class ResultView(
    split: Map<Person, Money>
) : VerticalLayout() {

    init {
        this.alignItems = FlexComponent.Alignment.CENTER
        this.add(H1("Here is your fair split!"))
        split.forEach {
            val layout = VerticalLayout()
            layout.addClassName("shadowBox")
            layout.width = this.maxWidth
            layout.height = this.maxHeight
            layout.add(it.key.name)
            val amount: Float = round(it.value.denomination.div(BigDecimal.valueOf(100)).toFloat())
            layout.add(
                " fairly pays: $amount ${it.value.currency.name}"
            )
            this.add(layout)
        }
    }
}
