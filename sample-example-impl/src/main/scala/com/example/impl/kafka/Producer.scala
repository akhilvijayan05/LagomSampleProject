package com.example.impl.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source.fromURL

class Producer {

  val reader = fromURL(getClass.getResource("/kafkaproducer.properties")).bufferedReader()
  val props = new Properties()
  props.load(reader)
  val producer = new KafkaProducer[Int, Int](props)

  val list = Range(1, 10000).toList

  def produceData(): Boolean = {

    list.map { value =>
      val producerRecord = new ProducerRecord[Int, Int]("MyTopic", value, value)
      println("Sending data: " + value)
      producer.send(producerRecord)
    }
    producer.close()
    true
  }
}
