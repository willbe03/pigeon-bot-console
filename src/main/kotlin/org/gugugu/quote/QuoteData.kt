package org.gugugu.quote

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object QuoteData : AutoSavePluginData(saveName = "quotes") {
    val quoteData: MutableList<Quote> by value()
}

@Serializable
data class Quote(val md5: String, val memberQQ: Long, val content: String)