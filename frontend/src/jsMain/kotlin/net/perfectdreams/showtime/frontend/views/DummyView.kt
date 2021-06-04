package net.perfectdreams.showtime.frontend.views

import kotlinx.coroutines.delay

class DummyView : DokyoView() {
    override suspend fun onPreLoad() {
        println("onPreLoad()")
        delay(7_000)
    }

    override suspend fun onLoad() {
        println("onLoad()")
    }

    override suspend fun onUnload() {
        println("onUnload()")
    }
}