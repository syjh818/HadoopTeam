# GIT提交问题
```bash
//http
git config --global http.proxy http://<代理服务器地址>:<代理服务器端口号>
//https
git config --global https.proxy https://<代理服务器地址>:<代理服务器端口号>
```
## Clash示例

```bash
//http
git config --global http.proxy http://127.0.0.1:7890
//https
git config --global https.proxy https://127.0.0.1:7890
```

# 运行环境
JDK版本 >= 17

# 模块说明
start：springboot启动点
web：后端接口实现
model：生成模型文件实现
predict：预测实现

# 接口路径

需以
```bash
/api
```
开头

# 文件路径
文件路径配置在start模块中的application-dev.yml文件中，具体实现时通过注解读取


