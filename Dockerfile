FROM gradle:jdk21-alpine as gradle
WORKDIR /app
COPY . /app
RUN gradle bootJar

FROM bellsoft/liberica-openjdk-alpine:21 as openjdk
WORKDIR /app
ENV MONGO_URI=mongodb://localhost:27017/cinemagazine
ENV PROFILE=prod
ENV API_KEY=KEY
ENV JWT_SECRET=SECRET
ENV PORT=8080
COPY --from=gradle /app/build/libs/*.jar /app/api-cinemagazine.jar
CMD java -Dserver.port=${PORT} -Xmx512m -jar api-cinemagazine.jar
EXPOSE ${PORT}