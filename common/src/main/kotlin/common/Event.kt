package common

import org.bson.Document
import com.mongodb.client.model.*

data class Event(
        var eventId : String,
        var category : String,
        var subcategory : String,
        var name : String,
        var startTime : Long,
        var displayed : Boolean,
        var suspended : Boolean
    ): MessagePacketInterface, MessagePacketFunctions()
{
    override fun mongoCreateSetDoc() = Document.parse(this.generateJson())

    override fun mongoCreateSearchDoc() = Document("eventID", eventId)

    override fun mongoUpdateSearchDoc() = Document("eventId",eventId)

    override fun mongoUpdateSetDoc() : Document
    {
        val documentToUse = Document("eventId", eventId)
                .append("category",category)
                .append("subcategory",subcategory)
                .append("name",name)
                .append("startTime" ,startTime)
                .append("displayed",displayed)
                .append("suspended",suspended)

        return Document("\$set",documentToUse)
    }

    override fun mongoUpdateOptions() = FindOneAndUpdateOptions()

}

