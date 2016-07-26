# Timer
 基于spring项目的注解式方法计时插件（可配置执行次数），可用于在开发过程中的优化方法、提升性能的检测工具。


### 效果

![](http://i.imgur.com/H6aRQgS.png)


以log debug级别打印类、方法、执行时间、执行次数信息。


### 使用方法：

1、首先你的项目必须是基于Spring项目工程，最好是maven项目，就可以直接增加依赖；

2、在spring的配置文件，如applicationContext.xml中加入：

![](http://i.imgur.com/JqMI63G.png)

3、pom.xml文件中增加插件

![](http://i.imgur.com/rYRDTw2.png)

	<dependency>
			<groupId>com.pluginX</groupId>
			<artifactId>timer</artifactId>
			<version>1.0.0</version>
		</dependency>

4、代码的方法上增加注解

<pre>
<code> 
@Timer(2) 
    public void excute() {
    ...
}
</code>
</pre>

如上，记录此方法循环执行2次所需要的时间(ms)


***
#####注意：注解后不添加数值@Timer，则被注解的方法默认执行1次，如果数值小于1@Timer(0)，被注解的方法则不执行。








