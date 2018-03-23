package com.example.impl.kafka

import java.util.Properties

import com.example.impl.utils.Constants
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source.fromURL

trait Producer {

  val reader = fromURL(getClass.getResource(Constants.KAFKA_STREAM_CONFIG_FILE)).bufferedReader()
  val props = new Properties()
  props.load(reader)
  val producer = new KafkaProducer[Integer, Integer](props)

  def produceData(data: List[Int], topic: String): String = {

    val kafkaTopicName = data.map { value =>
      val producerRecord = new ProducerRecord[Integer, Integer](topic, value, value)
      println("Sending data: " + value + " - " + topic)
      val recordMetaData = producer.send(producerRecord)
      recordMetaData.get().topic()
    }.headOption

    producer.close()

    kafkaTopicName match {
      case Some(topicName) => topicName
      case None => ""
    }
  }
}
