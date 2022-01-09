package org.gugugu.KeywordAutoReply

import net.mamoe.mirai.console.terminal.consoleLogger
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.ExternalResource.Companion.sendAsImageTo
import org.gugugu.config.Config
import org.gugugu.org.gugugu.PigeonBotConsole
import kotlin.random.Random

fun subscribeKeywordAutoReply() {
    val autoReplyMap = KeywordData.replyData
    GlobalEventChannel.subscribeGroupMessages {
        always {
            if (Random.nextDouble(0.0, 100.0) <= Config.replyP) {
                for ((key, value) in autoReplyMap) {
                    if (message.content.contains(key) && !message.content.startsWith("1")) {
                        val reply = value.random()
                        if (reply.startsWith("$")) {
                            // find image file with same name of reply in reply directory
                            consoleLogger.info(reply)
                            consoleLogger.info(PigeonBotConsole.resolveDataFile("images/replies").list().contentToString())
                            PigeonBotConsole.resolveDataFile("images/replies").listFiles { _, name -> name == "$reply.gif" }?.get(0)?.sendAsImageTo(subject)
                        } else
                            group.sendMessage(reply)
                        break
                    }
                }
            }
        }
    }
}