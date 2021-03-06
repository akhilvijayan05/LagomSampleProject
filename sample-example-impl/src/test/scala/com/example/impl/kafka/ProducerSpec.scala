package com.example.impl.kafka

import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.common.serialization.IntegerSerializer
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class ProducerSpec extends WordSpec with EmbeddedKafka with Matchers with BeforeAndAfterAll with Producer {

  implicit val serializer: IntegerSerializer = new IntegerSerializer()
  implicit val config = EmbeddedKafkaConfig(kafkaPort = 7000, zooKeeperPort = 7001)
  override val producer = EmbeddedKafka.kafkaProducer("TestTopic", new Integer(1), new Integer(1))(config, serializer, serializer) //aKafkaProducer(serializer,config)

  override def beforeAll(): Unit = {
    EmbeddedKafka.start()
  }

  override def afterAll(): Unit = {
    EmbeddedKafka.stop()
  }

  "Producer" should {
    "publish message to a topic" in {
      assert(produceData(List(1,2,3), "TestTopic") == "TestTopic")
    }
  }
}
