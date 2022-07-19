package org.gugugu.config

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import org.gugugu.PigeonBotConsole

object ConfigCommand: CompositeCommand(PigeonBotConsole,"config") {
    @SubCommand
    suspend fun CommandSender.replyP(value: Double){
        if (value >= 0.0 && value <= 100.0){
            Config.replyP = value
            sendMessage("自动回复概率更改为$value%")
        }
    }
    @SubCommand
    suspend fun CommandSender.hhshP(value: Double){
        if (value >= 0.0 && value <= 100.0){
            Config.hhshP = value
            sendMessage("好好说话概率更改为$value%")
        }
    }

    @SubCommand
    suspend fun CommandSender.repeatP(value: Double){
        if (value >= 0.0 && value <= 100.0) {
            Config.repeatP = value
            sendMessage("复读概率更改为$value%")
        }
    }
    @SubCommand
    suspend fun CommandSender.smartP(value: Double){
        if (value >= 0.0 && value <= 100.0) {

            Config.smartP = value
            sendMessage("智能回复概率更改为$value%")
        }
    }

    @SubCommand
    suspend fun CommandSender.list(){
        sendMessage(
            """
                自动回复概率为${Config.replyP}% 
                复读概率为${Config.repeatP}% 
                好好说话概率为${Config.hhshP}%
                智能回复概率为${Config.smartP}%
            """.trimIndent()
        )
    }

}