FROM openjdk:8-jdk
MAINTAINER dcy "1540770111@qq.com"
COPY target/file-center.jar /app.jar
# 同步时间
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone
RUN sh -c 'touch /app.jar'
VOLUME /tmp
EXPOSE 5000
# 修改spring环境变量
ENV JAVA_OPTS=""
#ENV JAVA_OPTS="-Dspring.profiles.active=test"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]