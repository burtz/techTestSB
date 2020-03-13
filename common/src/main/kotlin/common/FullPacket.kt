package common

data class FullPacket(val headerPacket: HeaderPacket,
                      val messagePacket : MessagePacketInterface)