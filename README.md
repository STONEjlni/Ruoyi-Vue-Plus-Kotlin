[![JDK-20](https://img.shields.io/badge/JDK-20-green.svg)]()
[![Kotlin-1.9.0](https://img.shields.io/badge/Kotlin-1.9.0-green.svg)]()

> 使用ruoyi-vue-plus: [传送门](https://plus-doc.dromara.org/#/common/demo_system)

> kotlin-springboot: 
> [maven配置1](https://www.uoften.com/article/212843.html)
> [maven配置2](https://blog.csdn.net/weixin_33756418/article/details/90304544)

~~~ html
使kotlin接口默认方法不需要被实现
<arg>-Xjvm-default=all</arg>
@JvmDefaultWithoutCompatibility
~~~

~~~ html
jdk21下的kapt有bug
https://youtrack.jetbrains.com/issue/KT-57389/KAPT3-uses-a-Javac-API-for-JCImport-which-will-break-in-JDK-21
https://youtrack.jetbrains.com/issue/KT-60507/Kapt-IllegalAccessError-superclass-access-check-failed-using-java-21-toolchain
先退回jdk20
等 kotlin 2.0.0-Beta1修复再升级21
~~~
