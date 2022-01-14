package org.gugugu

import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader

fun main(){
    MiraiConsoleTerminalLoader.startAsDaemon()
    PigeonBotConsole.load()
    PigeonBotConsole.enable()
}