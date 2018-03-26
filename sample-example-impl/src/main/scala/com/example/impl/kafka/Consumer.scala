package com.example.impl.kafka

import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

import scala.annotation.tailrec
import scala.collection.JavaConversions._
import scala.io.Source.fromURL

trait Consumer {

  val reader = fromURL(getClass.getResource("/kafkaconsumer.properties")).bufferedReader()
  val props = new Properties()
  props.load(reader)
  val consumer = new KafkaConsumer[Integer, Integer](props)

  def consumeData(topic: String): Map[Integer, Integer] = {

    consumer.subscribe(Collections.singletonList(topic))
    val result = consumerPoll(topic, consumer.poll(5000), Map())
    consumer.close()
    result
  }

  @tailrec
  private def consumerPoll(topic: String, records: ConsumerRecords[Integer, Integer], data: Map[Integer, Integer]): Map[Integer, Integer] = {

    if (records.count() == 0) {
      data
    } else {
      val record = for {
        record <- records
      } yield {
        record.key() -> record.value()
      }
      consumerPoll(topic, consumer.poll(1000), data ++ record.toMap)
    }
  }
}

