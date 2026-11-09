
# Imagem base com Java 25
FROM eclipse-temurin:25-jre-noble
# Diretório de trabalho dentro da imagem Docker
WORKDIR /app
# Copia o JAR do seu computador para dentro da imagem, renomeando-o para app.jar.
COPY target/mscloudgateway-0.0.1-SNAPSHOT.jar app.jar
# Porta utilizada pelo Spring Boot
EXPOSE 8080
# Define o comando executado quando o container iniciar.
ENTRYPOINT ["java", "-jar", "app.jar"]
