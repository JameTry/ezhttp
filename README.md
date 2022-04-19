# ezhttp
对java原生的http请求进行封装,springboot-starter的方式更加方便,使用的jdk版本为1.8,maven管理

在application.properties或yml文件中配置短短几行即可使用,目前仅支持get/post的提交请求
最少只需要配置两个参数

```yml
ezhttp:
  prefix-url: localhost #设置请求路径
  port: 6789 #设置请求端口
```



## 快速开始

首先将下载的项目使用maven package打包,找到jar包位置在路径栏中输入cmd,打开cmd到当前路径下

之后使用maven命令将当前jar包放到本地仓库

```bash
mvn install:install-file -Dfile=ezhttp-1.0.jar -DgroupId=work.jame -DartifactId=ezhttp-spring-boot-starter -Dversion=1.0 -Dpackaging=jar
```

在项目中导入即可

```xml
<dependency>
    <groupId>work.jame</groupId>
    <artifactId>ezhttp-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

在被spring管理的bean中注入即可使用

```java
    @Autowired
    private SendHttpRequestService httpService;
```

## Api

-   sendGet(String uri) 发送无参的get请求
-   sendGet(String uri,Map<String,String> params) 发送携带参数的get请求
-   sendPost(String uri) 发送无参的post请求
-   sendPost(String uri,String jsonData) 发送带参数的post请求


![](https://raw.githubusercontent.com/JameTry/ezhttp/master/src/img/1.png)
![](https://raw.githubusercontent.com/JameTry/ezhttp/master/src/img/2.png)
