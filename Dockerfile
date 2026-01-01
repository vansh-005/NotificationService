FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8084

HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget -qO- http://localhost:8084/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["java","-jar","app.jar"]
