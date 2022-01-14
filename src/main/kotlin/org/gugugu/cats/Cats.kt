package org.gugugu.cats

import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.utils.ExternalResource.Companion.sendAsImageTo
import org.gugugu.PigeonBotConsole

fun cats(){
    val catFolder = PigeonBotConsole.resolveDataFile("images/cats")
    GlobalEventChannel.subscribeMessages{
        contains("fm"){
            catFolder.listFiles().random()?.sendAsImageTo(subject)
        }
    }
}