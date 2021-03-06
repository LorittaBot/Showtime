package net.perfectdreams.showtime.backend.views.home

import com.mrpowergamerbr.loritta.utils.locale.BaseLocale
import kotlinx.html.*
import net.perfectdreams.showtime.backend.views.BaseView
import net.perfectdreams.showtime.backend.utils.imgSrcSet

fun DIV.funnyCommands(locale: BaseLocale, websiteUrl: String, sectionClassName: String) {
    div(classes = "$sectionClassName wobbly-bg") {
        id = "fun-section"

        // generateNitroPayAdOrSponsor(2, "home-funny-commands1", "Loritta v2 Funny Commands") { true }
        // generateNitroPayAdOrSponsor(3, "home-funny-commands2", "Loritta v2 Funny Commands") { it != NitroPayAdDisplay.PHONE }

        /* div(classes = "funny-commands") {
        div {
            div(classes = "marquee") {
                div(classes = "scroller") {
                    headerCommands()
                }
            }

            div(classes = "marquee marquee2") {
                div(classes = "scroller") {
                    headerCommands()
                }
            }
        }
        } */

        div(classes = "media") {
            div(classes = "media-figure") {
                imgSrcSet(
                        "${BaseView.versionPrefix}/assets/img/home/",
                        "lori_commands.png",
                        "(max-width: 800px) 50vw, 15vw",
                        791,
                        191,
                        100
                )
            }
            div(classes = "media-body") {
                div {
                    style = "text-align: left;"

                    div {
                        style = "text-align: center;"
                        h1 {
                            + locale["website.home.funnyCommands.title"]
                        }
                    }

                    for (str in locale.getList("website.home.funnyCommands.description")) {
                        p {
                            + str
                        }
                    }
                }
            }
        }
    }
}


fun DIV.funnyCommandsBrasil(locale: BaseLocale, websiteUrl: String) {
    div(classes = "odd-wrapper wobbly-bg") {
        id = "fun-section"

        // generateNitroPayAdOrSponsor(2, "home-funny-commands1-brazil1", "Loritta v2 Funny Commands") { true }
        // generateNitroPayAdOrSponsor(3, "home-funny-commands2-brazil2", "Loritta v2 Funny Commands") { it != NitroPayAdDisplay.PHONE }

        /* div(classes = "funny-commands") {
        div {
            div(classes = "marquee") {
                div(classes = "scroller") {
                    headerCommands()
                }
            }

            div(classes = "marquee marquee2") {
                div(classes = "scroller") {
                    headerCommands()
                }
            }
        }
        } */

        div(classes = "media") {
            div(classes = "media-figure") {
                imgSrcSet(
                        "${BaseView.versionPrefix}/assets/img/home/",
                        "lori_commands.png",
                        "(max-width: 800px) 50vw, 15vw",
                        791,
                        191,
                        100
                )
            }
            div(classes = "media-body") {
                div {
                    style = "text-align: left;"


                    div {
                        style = "text-align: center;"
                        h1 {
                            + "Memes Brasileiros em um Bot Brasileiro"
                        }
                    }

                    p {
                        + "N??o tem gra??a usar bots de entreterimento gringos se voc?? n??o entende nada dos memes que eles fazem, seus membros n??o entendem nada e voc?? s?? solta aquela risadinha de \"eu n??o entendi mas ok\"."
                    }
                    p {
                        + "Por isto eu possui v??rios comandos diferentes e engra??ados para voc?? se divertir e ter gargalhadas com eles! Fa??a seus pr??prios memes comigo, sem voc?? precisar do conforto do seu servidor no Discord!"
                    }
                    ul {
                        li {
                            + "Fa??a montagens com o Bolsonaro com "
                            code {
                                + "+bolsonaro"
                            }
                        }
                        li {
                            + "Destrua seus piores inimigos no cepo de madeira com o "
                            code {
                                + "+cepo"
                            }
                        }
                        li {
                            + "Imagine como voc?? aparecia no Treta News com o "
                            code {
                                + "+tretanews"
                            }
                        }
                        li {
                            + "T?? pegando fogo bicho! Invoque o Faust??o no seu servidor com "
                            code {
                                + "+faust??o"
                            }
                        }
                        li {
                            + "?? p?? v?? ou p?? cume? Piadas de Tioz??o no "
                            code {
                                + "+tiodopave"
                            }
                        }
                        li {
                            + "O SAM ?? brabo? Coloque a marca da ??gua da South America Memes em seus memes de qualidade duvidosa com "
                            code {
                                + "+sam"
                            }
                            + " e pegue memes tamb??m de qualidade duvidosa com "
                            code {
                                + "+randomsam"
                            }
                        }
                        li {
                            + "ata com "
                            code {
                                + "+ata"
                            }
                        }
                        li {
                            + "E muito mais! Veja todos na minha "
                            a(href = "$websiteUrl/commands") {
                                + "lista de comandos"
                            }
                            + "."
                        }
                    }


                    div {
                        style = "text-align: center;"
                        img(src = "$websiteUrl${BaseView.versionPrefix}/assets/img/bolsonaro_tv_add_lori.png") {}
                    }

                }
            }
        }
    }
}