FROM ubuntu:22.04

# 환경 변수 설정
ENV DEBIAN_FRONTEND=noninteractive

# 필수 패키지 설치
RUN apt-get update && apt-get install -y openjdk-17-jdk ffmpeg wget unzip curl zip

# Gradle 8.x 설치
RUN wget https://services.gradle.org/distributions/gradle-8.3-bin.zip -P /tmp && \
    unzip /tmp/gradle-8.3-bin.zip -d /opt && \
    ln -s /opt/gradle-8.3/bin/gradle /usr/bin/gradle

# Gradle 버전 확인 (선택 사항)
RUN gradle -v

WORKDIR /app

COPY . .

RUN gradle build -x test

WORKDIR /app/build/libs


EXPOSE 8887
ENTRYPOINT ["java", "-jar", "ownBang-0.0.1-SNAPSHOT.jar"]

