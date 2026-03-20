### 打包项目
```
 & "d:\YX2_demo\yuanxiaoer\mvnw.cmd" clean package -DskipTests -f "d:\YX2_demo\yuanxiaoer\pom.xml"
```
```
docker build -t yuanxiaoer-app:v1.0 .
```
```
docker save -o yuanxiaoer-app-v1.0.tar yuanxiaoer-app:v1.0
```

SPRING_PROFILES_ACTIVE=prod

创建自定义网络
docker network create YX2-network

将现有容器连接到该网络
docker network connect YX2-network yuanxiaoer-app
docker network connect YX2-network yuanxiaoer-frontend

验证网络连接
docker network inspect YX2-network