FROM adoptopenjdk/openjdk11:jre-11.0.11_9-ubi-minimal

MAINTAINER Axual <maintainer@axual.io>
USER root
ADD target/libs/ /opt/ksml/libs/
ADD target/ksml-runner*.jar /opt/ksml/ksml.jar
RUN chown -R 1024:users /opt
WORKDIR /opt/ksml
USER 1024
ENTRYPOINT ["java", "-jar", "/opt/ksml/ksml.jar"]
