# ===== 1) Build Stage =====
FROM gradle:8.8-jdk17 AS build
WORKDIR /app
COPY . .
# 테스트 빌드 스킵: -x test
RUN ./gradlew clean bootJar -x test

# ===== 2) Run Stage =====
FROM eclipse-temurin:17-jre
WORKDIR /app
# 빌드 산출물 복사 (Spring Boot fat jar)
COPY --from=build /app/build/libs/*.jar app.jar

# Render가 PORT 환경변수를 줍니다.
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod}", "-jar","/app/app.jar"]
