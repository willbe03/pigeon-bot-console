package org.gugugu.game

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import java.lang.reflect.Member

object GameData: AutoSavePluginData("game") {
    val games : Map<String, List<Member>> by value()
}