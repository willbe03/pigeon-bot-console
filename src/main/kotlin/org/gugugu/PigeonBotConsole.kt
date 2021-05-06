package org.gugugu.org.gugugu

import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.gugugu.KeywordAutoReply.KeyWordList
import org.gugugu.KeywordAutoReply.KeywordAdd
import org.gugugu.KeywordAutoReply.KeywordData
import org.gugugu.KeywordAutoReply.subscribeKeywordAutoReply
import org.gugugu.config.Config
import org.gugugu.config.ConfigCommand

object PigeonBotConsole : KotlinPlugin(
    JvmPluginDescription(
        id = "org.gugugu.pigeon-bot-console",
        name = "pigeonbot",
        version = "1.0-SNAPSHOT",
    )
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
        // load configs
        KeywordData.reload()
        Config.reload()
        // register commands
        KeywordAdd.register()
        KeyWordList.register()
        ConfigCommand.register()
        // subscribe messages
        subscribeKeywordAutoReply()
    }

    override fun onDisable() {
        CommandManager.INSTANCE.unregisterAllCommands(PigeonBotConsole)
    }
}