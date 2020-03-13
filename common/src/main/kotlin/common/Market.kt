package common

import com.mongodb.client.model.FindOneAndUpdateOptions
import org.bson.Document

data class Market(
    var eventId: String,
    var marketId: String,
    var name: String,
    var displayed: Boolean,
    var suspended: Boolean
    ): MessagePacketInterface, MessagePacketFunctions(){
    override fun mongoCreateSearchDoc() = Document("eventId",eventId)

    override fun mongoCreateSetDoc() = Document("\$push",Document("Markets",Document.parse(this.generateJson())))

    override fun mongoUpdateOptions() = FindOneAndUpdateOptions().arrayFilters(arrayListOf(Document("elem.marketId",marketId)))

    override fun mongoUpdateSearchDoc() = Document("Markets.marketId",marketId)

    override fun mongoUpdateSetDoc(): Document {
        val elemString = "Markets.\$[elem]."
        //Must be a nice way to loop through?
        val documentToUse = Document(elemString+"eventId",eventId)
                .append(elemString+"marketId", marketId)
                .append(elemString+"name",name)
                .append(elemString+"displayed",displayed)
                .append(elemString+"suspended",suspended)

        return Document("\$set",documentToUse)
    }
}
