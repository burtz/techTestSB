package producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import common.FullPacket
import java.util.*


class Producer(private val brokers: String = "localhost:9092", private val topic: String = "betting"){

    private val producer = createProducer(brokers)
    private val jsonMapper = ObjectMapper().apply { registerKotlinModule()}

    private fun createProducer(brokers: String): Producer<String, String> {
        val props = Properties()
        props["bootstrap.servers"] = brokers
        props["key.serializer"] = StringSerializer::class.java.canonicalName
        props["value.serializer"] = StringSerializer::class.java.canonicalName
        return KafkaProducer<String, String>(props)
    }

    fun kafkaWriter(fullPacket: FullPacket)
    {
        val fullPacketJson = jsonMapper.writeValueAsString(fullPacket)
        val sendMessage = producer.send(ProducerRecord(topic, fullPacketJson))
        sendMessage.get()
    }

    fun closeProducer() { producer.close() }
}

