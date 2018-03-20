package com.example.impl.kafkastreams

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source.fromURL

trait StreamProducer {

  val reader = fromURL(getClass.getResource("/kafkastreamproducer.properties")).bufferedReader()
  val props = new Properties()
  props.load(reader)
  val producer = new KafkaProducer[String, String](props)

  def produceData(data: List[String], topic: String): Boolean = {

    data.map { value =>
      val producerRecord = new ProducerRecord[String, String]("MyTopic", value, value)
      producer.send(producerRecord)
      println("Sending data: " + value)

    }
    producer.close()
    true
  }
}
