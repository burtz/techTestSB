package consumer

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import common.FullPacket

class MongoWriter(
        private val mongoHost : String = "localhost",
        private val mongoPort : Int = 27017,
        private val databaseName : String = "eventdatastore",
        private val collectionName : String = "events"
) {

    val mongoClient : MongoClient = MongoClient(mongoHost, mongoPort)
    //private var mongoClient = MongoClient(MongoClientURI("mongodb+srv://burtons:9cEWllgZc2kGaXKi@cluster0-ev6kx.mongodb.net/test?retryWrites=true&w=majority"))

    private val db: MongoDatabase = mongoClient.getDatabase(databaseName)
    private val collection: MongoCollection<Document> = db.getCollection(collectionName)



    fun createWrite(fullPacket: FullPacket) {
        try {
            val mongoQuery : MongoQueries = MongoQueries(fullPacket)
            if (fullPacket.headerPacket.type == "event") {
                collection.insertOne(fullPacket.messagePacket.mongoCreateSetDoc())
                //collection.insertOne(mongoQuery.createSetDoc())
                //println(fullPacket.messagePacket.mongoCreateSetDoc())
                println("-----------------------------------------------------------------")
            } else {
                collection.findOneAndUpdate(fullPacket.messagePacket.mongoCreateSearchDoc(), fullPacket.messagePacket.mongoCreateSetDoc())
                //println("1: Search Doc: " + fullPacket.messagePacket.mongoCreateSearchDoc())
                //println("2: Committed Doc: " + fullPacket.messagePacket.mongoCreateSetDoc())
                //println(fullPacket.messagePacket.mongoCreateSetDoc())
                println("-----------------------------------------------------------------")
            }
        }catch (E : Exception){
            println("ERROR : Creation failed")
            println("-----------------------------------------------------------------")} //Catches failed creations -> Ideally would store these somewhere to be re-processed
    }

    fun updateWrite(fullPacket: FullPacket) {
        try {
            collection.findOneAndUpdate(fullPacket.messagePacket.mongoUpdateSearchDoc(),
                    fullPacket.messagePacket.mongoUpdateSetDoc(),
                    fullPacket.messagePacket.mongoUpdateOptions())
            //println("1: Search Doc: " + fullPacket.messagePacket.mongoUpdateSearchDoc())
            //println("2: Committed Doc: " + fullPacket.messagePacket.mongoUpdateSetDoc())
            //println("3: UpdateOptions: " + fullPacket.messagePacket.mongoUpdateOptions().toString())
            println("-----------------------------------------------------------------")
        }catch (E : Exception){println("ERROR : Update failed")
            println("-----------------------------------------------------------------")} //Catches failed updates -> Ideally would store these somewhere to be re-processed
    }

    //Future implementation
    fun deleteEvent(document: Document) {
        collection.deleteMany(document)
    }
}

