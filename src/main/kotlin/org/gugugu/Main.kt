package org.gugugu

import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import org.gugugu.org.gugugu.PigeonBotConsole

@OptIn(ConsoleExperimentalApi::class)
fun main(){
    MiraiConsoleTerminalLoader.startAsDaemon()
    PigeonBotConsole.load()
    PigeonBotConsole.enable()
}