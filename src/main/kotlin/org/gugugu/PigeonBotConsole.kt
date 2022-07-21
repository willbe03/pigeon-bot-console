package org.gugugu

import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.gugugu.key_word_auto_reply.*
import org.gugugu.cats.cats
import org.gugugu.config.Config
import org.gugugu.config.ConfigCommand
import org.gugugu.game.GameCommand
import org.gugugu.game.GameData
import org.gugugu.quote.AddQuoteCommand
import org.gugugu.quote.QueryQuoteCommand
import org.gugugu.quote.QuoteData
import org.gugugu.quote.RandomQuoteCommand
import org.gugugu.saucenao.SauceNaoCommand
import java.io.File

object PigeonBotConsole : KotlinPlugin(
    JvmPluginDescription(
        id = "org.gugugu.pigeon-bot-console",
        name = "pigeonbot",
        version = "1.0-SNAPSHOT",
    )
) {
    override fun onEnable() {
        logger.info { "PigeonBot loaded" }
        initFolders()
        // load configs
        KeywordData.reload()
        Config.reload()
        GameData.reload()
        QuoteData.reload()
        // register commands
        KeywordAdd.register()
        KeyWordList.register()
        KeyWordDelete.register()
        ConfigCommand.register()
        GameCommand.register()
        SauceNaoCommand.register()

        RandomQuoteCommand.register()
        AddQuoteCommand.register()
        QueryQuoteCommand.register()
        // subscribe messages
//        sauce()
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