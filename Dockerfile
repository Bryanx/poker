FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD /build/libs/game-service-0.0.1-SNAPSHOT.jar app.jar
RUN adduser -D myuser
USER myuser
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
CMD -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap java -Dserver.port=$PORT -jar app.jar