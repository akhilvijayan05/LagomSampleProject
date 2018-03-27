package com.example.impl.kafkaprocessing

import com.example.models.{Log, Request, Response}
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.connect.json.JsonSerializer
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class ProducerSpec extends WordSpec with EmbeddedKafka with Matchers with BeforeAndAfterAll with Producer {

  implicit val jsonSerializer = new JsonSerializer()
  implicit val config = EmbeddedKafkaConfig(kafkaPort = 7000, zooKeeperPort = 7001)
  override val producer = aKafkaProducer(jsonSerializer, config)

  override def beforeAll(): Unit = {
    EmbeddedKafka.start()
  }

  override def afterAll(): Unit = {
    EmbeddedKafka.stop()
  }

  "Producer" should {
    "publish message to a topic" in {
      val log = Log("123", Request("9999","New Json Message"), Response("Json Message"))
      assert(produceData(log, "TestTopic") == "TestTopic")
    }
  }
}
