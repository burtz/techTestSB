package common

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.mongodb.client.model.FindOneAndUpdateOptions
import org.bson.Document

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
    @JsonSubTypes(
    JsonSubTypes.Type(value = Event::class, name = "common.Event"),
    JsonSubTypes.Type(value = Market::class, name = "common.Market"),
    JsonSubTypes.Type(value = Outcome::class, name = "common.Outcome"))
interface MessagePacketInterface {
    fun mongoCreateSetDoc() : Document
    fun mongoCreateSearchDoc() : Document
    fun mongoUpdateSearchDoc() : Document
    fun mongoUpdateSetDoc() : Document
    fun mongoUpdateOptions() : FindOneAndUpdateOptions
}