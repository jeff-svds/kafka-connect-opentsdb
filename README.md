OpenTSDB Sink Connector
=======================

This is a ***VERY*** minimal Kafka connector that pushes SinkRecords to OpenTSDB via its HTTP API. 

It assumes that incoming messages from the Kafka topic source are Avro messages that follow the schema passed as the command line argument at the bottom of this page.

## Contributions are welcome! ##

### Tested With ###
- Confluent Platform 3.0.0
- OpenTSDB 2.2.0

### Build the JAR file ###
    sbt assembly

### Running the connector: ###
Include the JAR file in your classpath:

    export CLASSPATH="$CLASSPATH:`pwd`/target/scala-2.11/kafka-connect-opentsdb-assembly-0.0.1.jar"

#### Standalone Mode
    $CONFLUENT_HOME/bin/connect-standalone $CONFLUENT_HOME/etc/schema-registry/connect-avro-standalone.properties connect-opentsdb-sink.properties

#### Distributed Mode
##### 1. Configure & Start Confluent Control Center
    $CONFLUENT_HOME/bin/control-center-start control-center.properties

##### 2. In a separate terminal, start the OpenTSDB sink connector
    $CONFLUENT_HOME/bin/connect-distributed $CONFLUENT_HOME/etc/schema-registry/connect-avro-distributed.properties

### Insert dummy Avro messages in a new terminal ###
    $CONFLUENT_HOME/bin/kafka-avro-console-producer --topic eeg --broker-list localhost:9092 --property value.schema='{"type":"record","name":"Point","namespace":"com.svds.kafka.connect.opentsdb","fields":[{"name":"metric","type":"string"},{"name":"timestamp","type":{"type":"long","logicalType":"timestamp-millis"}},{"name":"value","type":"double"},{"name":"tags","type":{"type":"map","values":"string"}}]}' --property schema.registry.url=http://localhost:8081
    {"metric":"eeg","timestamp":0,"value":1.0,"tags":{"sensor":"af3"}}

### Docker build ###
NOTE: Make sure the JAR is built ***BEFORE*** building the Docker container image
