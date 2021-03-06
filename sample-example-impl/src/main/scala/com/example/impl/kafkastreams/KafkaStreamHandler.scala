package com.example.impl.kafkastreams

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.example.impl.utils.constant.Constants
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream.{KStream, Produced}


trait KafkaStreamHandler {

  val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, Constants.APPLICATION_ID_CONFIG)
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_SERVERS_CONFIG)
    p.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    p.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    p
  }
  val builder = new StreamsBuilder

  val stringSerde: Serde[String] = Serdes.String()


  def streamProcessing(initialTopic: String): Boolean = {

    // Read the input Kafka topic into a KStream instance.
    val textLines: KStream[String, String] = builder.stream(initialTopic)
    // Variant 1: using `mapValues`
    val uppercasedWithMapValues: KStream[String, String] = textLines.mapValues(_.toUpperCase())

    // Write (i.e. persist) the results to a new Kafka topic called "UppercasedTextLinesTopic".
    //
    // In this case we can rely on the default serializers for keys and values because their data
    // types did not change, i.e. we only need to provide the name of the output topic.
    uppercasedWithMapValues.to("UppercaseTextLinesTopic", Produced.`with`(stringSerde, stringSerde))

    // We are using implicit conversions to convert Scala's `Tuple2` into Kafka Streams' `KeyValue`.
    // This allows us to write streams transformations as, for example:
    //
    //    map((key, value) => (key, value.toUpperCase())
    //
    // instead of the more verbose
    //
    //    map((key, value) => new KeyValue(key, value.toUpperCase())
    //

    // Variant 2: using `map`, modify value only (equivalent to variant 1)
    //val uppercasedWithMap: KStream[String, String] = textLines.map((key, value) => new KeyValue(key, value.toUpperCase()))

    // Variant 3: using `map`, modify both key and value
    //
    // Note: Whether, in general, you should follow this artificial example and store the original
    //       value in the key field is debatable and depends on your use case.  If in doubt, don't
    //       do it.
    //    val originalAndUppercased: KStream[String, String] = textLines.map((key, value) => new KeyValue(value, value.toUpperCase()))

    // Write the results to a new Kafka topic "OriginalAndUppercasedTopic".
    //
    // In this case we must explicitly set the correct serializers because the default serializers
    // (cf. streaming configuration) do not match the type of this particular KStream instance.
    //    originalAndUppercased.to("OriginalAndUppercasedTopic", Produced.`with`(stringSerde, stringSerde))
    //

    val streams: KafkaStreams = new KafkaStreams(builder.build(), config)
    streams.start()

    Runtime.getRuntime.addShutdownHook(new Thread(() => {
      streams.close(10, TimeUnit.SECONDS)
    }))
    true
  }

}
