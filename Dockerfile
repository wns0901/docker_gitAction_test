FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 80

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod",
  "-Dspring.datasource.url=${SPRING_MYSQL_URL}",
  "-Dspring.datasource.username=${SPRING_MYSQL_USERNAME}",
  "-Dspring.datasource.password=${SPRING_MYSQL_PASSWORD}",
  "-Dspring.data.mongodb.uri=${SPRING_MONGODB_URI}",
  "-Dspring.security.oauth2.client.registration.google.client-id=${SPRING_GOOGLE_CLIENT_ID}",
  "-Dspring.security.oauth2.client.registration.google.client-secret=${SPRING_GOOGLE_CLIENT_SECRET}",
  "-Dspring.mail.username=${SPRING_GOOGLE_MAIL_USERNAME}",
  "-Dspring.mail.password=${SPRING_GOOGLE_MAIL_PASSWORD}",
  "-Djwt.secret=${SPRING_JWT_SECRET}",
  "-Dcors.allowed-origins=${SPRING_CORS_ALLOWED_ORIGINS}",
  "-Dapp.oauth2.password=${SPRING_OAUTH2_PASSWORD}",
  "-Dapp.oauth2.auth-redirect-uri=${SPRING_OAUTH2_REDIRECT_URI}",
  "-Dcloud.aws.s3.bucket=${SPRING_S3_BUCKET}",
  "-Dcloud.aws.region.static=${SPRING_S3_REGION}",
  "-Dcloud.aws.credentials.accessKey=${SPRING_S3_ACCESS_KEY}",
  "-Dcloud.aws.credentials.secretKey=${SPRING_S3_SECRET_KEY}",
  "-Dgithub.token=${SPRING_GITHUB_TOKEN}",
  "/app.jar"]