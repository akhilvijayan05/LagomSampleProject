package com.example.impl.kafkastreams

import java.util.Properties

import com.example.impl.utils.Constants
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source.fromURL

trait StreamProducer {

  val reader = fromURL(getClass.getResource(Constants.KAFKA_STREAM_CONFIG_FILE)).bufferedReader()
  val props = new Properties()
  props.load(reader)
  val producer = new KafkaProducer[String, String](props)

  def produceData(data: List[String], topic: String): String = {

    val kafkaTopicName = data.map { value =>
      val producerRecord = new ProducerRecord[String, String](topic, value, value)
      val recordMetaData = producer.send(producerRecord)
      println("Sending data: " + value)
      recordMetaData.get().topic()
    }.headOption

    producer.close()

    kafkaTopicName match {
      case Some(topicName) => topicName
      case None => ""
    }
  }
}
