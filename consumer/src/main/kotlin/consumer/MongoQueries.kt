package consumer

import common.FullPacket
import org.bson.Document

class MongoQueries (fullPacket : FullPacket) {
    fun createSetDoc(): String {
        return "DS"
    }


}