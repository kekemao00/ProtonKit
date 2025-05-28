# ProtonKit
![Lines of code](https://img.shields.io/tokei/lines/github.com/kekemao00/ProtonKit)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/kekemao00/ProtonKit)
![GitHub last commit](https://img.shields.io/github/last-commit/kekemao00/ProtonKit)
[![](https://jitpack.io/v/io.jitpack/gradle-simple.svg)](https://jitpack.io/#io.jitpack/gradle-simple)

1. 添加 key-value-vault 依赖

## TODO

- [ ] 整理 base, common, network, ui, util, widget,key-value-vault 等模块, 并发布到 jitpack 仓库


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


## 添加和使用 JitPack
使用 JitPack 发布 Android 库，以下是详细的步骤和配置方法：

### 1. 创建 GitHub 仓库并推送代码

首先，确保你的库模块代码已经推送到 GitHub 仓库。

### 2. 配置 `build.gradle` 文件

在你的库模块（例如 `yourlibrary`）的 `build.gradle` 文件中进行以下配置：

1. **应用插件**：

   ```
   groovy复制代码apply plugin: 'com.android.library'
   apply plugin: 'com.github.dcendents.android-maven'
   ```

2. **配置 Android 构建参数**：

   ```
   groovy复制代码android {
       compileSdkVersion 30
       defaultConfig {
           minSdkVersion 16
           targetSdkVersion 30
           versionCode 1
           versionName "1.0.0"
       }
       buildTypes {
           release {
               minifyEnabled false
               proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
           }
       }
   }
   ```

3. **添加 JitPack 配置**：

   ```
   groovy复制代码group = 'com.github.YourUsername'  // 替换为你的 GitHub 用户名
   version = '1.0.0'  // 库的版本号
   
   dependencies {
       implementation fileTree(dir: 'libs', include: ['*.jar'])
       // 添加你库的依赖项，例如：
       implementation 'androidx.core:core-ktx:1.6.0'
       implementation 'com.google.code.gson:gson:2.8.6'
   }
   ```

### 3. 配置根项目的 `build.gradle`

在项目根目录下的 `build.gradle` 文件中添加 JitPack 插件依赖：

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

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### 4. 创建 Git 标签并推送到 GitHub

为你的项目创建一个 Git 标签，然后推送到 GitHub：

```
sh复制代码git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

### 5. 使用 JitPack 生成库

1. 打开 [JitPack 网站](https://jitpack.io/)。
2. 输入你的 GitHub 仓库地址（例如 `https://github.com/YourUsername/YourRepository`），然后点击 "Look up"。
3. 在生成的页面中，你应该会看到可用的版本，点击生成对应的版本。

### 6. 在其他项目中引用你的库

在其他项目的 `build.gradle` 文件中添加 JitPack 仓库和你的库依赖：

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

### 示例项目结构

为了更好地理解，以下是一个示例项目结构：

```
bash复制代码YourRepository/
├── yourlibrary/            # 库模块
│   ├── src/
│   ├── build.gradle        # 库模块的 build.gradle 文件
│   └── AndroidManifest.xml
├── app/                    # 示例应用程序模块
│   ├── src/
│   └── build.gradle        # 示例应用程序的 build.gradle 文件
├── build.gradle            # 根目录下的 build.gradle 文件
├── settings.gradle         # 包含所有模块的 settings.gradle 文件
└── README.md               # 项目说明文件
```

通过以上步骤和配置，你可以将 Android 库发布到 JitPack，并在其他项目中轻松引用和使用。
