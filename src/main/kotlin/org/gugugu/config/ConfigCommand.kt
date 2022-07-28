package org.gugugu.config

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import org.gugugu.PigeonBotConsole
import org.gugugu.intervalSendMessage

object ConfigCommand: CompositeCommand(PigeonBotConsole,"config") {
    @SubCommand
    suspend fun CommandSender.replyP(value: Double){
        if (value in 0.0..100.0){
            Config.replyP = value
            intervalSendMessage("自动回复概率更改为$value%")
        }
    }
    @SubCommand
    suspend fun CommandSender.hhshP(value: Double){
        if (value in 0.0..100.0){
            Config.hhshP = value
            intervalSendMessage("好好说话概率更改为$value%")
        }
    }

    @SubCommand
    suspend fun CommandSender.repeatP(value: Double){
        if (value in 0.0..100.0) {
            Config.repeatP = value
            intervalSendMessage("复读概率更改为$value%")
        }
    }
    @SubCommand
    suspend fun CommandSender.smartP(value: Double){
        if (value in 0.0..100.0) {

            Config.smartP = value
            intervalSendMessage("智能回复概率更改为$value%")
        }
    }

    @SubCommand
    suspend fun CommandSender.list(){
        intervalSendMessage(
            """
                自动回复概率为${Config.replyP}% 
                复读概率为${Config.repeatP}% 
                好好说话概率为${Config.hhshP}%
                智能回复概率为${Config.smartP}%
            """.trimIndent()
        )
    }

}