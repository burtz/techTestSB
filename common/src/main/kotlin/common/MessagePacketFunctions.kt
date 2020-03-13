package common

import com.google.gson.GsonBuilder

open class MessagePacketFunctions {

    fun generateJson(): String {
        val gson = GsonBuilder().create()
        val jsonPacket : String = gson.toJson(this)
        //println("> Create JSON String: " + jsonPacket)
        return jsonPacket
    }
}