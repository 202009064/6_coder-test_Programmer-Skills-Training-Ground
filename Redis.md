# Redis 从零到用（新手小白版）

## 1. Redis 是什么？为什么需要它？

简单理解：**Redis 就是一个放在你电脑上、不会随服务重启而丢失数据的"超级记事本"**。

在本项目中，它用来存储用户登录状态。之前登录状态存放在服务端内存里，服务一重启就全丢了，用户需要重新登录。改用 Redis 后，即使重启服务，登录状态依然保留。

---

## 2. 安装 Redis（Docker 方式，推荐）

### 2.1 打开 Docker Desktop

- 按键盘 **Win 键**
- 搜索 **Docker Desktop**
- 点击打开，等待右下角系统托盘图标不再转圈

### 2.2 在终端执行启动命令

打开 PowerShell（或项目终端的 Terminal），粘贴并回车：

```bash
docker run -d --name redis --restart=always -p 6379:6379 redis:7-alpine
```

**命令拆解（不用记，了解即可）：**

| 参数               | 含义                                       |
| ------------------ | ------------------------------------------ |
| `docker run`       | 运行一个容器                               |
| `-d`               | 后台运行，不阻塞终端                       |
| `--name redis`     | 给容器取名叫 redis                         |
| `--restart=always` | 开机 / Docker Desktop 启动时自动重启       |
| `-p 6379:6379`     | 把你电脑的 6379 端口映射到容器的 6379 端口 |
| `redis:7-alpine`   | 使用 Redis 7 的精简版镜像                  |

### 2.3 验证是否启动成功

```bash
docker ps --filter name=redis
```

看到 `Up` 状态就说明成功了：

```
NAMES     STATUS         PORTS
redis     Up 10 seconds  0.0.0.0:6379->6379/tcp
```

---

## 3. 项目中的配置

项目里已经配好了，不需要再改动。在 `coder-test-backend\src\main\resources\application.yml` 中：

```yaml
spring:
    data:
        redis:
            host: localhost   # Redis 地址（本机）
            port: 6379        # Redis 端口（默认）
    session:
        store-type: redis     # 把 Session 存到 Redis
```

依赖在 `coder-test-backend\pom.xml` 中已添加：

```xml
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

**这些都已经配好了，你不需要做任何改动。**

---

## 4. 日常使用命令

| 场景              | 命令                                          |
| ----------------- | --------------------------------------------- |
| 启动 Redis        | `docker start redis`                          |
| 停止 Redis        | `docker stop redis`                           |
| 查看是否在运行    | `docker ps --filter name=redis`               |
| 进入 Redis 命令行 | `docker exec -it redis redis-cli`             |
| 查看所有 key      | 进入 redis-cli 后执行 `KEYS *`                |
| 查看 Session 数据 | 进入 redis-cli 后执行 `KEYS spring:session:*` |
| 退出 redis-cli    | `exit`                                        |

### 4.1 什么时候需要手动启动 Redis？

- 每次重启电脑后，运行 `docker start redis`（如果设了 `--restart=always` 则 Docker Desktop 会自动启动）
- 如果之前执行过 `docker stop redis`，需要重新 `docker start redis`

---

## 5. 工作原理（一张图看懂）

```
用户登录
    │
    ▼
POST /api/user/login
    │
    ▼
session.setAttribute("userId", 1)
    │
    ▼  spring-session-data-redis 自动拦截
    │
    ▼
存入 Redis ────────────► key: spring:session:abc123
                         value: { userId: 1, ... }

============================================
服务重启（之前会丢，现在不会丢了）
============================================

用户再次请求 /api/user/current
    │
    ▼
session.getAttribute("userId")
    │
    ▼  spring-session-data-redis 自动拦截
    │
    ▼
从 Redis 读取 ─────────► 还在！→ 返回 userId=1
```

**关键点**：Spring 代码完全不用改，`session.setAttribute` 和 `session.getAttribute` 照常写，`spring-session-data-redis` 会在底层自动把数据存到 Redis。

---

## 6. 常见问题

### Q: Redis 没启动就运行项目会怎样？

会报错，日志中看到 `Cannot connect to Redis`，项目启动失败。先 `docker start redis` 再重启项目。

### Q: 换了台电脑怎么重新安装？

重复第 2 步即可。但如果已经用 Docker 安装过一次，直接用 `docker start redis` 启动。

### Q: Redis 里有之前的数据，想清空？

```bash
docker exec -it redis redis-cli FLUSHALL
```

### Q: 怎么知道 Redis 里存了什么？

```bash
docker exec -it redis redis-cli
127.0.0.1:6379> KEYS *
127.0.0.1:6379> exit
```

### Q: 怎么卸载 Redis 容器？

```bash
docker stop redis
docker rm redis
```
