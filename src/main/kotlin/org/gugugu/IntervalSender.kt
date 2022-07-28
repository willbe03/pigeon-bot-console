package org.gugugu

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
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

fun Message.intervalSendTo(contact: Contact){
    IntervalSender.sendMessage(this, contact)
}

object IntervalSender {

    private var queue = ArrayDeque<Pair<Message, Contact>>()

    fun sendMessage(message: Message, subject: Contact) {
        queue.add(Pair(message, subject))
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun startIntervalSend() {
        GlobalScope.launch {
            while (true) {
                if(queue.isNotEmpty()){
                    val message = queue.removeFirst()
                    message.second.sendMessage(message.first)
                }
                delay(1000)
            }
        }
    }
}