# Builds a docker image for this connector.
# Expects links to "zookeeper", "kafka", "schema-registry" & "confluent-control-center" containers.
#
# Usage:
#   docker build -t jeffsvds/kafka-connect-opentsdb .
#   docker push jeffsvds/kafka-connect-opentsdb

FROM confluent/platform

COPY src/main/resources/connect-avro-standalone.properties /etc/schema-registry/
COPY src/main/resources/connect-avro-distributed.properties /etc/schema-registry/
COPY src/main/resources/connect-opentsdb-sink.properties /etc/kafka-connect-opentsdb/sink.properties
COPY target/scala-2.11/*.jar /usr/local/lib/

ENV CLASSPATH=$CLASSPATH:/usr/local/lib/kafka-connect-opentsdb-assembly-0.0.1.jar

EXPOSE 8083

USER confluent
ENTRYPOINT ["/usr/bin/connect-standalone", "/etc/schema-registry/connect-avro-standalone.properties", "/etc/kafka-connect-opentsdb/sink.properties"]
#ENTRYPOINT ["/usr/bin/connect-distributed", "/etc/schema-registry/connect-avro-distributed.properties"]
