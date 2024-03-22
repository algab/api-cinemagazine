FROM openjdk:21-ea-jdk-slim
WORKDIR /app
ENV MONGO_URI=mongodb://localhost:27017/cinemagazine
ENV PROFILE=prod
ENV API_KEY=KEY
ENV JWT_SECRET=SECRET
ENV PORT=8080
COPY . /app
RUN ./gradlew bootJar
CMD java -Dserver.port=${PORT} -Xmx512m -jar /build/libs/cinemagazine-0.0.1-SNAPSHOT.jar
EXPOSE ${PORT}