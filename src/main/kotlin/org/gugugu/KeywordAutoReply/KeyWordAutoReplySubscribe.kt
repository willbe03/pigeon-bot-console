package org.gugugu.KeywordAutoReply

import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.ExternalResource.Companion.sendAsImageTo
import java.io.File

fun subscribeKeywordAutoReply(){
    val autoReplyMap = KeywordData.replyData
    GlobalEventChannel.subscribeGroupMessages{
        always {
            for ((key,value) in autoReplyMap){
                if (message.content.contains(key) && !message.content.startsWith("1")) {
                    val reply = value.random()
                    if (reply.startsWith("$"))
//                        File("src/img/autoreply/$reply.jpg").sendAsImageTo(subject)
                    else
                        group.sendMessage(reply)
                    break
                }
            }
        }
    }
}