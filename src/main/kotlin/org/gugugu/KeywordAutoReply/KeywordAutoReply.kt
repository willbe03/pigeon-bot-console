package org.gugugu.KeywordAutoReply

import net.mamoe.mirai.console.command.BuiltInCommands.AutoLoginCommand.add
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.data.PluginDataStorage
import net.mamoe.mirai.console.terminal.consoleLogger
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import org.gugugu.org.gugugu.PigeonBotConsole

object KeywordAdd : RawCommand(PigeonBotConsole,"add","a",description = "增加关键字") {
//    @Handler
    override suspend fun CommandSender.onCommand(args: MessageChain){
        if (args.size!=2) {
            sendMessage("参数错误")
            consoleLogger.info(args.content)
            return
        }
        if (!args.contains(Image)) {
            val key = args[0].content
            val value = args[1].content
            if (KeywordData.replyData.containsKey(key))
                KeywordData.replyData[key]?.add(value)
            else
                KeywordData.replyData[key] = mutableSetOf(value)
            sendMessage("添加\"${value}\"到\"${key}\"")
        }
        else {
            val key = args[0].content
            val value = args[1]
            sendMessage("IMG"+key)
            sendMessage((value as Image).queryUrl())
        }
    }
//    @Handler
//    suspend fun CommandSender.handle(key: String, img: Image){
//        sendMessage("IMG"+key)
//        sendMessage(img)
//    }
}

//object KeywordAddRaw:RawCommand(PigeonBotConsole,"add", description = "增加关键字")

object KeyWordList: SimpleCommand(PigeonBotConsole,"list","ls",description = "列出关键字"){
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
        sendMessage(KeywordData.replyData.keys.toString())
    }
}