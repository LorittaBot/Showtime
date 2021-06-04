package net.perfectdreams.showtime.backend.views

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import net.perfectdreams.showtime.backend.utils.SVGIconManager
import net.perfectdreams.showtime.backend.utils.WebsiteAssetsHashManager

abstract class BaseView(
    val iconManager: SVGIconManager,
    val hashManager: WebsiteAssetsHashManager,
    val locale: BaseLocale,
    val path: String
) {
    companion object {
        val versionPrefix = "/v3"
        val websiteUrl = "https://loritta.website"
    }

    fun generateHtml(): String {
        val supportUrl = "https://loritta.website/support"
        val firefoxUrl = "https://www.mozilla.org/firefox"
        val chromeUrl = "https://www.google.com/chrome"
        val edgeUrl = "https://www.microsoft.com/edge"

        // "br" to "pt-BR", "us" to "en", "es" to "es", "pt" to "pt"
        val pageLanguage = when (locale.id) {
            "default" -> "pt-BR"
            "en-us" -> "en"
            "es" -> "es"
            else -> "en"
        }

        return StringBuilder().appendHTML().html {
            attributes["lang"] = pageLanguage

            head {
                meta(charset = "utf-8")
                // Necessary for responsive design on phones: If not set, the browsers uses the "Use Desktop Design"
                meta(name = "viewport", content = "width=device-width, initial-scale=1, viewport-fit=cover")

                generateMeta()

                unsafe {
                    raw("""
<link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png" />
<link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png" />
<link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png" />
<link rel="manifest" href="/site.webmanifest" />
<link rel="mask-icon" href="/safari-pinned-tab.svg" color="#5bbad5" />
<meta name="msapplication-TileColor" content="#5bbad5" />
""")
                }

                styleLink("$versionPrefix/assets/css/style.css?hash=${assetHash("/assets/css/style.css")}")

                // ===[ SCRIPTS ]===
                // Usado para login: A SpicyMorenitta usa esse código ao autenticar via "auth_popup.kts"
                // Já que é meio difícil chamar códigos de Kotlin/JS na parent window, existe esse método auxiliar para facilitar.
                // authenticate(p) sendo p = "user identification do Discord"
                // Também tem umas coisinhas do Google reCAPTCHA
                // TODO: Fix
                /* script(type = ScriptType.textJavaScript) {
                    unsafe {
                        raw("""
function authenticate(p) { output.net.perfectdreams.spicymorenitta.utils.AuthUtils.handlePostAuth(p); };

document.domain = "loritta.website";

function onGoogleRecaptchaLoadCallback() { this['spicy-morenitta'].net.perfectdreams.spicymorenitta.utils.GoogleRecaptchaUtils.onRecaptchaLoadCallback(); };

window.addEventListener('load', function () {
    // Verificar se o usuário está usando o antigo Edge ou MSIE, já que nós não suportamos nenhum desses dois
    // ; MSIE == MS Internet Explorer
    // Trident/7.0 == MSIE11
    if (/(?:\b(MS)?IE\s+|\bTrident\/7\.0;.*\s+rv:|\bEdge\/)(\d+)/.test(navigator.userAgent)) {
        alert("${locale.getList("website.unsupportedBrowser", supportUrl, firefoxUrl, chromeUrl, edgeUrl).joinToString("\\n\\n")}")
    }
    // Verificar se o SpicyMorenitta foi carregado corretamente
    if (window.spicyMorenittaLoaded === undefined) {
        alert("${locale.getList("website.failedToLoadScripts", supportUrl, firefoxUrl, chromeUrl, edgeUrl).joinToString("\\n\\n")}")
    }
});
""")
                    }
                } */

                // TODO: Fix
                // Detect AdBlock
                // script(src = "$versionPrefix/adsbygoogle.js") {}

                // Google Analytics
                deferredScript("https://www.googletagmanager.com/gtag/js?id=UA-53518408-9")
                script(type = ScriptType.textJavaScript) {
                    unsafe {
                        raw("""window.dataLayer = window.dataLayer || []; function gtag(){dataLayer.push(arguments);} gtag('js', new Date()); gtag('config', 'UA-53518408-9');""")
                    }
                }

                // Google Analytics
                // Google AdSense
                // script(src = "https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js") {}

                // Google ReCAPTCHA
                // script(src = "https://www.google.com/recaptcha/api.js?render=explicit&onload=onGoogleRecaptchaLoadCallback") {}

                // NitroPay
                script(type = ScriptType.textJavaScript) {
                    unsafe {
                        raw("""
window["nitroAds"] = window["nitroAds"] || {
    createAd: function() {
        window.nitroAds.queue.push(["createAd", arguments]);
    },
    queue: []
};""".trimIndent())
                    }
                }
                deferredScript("https://s.nitropay.com/ads-595.js")

                // App itself
                deferredScript("/v3/assets/js/frontend.js?hash=${assetHash("/assets/js/frontend.js")}")

                for ((websiteLocaleId, localeName) in listOf("br" to "pt-BR", "us" to "en", "es" to "es", "pt" to "pt")) {
                    link {
                        attributes["rel"] = "alternate"
                        attributes["hreflang"] = localeName
                        attributes["href"] = "$websiteUrl/$websiteLocaleId/$path"
                    }
                }
            }
            generateBody()
        }.toString()
    }

    open fun getTitle(): String = "¯\\_(ツ)_/¯"
    open fun getFullTitle(): String = "${getTitle()} • Loritta"

    open fun HEAD.generateMeta() {
        // Used for Search Engines and in the browser itself
        title(getFullTitle())

        // Used for Search Engines
        meta("description", content = locale["website.genericDescription"])

        // Used for Discord Embeds
        meta("theme-color", "#00c1df")

        // Used in Twitter
        meta(name = "twitter:card", content = "summary")
        meta(name = "twitter:site", content = "@LorittaBot")
        meta(name = "twitter:creator", content = "@MrPowerGamerBR")

        // Used for Discord Embeds
        meta(content = locale["website.lorittaWebsite"]) { attributes["property"] = "og:site_name" }
        meta(content = locale["website.genericDescription"]) { attributes["property"] = "og:description" }
        meta(content = getTitle()) { attributes["property"] = "og:title" }
        meta(content = "600") { attributes["property"] = "og:ttl" }
        meta(content = "https://loritta.website/assets/img/loritta_gabizinha_v1.png") { attributes["property"] = "og:image" }
    }

    abstract fun HTML.generateBody()

    fun assetHash(asset: String) = hashManager.getAssetHash(asset)

    fun FlowOrInteractiveContent.customHtmlTag(htmlTag: String, block: HtmlBlockTag.() -> Unit = {}) {
        val obj = object: HTMLTag(htmlTag, consumer, emptyMap(),
            inlineTag = false,
            emptyTag = false), HtmlBlockTag {}
        obj.visit(block)
    }

    fun FlowOrInteractiveContent.sidebarWrapper(block: HtmlBlockTag.() -> Unit = {}) = customHtmlTag("lori-sidebar-wrapper", block)

    fun SectioningOrFlowContent.leftSidebar(block: ASIDE.() -> Unit = {}) = aside(classes = "left-sidebar") {
        id = "left-sidebar"
        attributes["data-preload-keep-scroll"] = "true"
        block.invoke(this)
    }

    fun SectioningOrFlowContent.rightSidebar(block: HtmlBlockTag.() -> Unit = {}) = section(classes = "right-sidebar") {
        block.invoke(this)
    }

    fun FlowOrPhrasingOrMetaDataContent.deferredScript(src: String) {
        // Defer = Downloads the file during HTML parsing and will only execute after the parser has completed
        // Defer also follows the script declaration order!
        // https://stackoverflow.com/questions/10808109/script-tag-async-defer
        script(src = src) { defer = true  }
    }
}