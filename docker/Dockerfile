#指定基础镜像，在其上进行定制
ENV LANG C.UTF-8
FROM java:8
ARG workdir=/jeeagile
VOLUME ${workdir}
WORKDIR ${workdir}
#复制上下文目录下的target/demo-1.0.0.jar 到容器里
ADD target/jeeagile-demo-*.jar jeeagile-demo.jar

ADD /src/main/resources/application-demo.properties /data/config/application.properties

#bash方式执行，使test.jar可访问
#RUN新建立一层，在其上执行这些命令，执行结束后， commit 这一层的修改，构成新的镜像。
#RUN bash -c "touch /demo.jar"
#声明运行时容器提供服务端口，这只是一个声明，在运行时并不会因为这个声明应用就会开启这个端口的服务


#指定容器启动程序及参数   <ENTRYPOINT> "<CMD>"
ENTRYPOINT ["java","-jar","jeeagile-demo.jar","--spring.config.location=/data/config/application.properties"]


