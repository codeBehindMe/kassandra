FROM openjdk:11
COPY . /usr/src/app
WORKDIR /usr/src/app
CMD ["java","-jar" , "target/kassandra-1.0-SNAPSHOT-jar-with-dependencies.jar"]