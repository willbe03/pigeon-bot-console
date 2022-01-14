package org.gugugu.game

import io.ktor.util.date.*
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.getMember
import org.gugugu.PigeonBotConsole

object GameCommand : CompositeCommand(PigeonBotConsole, "game", "g", description = "游戏") {
    val games = GameData.games
    const val INDENT: String = "    "

    @SubCommand("open", "o")
    suspend fun CommandSender.open(name: String) {
        if (!games.contains(Game(name, mutableListOf(), getTimeMillis()))) {
            games.add(Game(name, mutableListOf(user!!.id), getTimeMillis()))
            sendMessage("添加成功")
        }else{
            sendMessage("已有此名称，请使用其他名称")
        }
    }

    @SubCommand("list", "ls")
    suspend fun CommandSender.list() {
        var reply = ""
        for (game in games) {
            reply += game.name+"\n"
            for (user in game.gamers) {
                reply += INDENT + bot?.getGroup(subject!!.id)?.getMember(user)?.nick+"\n"
            }
        }
        sendMessage(reply)
    }

    @SubCommand("join", "j")
    suspend fun CommandSender.join(name: String) {
        
    }

    @SubCommand("quit", "q")
    suspend fun CommandSender.quit(name: String) {

    }

    @SubCommand("remove", "rm")
    suspend fun CommandSender.remove(name: String) {

    }
}
