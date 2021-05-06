package org.gugugu.KeywordAutoReply

import net.mamoe.mirai.console.command.BuiltInCommands.AutoLoginCommand.add
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.data.PluginDataStorage
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content
import org.gugugu.org.gugugu.PigeonBotConsole

object KeywordAdd : SimpleCommand(PigeonBotConsole,"add",description = "增加关键字") {
    @Handler
    suspend fun CommandSender.handle(key:String, value:Message){
        if (KeywordData.replyData.containsKey(key))
            KeywordData.replyData[key]?.add(value.content)
        else
            KeywordData.replyData[key] = mutableSetOf(value.content)
        sendMessage("添加\"${value}\"到\"${key}\"")
    }
}

object KeyWordList: SimpleCommand(PigeonBotConsole,"list",description = "列出关键字"){
    @Handler
    suspend fun CommandSender.list(key: String){
        try {
            sendMessage(KeywordData.replyData[key].toString())
        }catch (e:Exception){
            sendMessage("未找到关键字")
        }
    }
    @Handler
    suspend fun CommandSender.list(){
        sendMessage(KeywordData.replyData.toString())
    }
}