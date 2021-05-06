package org.gugugu.config

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginData(saveName = "config") {
    var replyP : Double by value(30.0)
    var smartP : Double by value(1.0)
    var repeatP : Double by value(3.0)
    var hhshP : Double by value(10.0)
}