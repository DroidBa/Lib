# baseLib

#### 介绍
{**BaseLib介绍**
此库为提供给文思同学快速开发的基类库}

#### 软件架构
MVP MVVM 采用kotlin语言设计，引入了Jetpack框架


#### 安装教程

1. Add the JitPack repository to your build file
allprojects {
    repositories {
	...
        maven { url 'https://maven.aliyun.com/repository/public' }
    }
}
2.  Add the dependency

dependencies {
    //基础库依赖
    implementation 'com.cecilleo.lib:base:1.0.0'
}


#### 使用说明

参照Example示例

#### 更新发布
./gradlew clean build bintrayUpload -PbintrayUser=cecilleo -PbintrayKey=xxx -PdryRun=false

