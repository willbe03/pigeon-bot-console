package org.gugugu.org.gugugu

import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.gugugu.KeywordAutoReply.*
import org.gugugu.cats.cats
import org.gugugu.config.Config
import org.gugugu.config.ConfigCommand
import org.gugugu.game.GameData
import java.io.File

object PigeonBotConsole : KotlinPlugin(
    JvmPluginDescription(
        id = "org.gugugu.pigeon-bot-console",
        name = "pigeonbot",
        version = "1.0-SNAPSHOT",
    )
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
        initFolders()
        // load configs
        KeywordData.reload()
        Config.reload()
        GameData.reload()
        // register commands
        KeywordAdd.register()
        KeyWordList.register()
        KeyWordDelete.register()
        ConfigCommand.register()
        // subscribe messages
        subscribeKeywordAutoReply()
        cats()
    }

    override fun onDisable() {
        CommandManager.INSTANCE.unregisterAllCommands(PigeonBotConsole)
    }

    fun initFolders(){
        fun createFolder (vararg imageFolder:File){
            for (folder in imageFolder) {
                if (!folder.exists())
                    if (!folder.mkdirs())
                        logger.error("创建图片文件夹失败。")
            }
        }
        val imageFolder = this.resolveDataFile("images/")
        val catFolder = this.resolveDataFile("images/cats")
        val quoteFolder = this.resolveDataFile("images/quotes")
        val replyFolder = this.resolveDataFile("images/replies")
        createFolder(imageFolder,catFolder,quoteFolder,replyFolder)
    }
}