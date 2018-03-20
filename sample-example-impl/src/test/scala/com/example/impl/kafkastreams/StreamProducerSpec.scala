package com.example.impl.kafkastreams

import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.common.serialization.StringSerializer
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}


class StreamProducerSpec extends WordSpec with EmbeddedKafka with Matchers with BeforeAndAfterAll with StreamProducer {

  implicit val serializer: StringSerializer = new StringSerializer()
  implicit val config = EmbeddedKafkaConfig(kafkaPort = 7000, zooKeeperPort = 7001)
  override val producer = aKafkaProducer(serializer, config) //EmbeddedKafka.kafkaProducer("TestTopic", new Integer(1), new Integer(1))(config, serializer, serializer)

  override def beforeAll(): Unit = {
    EmbeddedKafka.start()
  }

  override def afterAll(): Unit = {
    EmbeddedKafka.stop()
  }

  "StreamProducer" should {
    "publish message to a topic" in {
      assert(produceData(List("abc", "cde"), "TestTopic"))
    }
  }
}
