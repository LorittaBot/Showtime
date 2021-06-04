package net.perfectdreams.dokyoweb

/* import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.clear
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.sweetmorenitta.elements.ElementType
import net.perfectdreams.sweetmorenitta.elements.InputElementType
import net.perfectdreams.sweetmorenitta.views.*
import net.perfectdreams.sweetmorenitta.views.HelloWorldView
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement

fun main() {
    val header = HelloWorldView.HEADER_STUFF.get()

    println("Header: ${header.innerHTML}")

    console.log(HelloWorldView.BUTTON_STUFF.get())

    val textArea = HelloWorldView.TEXT_AREA_STUFF.get()

    println(textArea)

    textArea.addEventListener("input", {
        HelloWorldView.REPEAT_TEXT_HERE_STUFF.get().innerHTML = textArea.value
    })

    HelloWorldView.BUTTON_STUFF.get().addEventListener("click", {
        window.alert("Clicked!")
    })

    println(HelloWorldView.PAGE_STATE_STUFF.get().innerHTML)

    val pageState = Json.decodeFromString<PageState>(
        HelloWorldView.PAGE_STATE_STUFF.get().innerHTML
    )

    val pageState2 = Json.decodeFromString<List<TodoEntry>>(
        HelloWorldView.PAGE_STATE2_STUFF.get().innerHTML
    ).toMutableList()

    fun renderTodos() {
        HelloWorldView.ELEMENT_AREA_TEST.get().clear()

        HelloWorldView.ELEMENT_AREA_TEST.get().append {
            div {
                HelloWorldView(pageState2).renderTodos(this) { todo, button ->
                    button.onClickFunction = {
                        pageState2.remove(todo)
                        renderTodos()
                    }
                }
            }
        }
    }

    HelloWorldView.ADD_NEW_ELEMENT_TEST.get().addEventListener("click", {
        pageState2.add(
            TodoEntry(
                textArea.value
            )
        )

        renderTodos()
    })

    println(pageState)

    println(pageState.pageClazz)

    renderTodos()
}

fun ElementType.get(): Element {
    return document.getElementById(this.id)!!
}

fun <T> ElementType.get(): T {
    return document.getElementById(this.id) as T
}

fun InputElementType.get(): HTMLInputElement {
    return get<HTMLInputElement>()
} */