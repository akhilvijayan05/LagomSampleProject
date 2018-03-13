package com.example.impl.kafkastreams

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source.fromURL

object Producer extends App {

  val reader = fromURL(getClass.getResource("/kafkastreamproducer.properties")).bufferedReader()
  val props = new Properties()
  props.load(reader)
  val producer = new KafkaProducer[String, String](props)

  val list = List("Akhil", "Vijayan", "Akshansh", "Jain", "Sandeep", "Singh")

    list.map { value =>
      val producerRecord = new ProducerRecord[String, String]("MyTopic", value, value)
      println("Sending data: " + value)
      producer.send(producerRecord)
    }
    producer.close()
    true
}
