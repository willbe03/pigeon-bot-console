package org.gugugu.cats

import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.utils.ExternalResource.Companion.sendAsImageTo
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import org.gugugu.PigeonBotConsole
import org.gugugu.intervalSendTo

fun cats(){
    val catFolder = PigeonBotConsole.resolveDataFile("images/cats")
    GlobalEventChannel.subscribeMessages{
        (contains("fm") or contains("fdm")) {
            catFolder.listFiles()?.random()?.uploadAsImage(subject)?.intervalSendTo(subject)
        }
    }
}