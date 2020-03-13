package producer

import common.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.Socket

class StreamReader(private val serverAddress : String = "localhost",
                   private val serverPort : Int = 8282)
{
    private val socket = Socket(serverAddress, serverPort)
    private val inMessage = BufferedReader(InputStreamReader(socket.getInputStream()))

    fun reader() : FullPacket {
        var message = inMessage.readLine()
        println("Streamreader: " + message.toString())
        val splitMessage = splitMessage(message)
        val headerPacket = HeaderPacket(splitMessage[0].toInt(),splitMessage[1],splitMessage[2],splitMessage[3].toLong())
        return createFullPacket(headerPacket, splitMessage)
    }

    fun splitMessage(message : String) : List<String>
    {
        var result : List<String> = message.split("(?<!\\\\)\\|".toRegex())
        result = result.subList(1,result.lastIndex) //remove first and last index of the list which are blank
        return result
    }

    private fun createFullPacket(headerPacket : HeaderPacket, splitMessage : List<String>) : FullPacket
    {
        return when (headerPacket.type) {
            "outcome" -> FullPacket(headerPacket, Outcome(splitMessage[4],splitMessage[5],tidyName(splitMessage[6]),splitMessage[7], splitMessage[8] != "0",splitMessage[9] != "0"))
            "market" -> FullPacket(headerPacket, Market(splitMessage[4],splitMessage[5],tidyName(splitMessage[6]), splitMessage[7] != "0",splitMessage[8] != "0"))
            "event" -> FullPacket(headerPacket, Event(splitMessage[4],splitMessage[5],splitMessage[6], tidyName(splitMessage[7]),splitMessage[8].toLong(),splitMessage[9] != "0",splitMessage[10] != "0"))
            else -> throw(Exception())
        }
    }
    private fun tidyName(name: String): String {
        var tidiedName = name.replace(oldValue = "\\|", newValue = "")
        tidiedName = tidiedName.replace(oldValue = "\\", newValue = "")
        return tidiedName
    }
}

