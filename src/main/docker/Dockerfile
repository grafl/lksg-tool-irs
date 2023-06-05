FROM eclipse-temurin:17.0.6_10-jre-alpine

VOLUME /tmp

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080/tcp

ENTRYPOINT ["java","-jar","/app.jar"]
# ENTRYPOINT ["java","${JAVA_OPTS}","-jar","/app.jar"] ide kell egy SHELL, hogy kiértékelje a ${}-t
# ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]
