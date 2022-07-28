package org.gugugu.saucenao

import com.google.gson.Gson
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.gugugu.config.Config
import org.gugugu.PigeonBotConsole
import org.gugugu.intervalSendMessage

/**
 * enable Twitter, Pixiv, yande.re, Pixivhistorical
 * ref: https://saucenao.com/tools/examples/api/index_details.txt
 */
const val MASK = 1099511631968
const val SAUCENAO_URL = "https://saucenao.com/search.php"

object SauceNaoCommand : RawCommand(PigeonBotConsole, "sauce", description = "查询图片来源") {
    override suspend fun CommandContext.onCommand(args: MessageChain) {
        if (this.originalMessage.contains(QuoteReply)) {
            // Quote image to search
            val image = this.originalMessage[QuoteReply]!!.source.originalMessage[Image]
            val a = MessageSourceBuilder()
                .allFrom(this.originalMessage[QuoteReply]!!.source)
                .build(botId = 2211584232L, kind = MessageSourceKind.GROUP)
            a.originalMessage
            if (image != null) {
                this.sender.intervalSendMessage(search(image.queryUrl()))
            } else {
                this.sender.intervalSendMessage("错误：回复的消息内容没有图片")
            }
        } else {
            // direct search
            if (args.contains(Image)) {
                this.sender.intervalSendMessage(search(args[Image]!!.queryUrl()))
            } else {
                this.sender.intervalSendMessage("错误：消息内容没有图片")
            }
        }
    }
}

//fun sauce(){
//    GlobalEventChannel.subscribeMessages{
//        contains("/sauce"){
//            val args = message
//            val meta = args.metadataList().firstOrNull() { it is QuoteReply }
//            if (meta != null) {
//                val quote = meta as QuoteReply
//                // Quote image to search
//                val image = quote.source.originalMessage.get(Image)
//                if (image != null) {
//                    subject.sendMessage(search(image.queryUrl()))
//                } else {
//                    subject.sendMessage("错误：回复的消息内容没有图片")
//                }
//            } else {
//                // direct search
//                if (args.contains(Image)) {
//                    subject.sendMessage(search(args.get(Image)!!.queryUrl()))
//                } else {
//                    subject.sendMessage("错误：消息内容没有图片")
//                }
//            }
//        }
//    }
//}

/**
 * @throws NullPointerException when the response body is empty
 * @return url of
 */
fun search(url: String): String {
    //build url
    val apikey = Config.sauceNaoApiKey
    val urlBuilder = SAUCENAO_URL.toHttpUrl().newBuilder()
    urlBuilder.addQueryParameter("dbmask", MASK.toString())
    urlBuilder.addQueryParameter("hide", "2")
    urlBuilder.addQueryParameter("testmode", "0")
    urlBuilder.addQueryParameter("numres", "8")
    urlBuilder.addQueryParameter("api_key", apikey)
    urlBuilder.addQueryParameter("output_type", "2")
    urlBuilder.addQueryParameter("url", url)
    // get response
    val client = OkHttpClient()
    val request = Request.Builder().url(urlBuilder.build()).get().build()
    val response = client.newCall(request).execute().body!!.string()
    val gson = Gson()

    val results = gson.fromJson(response, SauceNao::class.java)
    results.results.sortByDescending { it.header.similarity.toDouble() } // sort result list by descending similarity
    val result = results.results[0]
    if (result.header.similarity.toDouble() < 80.0)
        return "找不到来源"
    return when (result.header.index_id) {
        // yandere
        12 -> {
            """
                url: ${result.data.ext_urls[0]}
                yandere_id: ${result.data.yandere_id}
                similarity: ${result.header.similarity}
            """.trimIndent()
        }
        //twitter
        41 -> {
            """
                url: ${result.data.ext_urls[0]}
                twitter_user_name: ${result.data.twitter_user_handle}
                similarity: ${result.header.similarity}
            """.trimIndent()
        }
        //pixiv
        else -> {
            """
                url: ${result.data.ext_urls[0].toHttpUrl()}
                pixiv_id: ${result.data.pixiv_id}
                similarity: ${result.header.similarity}
            """.trimIndent()
        }
    }
}