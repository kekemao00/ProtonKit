# ProtonKit

1. 添加 key-value-vault 依赖

## TODO

- [ ] 整理 base, common, network, ui, util, widget,key-value-vault 等模块, 并发布到 jitpack


### 使用 JitPack

1. **将项目托管在 GitHub 上**：

    - 创建一个新的 GitHub 仓库并推送代码。

2. **配置 Gradle 文件**：

    - 在库模块的 `build.gradle` 文件中添加 JitPack 发布插件和相关配置：

      ```
      groovy复制代码apply plugin: 'com.github.dcendents.android-maven'
      group = 'com.github.YourUsername'
      version = '1.0.0'
      ```

    - 在项目的根目录下的 `build.gradle` 文件中添加 JitPack 插件依赖：

      ```
      groovy复制代码buildscript {
          repositories {
              google()
              mavenCentral()
          }
          dependencies {
              classpath "com.android.tools.build:gradle:7.0.2"
              classpath "com.github.dcendents:android-maven-gradle-plugin:2.1"
          }
      }
      ```

3. **创建 Git 标签**：

    - 创建一个标签并推送到 GitHub：

      ```
      sh复制代码git tag -a v1.0.0 -m "Release version 1.0.0"
      git push origin v1.0.0
      ```

4. **使用 JitPack 生成库**：

    - 打开 [JitPack 网站](https://jitpack.io/)。
    - 输入你的 GitHub 仓库地址，然后点击 "Look up"。
    - 在生成的页面中，你应该会看到可用的版本。点击生成对应的版本。

5. **在应用程序中引用库**：

    - 在你的应用程序的

      ```
      build.gradle
      ```

      文件中添加 JitPack 仓库和你的库依赖：

      ```
      groovy复制代码repositories {
          google()
          mavenCentral()
          maven { url 'https://jitpack.io' }
      }
      
      dependencies {
          implementation 'com.github.YourUsername:YourRepository:1.0.0'
      }
      ```

通过以上步骤，你可以将新建的库发布到远程仓库，方便其他项目引用和使用。