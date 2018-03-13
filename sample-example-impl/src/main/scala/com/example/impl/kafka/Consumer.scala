package com.example.impl.kafka

import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConversions._
import scala.io.Source.fromURL


class Consumer {

  val reader = fromURL(getClass.getResource("/kafkaconsumer.properties")).bufferedReader()
  val props = new Properties()
  props.load(reader)
  val consumer = new KafkaConsumer[Int, Int](props)

  def consumeData(): Unit = {

    consumer.subscribe(Collections.singletonList("MyTopic"))

    while (true) {
      val records = consumer.poll(1000)

      for (record <- records) {
        System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset())
      }
    }
  }
}