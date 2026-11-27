# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# Pick the runnable archive (.war or .jar), skipping *.original and plain files
RUN mkdir -p /out \
 && find target -maxdepth 1 -type f \( -name "*.war" -o -name "*.jar" \) \
      ! -name "*.original" ! -name "*-plain.*" -exec cp {} /out/app.jar \; \
 && ls -la /out \
 && test -f /out/app.jar

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /out/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx300m", "-jar", "app.jar"]
