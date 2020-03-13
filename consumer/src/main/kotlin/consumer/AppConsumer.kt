package consumer

fun main(args: Array<String>) {

    Thread {
        val mongoWriter = MongoWriter()
        val consumer = Consumer(mongoWriter = mongoWriter)
        consumer.subscribe()

        try {
            while (true) {
                consumer.consume()
            }
        } finally {
            consumer.close()
        }
    }.start()
}
