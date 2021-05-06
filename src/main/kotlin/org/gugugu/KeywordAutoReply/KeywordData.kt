package org.gugugu.KeywordAutoReply

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object KeywordData : AutoSavePluginData(saveName = "autoReply") {
    val replyData: MutableMap<String, MutableSet<String>> by value()
}