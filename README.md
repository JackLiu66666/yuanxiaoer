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