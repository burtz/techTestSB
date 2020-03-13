package consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import common.FullPacket
import java.time.Duration
import java.time.LocalDateTime
import java.util.*


class Consumer (private val brokers: String = "localhost:9092",
                private val topic: String = "betting",
                val mongoWriter : MongoWriter) {

    private val consumer = createConsumer(brokers)
    private val jsonMapper = ObjectMapper().apply{registerKotlinModule()}

    fun subscribe()
    {
        consumer.subscribe(listOf(topic))
    }

    private fun createConsumer(brokers: String): Consumer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["group.id"] = "betting-processor"
        props["key.deserializer"] = StringDeserializer::class.java
        props["value.deserializer"] = StringDeserializer::class.java
        return KafkaConsumer<String, String>(props)
    }

    fun consume() {
        val records = consumer.poll(Duration.ofSeconds(1))
        println("processing " + records.count() + " records @ " + LocalDateTime.now())
        records.iterator().forEach {
            println("consumer: " + it.value().toString())
            val message = it.value()
            val fullPacket : FullPacket = jsonMapper.readValue(message, FullPacket::class.java)

            when(fullPacket.headerPacket.operation){
                "create" -> mongoWriter.createWrite(fullPacket)
                "update" -> mongoWriter.updateWrite(fullPacket)
                else -> throw Exception("ERROR : Unknown Operation")
            }
        }
    }

    fun close(){
        consumer.close()
    }
}

