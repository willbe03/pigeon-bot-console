package org.gugugu.KeywordAutoReply

import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.terminal.consoleLogger
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.utils.MiraiInternalApi
import org.gugugu.org.gugugu.PigeonBotConsole
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
            consoleLogger.info(args.content)
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
            consoleLogger.info("added image md5: " + value.md5.toHexString())
//            sendMessage("IMG" + key)
//            sendMessage((value as Image).queryUrl())
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

object KeyWordDelete : SimpleCommand(PigeonBotConsole, "del", "rm", description = "删除关键字") {
    @Handler
    suspend fun CommandSender.del(key: String, value: String) {
        try {
            KeywordData.replyData[key]!!.remove(value)
            sendMessage("删除成功")
        } catch (e: Exception) {
            sendMessage("未找到关键字或者回复词，删除失败")
        }
    }
}