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

# 1. 清空DOCKER-USER旧规则
iptables -F DOCKER-USER
# 2. 放行你的自定义网桥内容器间的所有互访流量（核心规则）
iptables -I DOCKER-USER -s 172.19.0.0/16 -d 172.19.0.0/16 -j ACCEPT
# 3. 放行已建立连接的回包流量
iptables -A DOCKER-USER -m conntrack --ctstate RELATED,ESTABLISHED -j ACCEPT
# 4. 放行对外暴露的前端端口
iptables -A DOCKER-USER -p tcp --dport 8080 -j ACCEPT
# 5. 收尾规则，其余流量返回系统处理
iptables -A DOCKER-USER -j RETURN