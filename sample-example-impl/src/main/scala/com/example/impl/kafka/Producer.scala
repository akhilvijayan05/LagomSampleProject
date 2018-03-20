package com.example.impl.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source.fromURL

trait Producer {

  val reader = fromURL(getClass.getResource("/kafkaproducer.properties")).bufferedReader()
  val props = new Properties()
  props.load(reader)
  val producer = new KafkaProducer[Integer, Integer](props)

  def produceData(data: List[Int], topic: String): Boolean = {

    data.map { value =>
      val producerRecord = new ProducerRecord[Integer, Integer](topic, value, value)
      println("Sending data: " + value+ " - "+ topic)
      producer.send(producerRecord)
    }
    producer.close()
    true
  }
}
