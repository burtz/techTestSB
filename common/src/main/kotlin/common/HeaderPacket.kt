package common

data class HeaderPacket(
    var msgID: Int,
    var operation: String,
    var type: String,
    var timestamp: Long)
