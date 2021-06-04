package net.perfectdreams.showtime.backend.views.home

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import kotlinx.html.*

fun DIV.customization(locale: BaseLocale, sectionClassName: String) {
    div(classes = "$sectionClassName wobbly-bg") {
        style = "text-align: center;"

        div(classes = "media") {
            div(classes = "media-body") {
                div {
                    style = "text-align: left;"

                    div {
                        style = "text-align: center;"
                        h1 {
                            + locale["website.home.customization.title"]
                        }
                    }

                    for (str in locale.getList("website.home.customization.description")) {
                        p {
                            + str
                        }
                    }
                }
            }

            div(classes = "media-figure") {
                img(src = "https://cdn.discordapp.com/attachments/510601125221761054/567911316094713856/customizacao.png") {
                    attributes["loading"] = "lazy"
                }
            }
        }
    }
}