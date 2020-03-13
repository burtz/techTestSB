package common

import com.mongodb.client.model.FindOneAndUpdateOptions
import common.MessagePacketFunctions
import common.MessagePacketInterface
import org.bson.Document

data class Outcome(
    var marketId: String,
    var outcomeId: String,
    var name: String,
    var price: String,
    var displayed : Boolean,
    var suspended : Boolean): MessagePacketInterface, MessagePacketFunctions(){

    override fun mongoCreateSearchDoc() = Document("Markets.marketId",marketId)

    override fun mongoCreateSetDoc() = Document("\$push",Document("Markets.$.Outcomes",Document.parse(this.generateJson())))

    override fun mongoUpdateSearchDoc() = Document("Markets",Document("\$elemMatch",Document("marketId",marketId).append("Outcomes.outcomeId",outcomeId)))

    override fun mongoUpdateSetDoc() = Document("\$set",Document("Markets.\$[marketArray].Outcomes.\$[outcomeArray]",Document.parse(this.generateJson())))

    override fun mongoUpdateOptions(): FindOneAndUpdateOptions {
        val filter1 = Document("marketArray.marketId",marketId)
        val filter2 = Document("outcomeArray.outcomeId",outcomeId)

        return FindOneAndUpdateOptions()
                .upsert(false) //can i set to true and use to create?
                .arrayFilters(arrayListOf(filter1, filter2))
    }

}