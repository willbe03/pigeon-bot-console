package org.gugugu

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.toPlainText

suspend fun CommandSender.intervalSendMessage(message: Message) {
    subject?.sendMessage(message)
}

suspend fun CommandSender.intervalSendMessage(message: String) {
    subject?.sendMessage(message)
}

fun Contact.intervalSendMessage(message: Message) {
    IntervalSender.sendMessage(message, this)
}

fun Contact.intervalSendMessage(message: String) {
    IntervalSender.sendMessage(message.toPlainText(), this)
}

object IntervalSender {

    private var queue = ArrayList<Pair<Message, Contact>>()
    private var lastSendTime = 0L

    fun sendMessage(message: Message, subject: Contact) {
        queue.add(Pair(message, subject))
    }

    fun startIntervalSend() {
        GlobalScope.launch {
            while (true) {
                val time = System.currentTimeMillis()
                if(time - lastSendTime > 10 * 1000 && queue.isNotEmpty()) {
                    val message = queue.first()
                    queue.removeAt(0)
                    message.second.sendMessage(message.first)
                    lastSendTime = time
                }
            }
        }
    }
}