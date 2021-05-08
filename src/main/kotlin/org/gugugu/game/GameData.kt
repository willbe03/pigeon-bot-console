package org.gugugu.game

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.contact.User
import kotlinx.serialization.Serializable

object GameData : AutoSavePluginData("game") {
    val games: MutableList<Game> by value(mutableListOf())
}

@Serializable
data class Game(val name: String, val gamers: MutableList<Long>, val startTime: Long) {
    private var endTime: Long = -1L

    constructor(name: String, gamers: MutableList<Long>, startTime: Long, endTime: Long) : this(
        name,
        gamers,
        startTime
    ) {
        this.endTime = endTime
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Game) {
            other.name == this.name
        } else {
            false
        }
    }

    override fun toString(): String {
        return name
    }
}