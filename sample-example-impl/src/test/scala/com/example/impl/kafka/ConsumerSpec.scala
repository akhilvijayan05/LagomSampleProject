package com.example.impl.kafka

import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.{IntegerDeserializer, IntegerSerializer}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}


class ConsumerSpec extends WordSpec with EmbeddedKafka with Matchers with BeforeAndAfterAll with Consumer {

//  implicit val deserializer: IntegerDeserializer = new IntegerDeserializer()
//  implicit val config = EmbeddedKafkaConfig(kafkaPort = 7000, zooKeeperPort = 7001)
//  override val consumer = EmbeddedKafka.kafkaConsumer(config,deserializer,deserializer)
//
//  override def beforeAll(): Unit = {
//    EmbeddedKafka.start()
//  }
//
//  override def afterAll(): Unit = {
//    EmbeddedKafka.stop()
//  }
//
//  "Consumer" should {
//    "get message from a topic" in {
//      assert(consumeData(Range(1, 10000).toList ,"TestTopic"))//should === true
//    }
//  }
}

