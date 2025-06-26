# Use uma imagem base do Maven com JDK 18 para compilar o projeto
FROM maven:3.8.7-openjdk-18 as build
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
ADD . /usr/src/app
RUN mvn package

# Use uma imagem base do OpenJDK 17 para executar a aplicação
FROM eclipse-temurin:17-jdk
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY --from=build /app/target/*.jar app.jar

# Exponha a porta que a aplicação usará
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]