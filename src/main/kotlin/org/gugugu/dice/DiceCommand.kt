package org.gugugu.dice

import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.SimpleCommand
import org.gugugu.PigeonBotConsole
import kotlin.random.Random

object DiceCommand : SimpleCommand(PigeonBotConsole, "roll", "r", description = "骰子") {
    @Handler
    suspend fun roll(context: CommandContext, args: String) {
        val parts = args.split("d", ignoreCase = true)
        if (parts.size != 2) {
            context.sender.sendMessage("格式错误")
            return
        }
        try {
            val repeat = Integer.valueOf(parts[0])
            val dice = Integer.valueOf(parts[1])
            if (repeat <= 0 || dice <= 0) {
                context.sender.sendMessage("格式错误")
                return
            }
            var out = 0
            for (i in 1..repeat) {
                out += Random.nextInt(1, dice + 1)
            }
            context.sender.sendMessage(out.toString())

        } catch (e: java.lang.NumberFormatException) {
            context.sender.sendMessage("格式错误")
            return
        }


    }
}