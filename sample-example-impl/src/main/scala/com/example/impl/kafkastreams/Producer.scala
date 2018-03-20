package com.example.impl.kafkastreams

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source.fromURL

object Producer extends App {

  val reader = fromURL(getClass.getResource("/kafkastreamproducer.properties")).bufferedReader()
  val props = new Properties()
  props.load(reader)
  val producer = new KafkaProducer[String, String](props)

//  var i = 0
//  while(true){
//    val producerRecord = new ProducerRecord[String, String]("MyTopic", i.toString, "Hello_Streams" + i.toString)
//    producer.send(producerRecord)
//    i += 1
//    Thread.sleep(2000)
//  }
  val list = List("Akhil", "Vijayan", "Akshansh", "Jain", "Sandeep", "Singh")


    list.map { value =>
      val producerRecord = new ProducerRecord[String, String]("MyTopic", value, value)
      producer.send(producerRecord)
      println("Sending data: " + value)

    }
    producer.close()
}
