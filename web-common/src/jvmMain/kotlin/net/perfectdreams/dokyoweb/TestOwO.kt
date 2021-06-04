package net.perfectdreams.dokyoweb

import kotlinx.html.*
import kotlinx.html.stream.appendHTML

fun main() {
    var element: H1? = null

    val html = StringBuilder().appendHTML().html {
        body {
            h1 {
                wrapInteractivity("stuff-123", this)

                + "hello test"
            }
        }
    }

    println(html)

    println(element)
}

fun wrapInteractivity(id: String, tag: CommonAttributeGroupFacade) {
    tag.attributes["test-attribute"] = id
}