package net.perfectdreams.showtime.backend.views.home

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import kotlinx.html.*
import net.perfectdreams.showtime.backend.views.BaseView
import net.perfectdreams.showtime.backend.utils.imgSrcSet

fun DIV.community(locale: BaseLocale, sectionClassName: String) {
    div(classes = "$sectionClassName wobbly-bg") {
        style = "text-align: center;"

        // generateNitroPayAdOrSponsor(0, "home-community", "Loritta v2 Community") { true }

        // generateNitroPayAd("home-community", "Loritta v2 Community")
        // generateAd("8109140955", "Loritta v2 Community", true)
        // generateAd("8109140955", "Loritta v2 Community", false)

        div(classes = "media") {
            div(classes = "media-figure") {
                imgSrcSet(
                        "${BaseView.versionPrefix}/assets/img/home/",
                        "lori_community.png",
                        "(max-width: 800px) 50vw, 15vw",
                        768,
                        168,
                        100
                )
            }

            div(classes = "media-body") {
                div {
                    style = "text-align: left;"

                    div {
                        style = "text-align: center;"
                        h1 {
                            + locale["website.home.community.title"]
                        }
                    }

                    for (str in locale.getList("website.home.community.description")) {
                        p {
                            + str
                        }
                    }
                }
            }
        }
    }
}