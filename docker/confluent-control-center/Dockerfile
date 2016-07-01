# Builds a docker image for Confluent Control Center.
# Expects links to "zookeeper", "kafka" & "schema-registry" containers.
#
# Usage:
#   docker build -t jeffsvds/confluent-control-center .
#   docker push jeffsvds/confluent-control-center

FROM confluent/platform

COPY control-center.properties /etc/control-center/

EXPOSE 9021

USER confluent
ENTRYPOINT ["/usr/bin/control-center-start", "/etc/control-center/control-center.properties"]
