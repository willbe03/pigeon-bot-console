package org.gugugu.config

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.SimpleCommand
import org.gugugu.config.ConfigCommand.repeatP
import org.gugugu.config.ConfigCommand.replyP
import org.gugugu.org.gugugu.PigeonBotConsole

object ConfigCommand: CompositeCommand(PigeonBotConsole,"config") {
    @SubCommand
    suspend fun CommandSender.replyP(value: Double){
        Config.replyP = value
        sendMessage("自动回复概率更改为$value")
    }
    @SubCommand
    suspend fun CommandSender.hhshP(value: Double){
        Config.hhshP = value
        sendMessage("好好说话概率更改为$value")
    }

    @SubCommand
    suspend fun CommandSender.repeatP(value: Double){
        Config.repeatP = value
        sendMessage("复读概率更改为$value")
    }
    @SubCommand
    suspend fun CommandSender.smartP(value: Double){
        Config.smartP = value
        sendMessage("智能回复概率更改为$value")
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