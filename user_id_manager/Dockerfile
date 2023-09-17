#
FROM eclipse-temurin:11
RUN mkdir /out
COPY target/scala-2.13/user_id_manager-assembly-0.1.0-SNAPSHOT.jar /out
EXPOSE 8002
CMD ["java", "-jar", "/out/user_id_manager-assembly-0.1.0-SNAPSHOT.jar"]
