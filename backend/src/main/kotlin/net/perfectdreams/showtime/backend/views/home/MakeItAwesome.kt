package net.perfectdreams.showtime.backend.views.home

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import kotlinx.html.*

fun DIV.makeItAwesome(locale: BaseLocale, sectionClassName: String, viewMoreFeatures: Boolean = false) {
    div(classes = "$sectionClassName wobbly-bg") {
        style = "text-align: center; padding-bottom: 64px;"

        h1 {
            + locale["website.home.makeItAwesome.title"]
        }

        // TODO: Fix
        /* a(classes = "add-me button pink shadow big", href = com.mrpowergamerbr.loritta.LorittaLauncher.loritta.discordInstanceConfig.discord.addBotUrl) {
            style = "font-size: 1.5em;"

            i(classes = "fas fa-plus") {}

            + " ${locale["website.jumbotron.addMe"]}"
        } */

        if (viewMoreFeatures) {
            a(classes = "add-me button light-green shadow big", href = "/${locale.path}/") {
                style = "font-size: 1.5em;"

                i(classes = "fas fa-star") {}

                attributes["data-enable-link-preload"] = "true"
                + " ${locale["website.jumbotron.viewMoreFeatures"]}"
            }
        }
    }
}