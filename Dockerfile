# 1. 指定基础镜像 (如果你的项目是 JDK 8，就改成 eclipse-temurin:8-jre)
FROM eclipse-temurin:17-jre

# 2. 设置容器内的工作目录
WORKDIR /app
RUN mkdir -p /app/upload && chmod 777 /app/upload

# 3. 把刚才打好的 Jar 包复制进来 (这里的 *.jar 是通配符，会自动匹配 target 下的 jar 文件)
COPY target/*.jar app.jar

# 4. 暴露端口 (要和你 application.yml 里的 server.port 一致，即 8081)
EXPOSE 8081

# 5. 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]