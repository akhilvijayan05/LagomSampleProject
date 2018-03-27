package com.example.impl.kafkaprocessing

import com.example.models.{Log, Request, Response}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.common.serialization.{IntegerDeserializer, IntegerSerializer, StringDeserializer, StringSerializer}
import org.apache.kafka.connect.json.{JsonDeserializer, JsonSerializer}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

class ConsumerSpec extends WordSpec with EmbeddedKafka with Matchers with BeforeAndAfterAll with Consumer {

  implicit val jsonDeserializer: JsonDeserializer = new JsonDeserializer()
  implicit val deserializer: StringDeserializer = new StringDeserializer()
  implicit val jsonSerializer = new JsonSerializer()
  implicit val serializer = new StringSerializer()
  implicit val config = EmbeddedKafkaConfig(kafkaPort = 7000, zooKeeperPort = 7001)
  override val consumer = EmbeddedKafka.kafkaConsumer(config, deserializer, jsonDeserializer)
  val json = "{\"timestamp\":\"123\",\"request\":{\"id\":\"9999\",\"message\":\"New Json Message\"},\"response\":{\"message\":\"Json Message\"}}"
  val mapper = new ObjectMapper()
  val topic = "TestTopic"

  override def beforeAll(): Unit = {
    EmbeddedKafka.start()
    val root: JsonNode = mapper.readTree(json)
    EmbeddedKafka.publishToKafka(topic, "123", root)(config, serializer, jsonSerializer)
  }

  override def afterAll(): Unit = {
    EmbeddedKafka.stop()
  }

  "Consumer" should {
    "get message from a topic" in {
      val log = Log("123", Request("9999","New Json Message"), Response("Json Message"))
      assert(consumeData(topic) == Map("123" -> log)) //should === true
    }
  }
}

