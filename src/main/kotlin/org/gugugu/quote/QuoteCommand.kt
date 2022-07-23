package org.gugugu.quote

import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import org.gugugu.PigeonBotConsole
import org.gugugu.key_word_auto_reply.downloadImage
import org.gugugu.key_word_auto_reply.toHexString
import java.io.File

object AddQuoteCommand : RawCommand(
    PigeonBotConsole, "上传语录", description = "上传语录", usage = """
    回复别人的消息并附上截图
    #上传语录 [图片]
""".trimIndent()
) {
    override suspend fun CommandContext.onCommand(args: MessageChain) {
        if (!args.contains(Image)) {
            this.sender.sendMessage("上传失败：未找到语录图片")
            return
        }
        val quote: String
        val image = args[Image]!!
        val md5 = "$" + image.md5.toHexString()
        val memberQQ: Long

        val message = this.originalMessage
        if (message.contains(QuoteReply)) {
            quote = message[QuoteReply]!!.content
            memberQQ = message[QuoteReply]!!.source.fromId
        } else if (message.findIsInstance<At>() != null) {
            quote = ""
            memberQQ = message.findIsInstance<At>()!!.target
        } else {
            this.sender.sendMessage("没有语录来源")
            return
        }
//        val q = (this as MemberCommandSenderOnMessage).fromEvent.message
//        if (q[1] is QuoteReply){
//            // 如果有quote
//            quote = (q[1] as QuoteReply).source.originalMessage.content //quote message
//            memberQQ = (q[1] as QuoteReply).source.fromId
//        } else {
//            quote = ""
//            // TODO: check if message contains At
//            val at = args.contentsList().firstOrNull { it is At } as At
//            memberQQ = at.target
//        }

        // download image
        val imageUrl = image.queryUrl()
        val quoteFolderPath = PigeonBotConsole.resolveDataFile("images/quotes").absolutePath
        downloadImage(imageUrl, "$quoteFolderPath/$md5.jpg")
        // add quote
        QuoteData.quoteData.add(Quote(md5, memberQQ, quote))
        this.sender.sendMessage("添加成功")
    }
}

object RandomQuoteCommand : SimpleCommand(PigeonBotConsole, "语录", description = "随机语录") {
    @Handler
    suspend fun foo(context: CommandContext, arg: At) {
        val quote = QuoteData.quoteData.filter { it.memberQQ == arg.target }
        if (quote.isNotEmpty()) {
            val filename = quote.random().md5
            val quoteFolderPath = PigeonBotConsole.resolveDataFile("images/quotes").absolutePath
            val img = File("$quoteFolderPath/$filename.jpg")
            context.sender.subject!!.sendImage(img)
        } else {
            context.sender.subject!!.sendMessage("没有找到语录")
        }
    }
}

object QueryQuoteCommand : SimpleCommand(PigeonBotConsole, "搜索语录", "q", description = "搜索语录") {
    @Handler
    suspend fun getQuoteFromUser(context: CommandContext, target: At, content: String) {
        val randomImageMd5 = QuoteData.quoteData.filter {
            it.memberQQ == target.target && it.content.contains(content)
        }.randomOrNull()?.md5
        if (randomImageMd5 != null) {
            sendQuote(context, randomImageMd5)
        } else {
            context.sender.subject!!.sendMessage("没有找到语录")
        }
    }

    @Handler
    suspend fun getQuote(context: CommandContext, content: String) {
        val randomImageMd5 = QuoteData.quoteData.filter { it.content.contains(content) }.randomOrNull()?.md5
        if (randomImageMd5 != null) {
            sendQuote(context, randomImageMd5)
        } else {
            context.sender.subject!!.sendMessage("没有找到语录")
        }
    }

    suspend fun sendQuote(context: CommandContext, md5: String) {
        val quoteFolderPath = PigeonBotConsole.resolveDataFile("images/quotes").absolutePath
        val img = File("$quoteFolderPath/$md5.jpg")
        context.sender.subject!!.sendImage(img)
    }
}