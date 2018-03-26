package com.example.impl.kafkastreams

import java.util.Properties

import net.manub.embeddedkafka.EmbeddedKafkaConfig
import net.manub.embeddedkafka.streams.EmbeddedKafkaStreamsAllInOne
import org.apache.kafka.common.serialization.{IntegerSerializer, Serdes, StringSerializer}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}


class KafkaStreamHandlerSpec extends WordSpec with Matchers with BeforeAndAfterAll with EmbeddedKafkaStreamsAllInOne with KafkaStreamHandler {

  val streamBuilder = new StreamsBuilder
  val initialTopic = "TestTopic"
  implicit val emConfig = EmbeddedKafkaConfig(kafkaPort = 7000, zooKeeperPort = 7001)
//  override val config = streamConfig("application")(emConfig)
  implicit val serializer: StringSerializer = new StringSerializer()
  override val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "application")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:7000")
    p.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    p.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    p
  }

  "KafkaStreamHandler" should {
    "send the data form the source topic to another" in {
      runStreamsWithStringConsumer(
        topicsToCreate = Seq(initialTopic, "UppercaseTextLinesTopic"),
        topology = streamBuilder.build()
      ) { consumer =>
        // your test code goes here
        publishToKafka(initialTopic, key = "hello", message = "world")(emConfig,serializer,serializer)
        assert(streamProcessing(initialTopic))
      }
    }
  }
}
