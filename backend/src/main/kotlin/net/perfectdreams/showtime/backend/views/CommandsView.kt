package net.perfectdreams.showtime.backend.views

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import kotlinx.html.*
import net.perfectdreams.dokyo.WebsiteTheme
import net.perfectdreams.loritta.api.commands.CommandCategory
import net.perfectdreams.loritta.api.commands.CommandInfo
import net.perfectdreams.showtime.backend.utils.NitroPayAdGenerator
import net.perfectdreams.showtime.backend.utils.NitroPayAdSize
import net.perfectdreams.showtime.backend.utils.SVGIconManager
import net.perfectdreams.showtime.backend.utils.SimpleImageInfo
import net.perfectdreams.showtime.backend.utils.WebsiteAssetsHashManager
import net.perfectdreams.showtime.backend.utils.commands.AdditionalCommandInfoConfig
import net.perfectdreams.showtime.backend.utils.generateNitroPayAd
import net.perfectdreams.showtime.backend.utils.locale.formatAsHtml
import net.perfectdreams.showtime.backend.utils.imgSrcSet
import java.awt.Color
import java.io.File
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class CommandsView(
        websiteTheme: WebsiteTheme,
        iconManager: SVGIconManager,
        hashManager: WebsiteAssetsHashManager,
        locale: BaseLocale,
        path: String,
        val commands: List<CommandInfo>,
        val filterByCategory: CommandCategory? = null,
        val additionalCommandInfos: List<AdditionalCommandInfoConfig>,
        val imageSizes: Map<String, Pair<Int, Int>>
) : SidebarsView(
        websiteTheme,
        iconManager,
        hashManager,
        locale,
        path
) {
    companion object {
        // We don't want to show commands in the "MAGIC" category
        private val PUBLIC_CATEGORIES = CommandCategory.values().filterNot { it == CommandCategory.MAGIC }
    }

    override val sidebarAdId = "commands"

    val publicCommands = commands.filter { it.category in PUBLIC_CATEGORIES }

    override fun getTitle() = locale["modules.sectionNames.commands"]

    override fun HtmlBlockTag.leftSidebarContents() {
        // Add search bar
        // Yes, you need to wrap it into a div. Setting "width: 100%;" in the input just causes
        // it to overflow for *some reason*.
        div(classes = "side-content") {
            div {
                style = "margin: 6px 10px;\n" +
                        "display: flex;\n" +
                        "align-items: center;\n" +
                        "justify-content: center;\n" +
                        "column-gap: 0.5em;"
                input(classes = "search-bar") {
                    style = "width: 100%;"
                }

                // And we also need to wrap this into a div to avoid the icon resizing automatically due to the column-gap
                div {
                    iconManager.search.apply(this)
                }
            }

            hr {}
        }

        // The first entry is "All"
        a(href = "/${locale.path}/commands", classes = "entry") {
            if (filterByCategory == null)
                classes = classes + "selected"

            // Yay workarounds!
            commandCategory {
                attributes["data-command-category"] = "ALL"

                div {
                    +"Todos"
                }

                div {
                    +publicCommands.size.toString()
                }
            }
        }

        for (category in PUBLIC_CATEGORIES.sortedByDescending { category -> commands.count { it.category == category } }) {
            val commandsInThisCategory = commands.count { it.category == category }

            // Register a redirect, the frontend will cancel this event if JS is enabled and filter the entries manually
            a(href = "/${locale.path}/commands/${category.name.toLowerCase()}", classes = "entry") {
                if (filterByCategory == category)
                    classes = classes + "selected"

                commandCategory {
                    attributes["data-command-category"] = category.name

                    div {
                        +category.getLocalizedName(locale)
                    }

                    div {
                        +"$commandsInThisCategory"
                    }
                }
            }
        }
    }

    override fun HtmlBlockTag.rightSidebarContents() {
        fun generateCategoryInfo(
                category: CommandCategory?,
                visible: Boolean,
                imageUrl: String
        ) {
            val categoryName = category?.name ?: "ALL"

            div(classes = "media") {
                style = "width: 100%;"
                attributes["data-category-info"] = categoryName

                if (!visible)
                    style += "display: none;"

                div(classes = "media-figure") {
                    style = "width: 250px;\n" +
                            "height: 250px;\n" +
                            "display: flex;\n" +
                            "align-items: center; justify-content: center;"

                    img(src = imageUrl) {
                        // Lazy load the images, because *for some reason* it loads all images even tho the div is display none.
                        attributes["loading"] = "lazy"
                        style = "max-height: 100%; max-width: 100%;"
                    }
                }

                div(classes = "media-body") {
                    if (category != null) {
                        for (entry in category.getLocalizedDescription(locale)) {
                            p {
                                +entry
                            }
                        }
                    } else {
                        for (entry in locale.getList("commands.category.all.description")) {
                            p {
                                formatAsHtml(
                                        entry,
                                        {
                                            if (it == 0) {
                                                code {
                                                    +"+"
                                                }
                                            }

                                            if (it == 1) {
                                                code {
                                                    +"+ping"
                                                }
                                            }
                                        },
                                        { +it }
                                )
                            }
                        }
                    }
                }
            }
        }

        fun generateCategoryInfo(
                category: CommandCategory?,
                visible: Boolean,
                imagePath: String,
                fileName: String,
                sizes: String,
                max: Int,
                min: Int,
                step: Int
        ) {
            val categoryName = category?.name ?: "ALL"

            div(classes = "media") {
                style = "width: 100%;"
                attributes["data-category-info"] = categoryName

                if (!visible)
                    style += "display: none;"

                div(classes = "media-figure") {
                    style = "width: 250px;\n" +
                            "height: 250px;\n" +
                            "display: flex;\n" +
                            "align-items: center; justify-content: center;"

                    imgSrcSet(imagePath, fileName, sizes, max, min, step) {
                        // Lazy load the images, because *for some reason* it loads all images even tho the div is display none.
                        attributes["loading"] = "lazy"
                        style = "max-height: 100%; max-width: 100%;"
                    }
                }

                div(classes = "media-body") {
                    if (category != null) {
                        for (entry in category.getLocalizedDescription(locale)) {
                            p {
                                +entry
                            }
                        }
                    } else {
                        p {
                            +"Todos os comandos"
                        }
                    }
                }
            }
        }

        generateCategoryInfo(
                null,
                filterByCategory == null,
                "/v3/assets/img/support/lori_support_268w.png"
        )

        generateCategoryInfo(
                CommandCategory.IMAGES,
                filterByCategory == CommandCategory.IMAGES,
                "/v3/assets/img/categories/images/",
                "images.png",
                "(max-width: 1366px) 250px",
                1486,
                186,
                100
        )

        generateCategoryInfo(
                CommandCategory.FUN,
                filterByCategory == CommandCategory.FUN,
                "/v3/assets/img/categories/fun/",
                "fun.png",
                "(max-width: 1366px) 250px",
                1213,
                113,
                100
        )

        generateCategoryInfo(
                CommandCategory.MODERATION,
                filterByCategory == CommandCategory.MODERATION,
                "/v3/assets/img/categories/moderation/",
                "moderation.png",
                "(max-width: 1366px) 250px",
                994,
                194,
                100
        )

        generateCategoryInfo(
                CommandCategory.SOCIAL,
                filterByCategory == CommandCategory.SOCIAL,
                "/v3/assets/img/categories/social/",
                "social.png",
                "(max-width: 1366px) 250px",
                2000,
                100,
                100
        )

        generateCategoryInfo(
                CommandCategory.DISCORD,
                filterByCategory == CommandCategory.DISCORD,
                "/v3/assets/img/categories/discord/",
                "discord.png",
                "(max-width: 1366px) 250px",
                1064,
                164,
                100
        )

        generateCategoryInfo(
                CommandCategory.UTILS,
                filterByCategory == CommandCategory.UTILS,
                "/v3/assets/img/categories/utilities/",
                "utilities.png",
                "(max-width: 1366px) 250px",
                1128,
                128,
                100
        )

        generateCategoryInfo(
                CommandCategory.MISC,
                filterByCategory == CommandCategory.MISC,
                "/v3/assets/img/categories/miscellaneous/",
                "miscellaneous.png",
                "(max-width: 1366px) 250px",
                1798,
                198,
                100
        )

        generateCategoryInfo(
                CommandCategory.ACTION,
                filterByCategory == CommandCategory.ACTION,
                "/v3/assets/img/categories/action/",
                "action.png",
                "(max-width: 1366px) 250px",
                1583,
                183,
                100
        )

        generateCategoryInfo(
                CommandCategory.UNDERTALE,
                filterByCategory == CommandCategory.UNDERTALE,
                "/v3/assets/img/categories/undertale/",
                "undertale.png",
                "(max-width: 1366px) 250px",
                1041,
                141,
                100
        )

        generateCategoryInfo(
                CommandCategory.POKEMON,
                filterByCategory == CommandCategory.POKEMON,
                "/v3/assets/img/categories/pokemon/",
                "pokemon.png",
                "(max-width: 1366px) 250px",
                960,
                160,
                100
        )

        generateCategoryInfo(
                CommandCategory.ECONOMY,
                filterByCategory == CommandCategory.ECONOMY,
                "/v3/assets/img/categories/economy/",
                "economy.png",
                "(max-width: 1366px) 250px",
                1811,
                111,
                100
        )

        generateCategoryInfo(
                CommandCategory.FORTNITE,
                filterByCategory == CommandCategory.FORTNITE,
                "/v3/assets/img/categories/fortnite/",
                "fortnite.png",
                "(max-width: 1366px) 250px",
                3479,
                179,
                100
        )

        generateCategoryInfo(
                CommandCategory.VIDEOS,
                filterByCategory == CommandCategory.VIDEOS,
                "/v3/assets/img/categories/videos/",
                "videos.png",
                "(max-width: 1366px) 250px",
                1541,
                141,
                100
        )

        generateCategoryInfo(
                CommandCategory.ANIME,
                filterByCategory == CommandCategory.ANIME,
                "/v3/assets/img/categories/anime/",
                "anime.png",
                "(max-width: 1366px) 250px",
                1102,
                102,
                100
        )

        generateCategoryInfo(
                CommandCategory.MINECRAFT,
                filterByCategory == CommandCategory.MINECRAFT,
                "/v3/assets/img/categories/minecraft/",
                "minecraft.png",
                "(max-width: 1366px) 250px",
                505,
                105,
                100
        )

        generateCategoryInfo(
                CommandCategory.ROBLOX,
                filterByCategory == CommandCategory.ROBLOX,
                "/v3/assets/img/categories/roblox/",
                "roblox.png",
                "(max-width: 1366px) 250px",
                3805,
                105,
                100
        )

        // Generate ads below the <hr> tag
        // Desktop
        fieldSet {
            legend {
                style = "margin-left: auto;"

                iconManager.ad.apply(this)
            }

            val zoneId = ZoneId.of("America/Sao_Paulo")
            val now = LocalDate.now(zoneId)

            // Discords.com
            // TODO: Proper sponsorship impl
            if (now.isBefore(LocalDate.of(2021, 9, 10))) {
                // A kinda weird workaround, but it works
                unsafe {
                    raw("""<a href="/sponsor/discords" target="_blank" class="sponsor-wrapper">
<div class="sponsor-pc-image"><img src="https://loritta.website/assets/img/sponsors/discords_pc.png?v2" class="sponsor-banner"></div>
<div class="sponsor-mobile-image"><img src="https://loritta.website/assets/img/sponsors/discords_mobile.png" class="sponsor-banner"></div>
</a>""")
                }
            } else {
                // Desktop Large
                generateNitroPayAd(
                    "commands-desktop-large",
                    listOf(
                        NitroPayAdSize(
                            728,
                            90
                        ),
                        NitroPayAdSize(
                            970,
                            90
                        ),
                        NitroPayAdSize(
                            970,
                            250
                        )
                    ),
                    mediaQuery = NitroPayAdGenerator.DESKTOP_LARGE_AD_MEDIA_QUERY
                )

                generateNitroPayAd(
                    "commands-desktop",
                    listOf(
                        NitroPayAdSize(
                            728,
                            90
                        )
                    ),
                    mediaQuery = NitroPayAdGenerator.RIGHT_SIDEBAR_DESKTOP_MEDIA_QUERY
                )

                // We don't do tablet here because there isn't any sizes that would fit a tablet comfortably
                generateNitroPayAd(
                    "commands-phone",
                    listOf(
                        NitroPayAdSize(
                            300,
                            250
                        ),
                        NitroPayAdSize(
                            320,
                            50
                        )
                    ),
                    mediaQuery = NitroPayAdGenerator.RIGHT_SIDEBAR_PHONE_MEDIA_QUERY
                )
            }
        }

        hr {}

        // First we are going to sort by category count
        // We change the first compare by to negative because we want it in a descending order (most commands in category -> less commands)
        for (command in publicCommands.sortedWith(compareBy({
            -(commands.groupBy { it.category }[it.category]?.size ?: 0)
        }, CommandInfo::category, CommandInfo::label))) {
            val commandDescriptionKey = command.description
            val commandExamplesKey = command.examples
            val commandLabel = command.label

            // Additional command info (like images)
            val additionalInfo = additionalCommandInfos.firstOrNull { it.name == command.name }

            val color = getCategoryColor(command.category)

            commandEntry {
                attributes["data-command-name"] = command.name
                attributes["data-command-category"] = command.category.name

                style = if (filterByCategory == null || filterByCategory == command.category)
                    "display: block;"
                else
                    "display: none;"

                details(classes = "fancy-details") {
                    style = "line-height: 1.2; position: relative;"

                    summary {
                        commandCategoryTag {
                            style = "background-color: rgb(${color.red}, ${color.green}, ${color.blue});"
                            +(command.category.getLocalizedName(locale))
                        }

                        div {
                            style = "display: flex;align-items: center;"

                            div {
                                style = "flex-grow: 1; align-items: center;"
                                div {
                                    style = "display: flex;"

                                    commandLabel {
                                        style =
                                                "font-size: 1.5em; font-weight: bold; box-shadow: inset 0 0px 0 white, inset 0 -1px 0 rgb(${color.red}, ${color.green}, ${color.blue});"
                                        +commandLabel
                                    }
                                }

                                commandDescription {
                                    style = "word-wrap: anywhere;"

                                    if (commandDescriptionKey != null) {
                                        +locale.get(commandDescriptionKey)
                                    } else {
                                        +"???"
                                    }
                                }
                            }

                            div(classes = "chevron-icon") {
                                style = "font-size: 1.5em"
                                iconManager.chevronDown.apply(this)
                            }
                        }
                    }

                    div(classes = "details-content") {
                        style = "line-height: 1.4;"

                        if (additionalInfo != null) {
                            // Add additional images, if present
                            if (additionalInfo.imageUrls != null && additionalInfo.imageUrls.isNotEmpty()) {
                                for (imageUrl in additionalInfo.imageUrls) {
                                    img(src = imageUrl, classes = "thumbnail") {
                                        // So, this is hard...
                                        // Because we are "floating" the image, content jumping is inevitable... (because the height is set to 0)
                                        // So we need to set a fixed width AND height, oof!
                                        // It is not impossible, but we need to load all the image widthxheight and calculate the fixed size manually...
                                        // that sucks...
                                        val size = imageSizes[imageUrl] ?: throw RuntimeException("Missing image size for $imageUrl")

                                        width = "250"

                                        // Lazy load the images, because *for some reason* it loads all images even tho the details tag is closed.
                                        attributes["loading"] = "lazy"

                                        // The aspect-ratio is used to avoid content reflow
                                        style = "aspect-ratio: ${size.first} / ${size.second};"
                                    }
                                }
                            }

                            // Add additional videos, if present
                            if (additionalInfo.videoUrls != null && additionalInfo.videoUrls.isNotEmpty()) {
                                for (videoUrl in additionalInfo.videoUrls) {
                                    video(classes = "thumbnail") {
                                        // For videos, we need to use the "preload" attribute to force the video to *not* preload
                                        // The video will only start loading after the user clicks the video
                                        attributes["preload"] = "none"

                                        // The aspect-ratio is used to avoid content reflow
                                        style = "aspect-ratio: 16 / 9;"

                                        width = "250"
                                        controls = true

                                        source {
                                            src = videoUrl
                                            type = "video/mp4"
                                        }
                                    }
                                }
                            }
                        }

                        if (command.aliases.isNotEmpty()) {
                            div {
                                b {
                                    style = "color: rgb(${color.red}, ${color.green}, ${color.blue});"
                                    +"Sinônimos: "
                                }

                                for ((index, a) in command.aliases.withIndex()) {
                                    if (index != 0) {
                                        +", "
                                    }

                                    code {
                                        +a
                                    }
                                }
                            }

                            hr {}
                        }

                        if (commandExamplesKey != null) {
                            div {
                                b {
                                    style = "color: rgb(${color.red}, ${color.green}, ${color.blue});"
                                    +"Exemplos: "
                                }

                                val examples = locale.getList(commandExamplesKey)

                                for (example in examples) {
                                    val split = example.split("|-|")
                                            .map { it.trim() }

                                    div {
                                        style = "padding-bottom: 8px;"
                                        if (split.size == 2) {
                                            // If the command has a extended description
                                            // "12 |-| Gira um dado de 12 lados"
                                            // A extended description can also contain "nothing", but contains a extended description
                                            // "|-| Gira um dado de 6 lados"
                                            val (commandExample, explanation) = split

                                            div {
                                                span {
                                                    style = "color: rgb(${color.red}, ${color.green}, ${color.blue});"

                                                    iconManager.smallDiamond.apply(this)
                                                }

                                                +" "

                                                b {
                                                    +explanation
                                                }
                                            }

                                            div {
                                                code {
                                                    +commandLabel

                                                    +" "

                                                    +commandExample
                                                }
                                            }
                                            // examples.add("\uD83D\uDD39 **$explanation**")
                                            // examples.add("`" + commandLabelWithPrefix + "`" + (if (commandExample.isEmpty()) "" else "**` $commandExample`**"))
                                        } else {
                                            val commandExample = split[0]

                                            div {
                                                +commandLabel

                                                +" "

                                                +commandExample
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getCategoryColor(category: CommandCategory) = when (category) {
        // Photoshop Logo Color
        CommandCategory.IMAGES -> Color(49, 197, 240)
        CommandCategory.FUN -> Color(254, 120, 76)
        CommandCategory.ECONOMY -> Color(47, 182, 92)
        // Discord Blurple
        CommandCategory.DISCORD -> Color(114, 137, 218)
        // Discord "Ban User" background
        CommandCategory.MODERATION -> Color(240, 71, 71)
        // Roblox Logo Color
        CommandCategory.ROBLOX -> Color(226, 35, 26)
        CommandCategory.ACTION -> Color(243, 118, 166)
        CommandCategory.UTILS -> Color(113, 147, 188)
        // Grass Block
        CommandCategory.MINECRAFT -> Color(124, 87, 58)
        // Pokémon (Pikachu)
        CommandCategory.POKEMON -> Color(244, 172, 0)
        // Undertale
        CommandCategory.UNDERTALE -> Color.BLACK
        // Vídeos
        CommandCategory.VIDEOS -> Color(163, 108, 253)
        // Social
        CommandCategory.SOCIAL -> Color(235, 0, 255)
        CommandCategory.MISC -> Color(121, 63, 166)
        CommandCategory.ANIME -> Color(132, 224, 212)
        else -> Color(0, 193, 223)
    }

    class COMMANDCATEGORY(consumer: TagConsumer<*>) :
            HTMLTag(
                    "lori-command-category", consumer, emptyMap(),
                    inlineTag = false,
                    emptyTag = false
            ), HtmlBlockTag

    fun FlowOrInteractiveContent.commandCategory(block: COMMANDCATEGORY.() -> Unit = {}) {
        COMMANDCATEGORY(consumer).visit(block)
    }

    class COMMANDENTRY(consumer: TagConsumer<*>) :
            HTMLTag(
                    "lori-command-entry", consumer, emptyMap(),
                    inlineTag = false,
                    emptyTag = false
            ), HtmlBlockTag

    fun FlowOrInteractiveContent.commandEntry(block: COMMANDENTRY.() -> Unit = {}) {
        COMMANDENTRY(consumer).visit(block)
    }

    fun FlowOrInteractiveContent.commandCategoryTag(block: HtmlBlockTag.() -> Unit = {}) =
            customHtmlTag("lori-command-category-tag", block)

    fun FlowOrInteractiveContent.commandLabel(block: HtmlBlockTag.() -> Unit = {}) =
            customHtmlTag("lori-command-label", block)

    fun FlowOrInteractiveContent.commandDescription(block: HtmlBlockTag.() -> Unit = {}) =
            customHtmlTag("lori-command-description", block)
}