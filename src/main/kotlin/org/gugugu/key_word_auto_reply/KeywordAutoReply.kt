package org.gugugu.key_word_auto_reply

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import org.gugugu.PigeonBotConsole
import org.gugugu.game.GameCommand.remove
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * Download image from url to path
 */
fun downloadImage(url: String, path: String) {
    val img = File(path)
    if (!img.exists()) img.createNewFile()
    val input = URL(url).openStream()
    val output = FileOutputStream(img)
    input.copyTo(output)
    input.close()
    output.close()
}

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

object KeywordAdd : RawCommand(PigeonBotConsole, "add", "a", description = "增加关键字") {
//    @OptIn(MiraiInternalApi::class)
    override suspend fun CommandSender.onCommand(args: MessageChain) {
        if (args.size != 2) {
            sendMessage("参数错误")
            return
        }
        if (!args.contains(Image)) { // 不带图像
            val key = args[0].content
            val value = args[1].content
            if (KeywordData.replyData.containsKey(key))
                KeywordData.replyData[key]?.add(value)
            else
                KeywordData.replyData[key] = mutableSetOf(value)
            sendMessage("添加\"${value}\"到\"${key}\"")
        } else { //带图像
            val key = args[0].content
            val value = args[1] as Image
            //download image
            val url = value.queryUrl()
            val replyFolderPath = PigeonBotConsole.resolveDataFile("images/replies").absolutePath
            downloadImage(url, replyFolderPath + "/$" + value.md5.toHexString() + ".gif")
            if (KeywordData.replyData.containsKey(key))
                KeywordData.replyData[key]?.add("$" + value.md5.toHexString())
            else
                KeywordData.replyData[key] = mutableSetOf("$" + value.md5.toHexString())
            sendMessage("添加\"${value}\"到\"${key}\"")
        }
    }

}

object KeyWordList : SimpleCommand(PigeonBotConsole, "list", "ls", description = "列出关键字") {
    @Handler
    suspend fun CommandSender.list(key: String) {
        try {
            sendMessage(KeywordData.replyData[key].toString())
        } catch (e: Exception) {
            sendMessage("未找到关键字")
        }
    }

    @Handler
    suspend fun CommandSender.list() {
        sendMessage(KeywordData.replyData.keys.toString())
    }
}

object KeyWordDelete : RawCommand(PigeonBotConsole, "del", "rm", description = "删除关键字") {
    override suspend fun CommandSender.onCommand(args: MessageChain) {
        if (args.size != 2) {
            sendMessage("参数错误")
            return
        }
        val key = args[0].content
        val value = args[1]
        if (!args.contains(Image)) { // 不带图像
            // 如果有key和value就delete
            if (KeywordData.replyData.containsKey(key)
                && KeywordData.replyData[key]!!.contains(value.content)){
                // remove value
                KeywordData.replyData[key]!!.remove(value.content)
                if(KeywordData.replyData[key]!!.isEmpty()){
                    // 如果没有value就remove key
                    KeywordData.replyData.remove(key)
                }
                sendMessage("已删除关联词")
            }else{
                sendMessage("未找到关键词")
            }
        }else{// 带图像
            val img = args[1] as Image
            val md5String = "$" + img.md5.toHexString()
            if (KeywordData.replyData.containsKey(key)
                && KeywordData.replyData[key]!!.contains(md5String)){
                // remove value
                KeywordData.replyData[key]!!.remove(md5String)
                // delete file
                val replyFolderPath = PigeonBotConsole.resolveDataFile("images/replies").absolutePath
                val imgFile = File("$replyFolderPath/$md5String.gif")
                imgFile.delete()
                if(KeywordData.replyData[key]!!.isEmpty()){
                    // 如果没有value就remove key
                    KeywordData.replyData.remove(key)
                }
                sendMessage("已删除关联词")
            }else{
                sendMessage("未找到关键词")
            }
        }

    }
}