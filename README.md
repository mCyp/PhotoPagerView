### [![Download](https://api.bintray.com/packages/jiewang19951030/Maven/PhotoPagerView/images/download.svg)](https://bintray.com/jiewang19951030/Maven/PhotoPagerView/_latestVersion) ![](https://img.shields.io/badge/language-java-orange.svg) [![](https://img.shields.io/badge/license-Apache2.0-green.svg)](https://opensource.org/licenses/apache2.0.php) 
# PhotoPagerView
🌁 **PhotoPagerView** 以ViewPager的形式实现大图预览 <br>

## 一、演示
<figure class="third">
    <img src="http://xxx.jpg">
    <img src="http://yyy.jpg">
</figure>

## 二、使用方法

### 1.添加依赖
  先在build.gradle(Project:xxx)的repositories中添加：
  ```
  allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
  ```
  然后在 build.gradle(Module:app) 的 dependencies 添加:
  ```
 dependencies {
    ...
   
    /*需要添加的依赖 这里可以查看一下上面的最新版本*/
    implementation 'com.jieWang:PhotoPagerView:xxx'
 }
  ```
### 2.使用
  **2.1 普通主题**
  ```java
IPhotoPager pageView = new PhotoPagerViewProxy.Builder(MainActivity.this)
   											// 添加图片 目前只支持Bitmap
                        .addBitmaps(bitmaps)
   											// 是否删除图片按钮 普通主题特有
                        .showDelete(true)
                        // 删除事件 普通主题特有
                        .setDeleteListener(new DeleteListener() {
                            @Override
                            public void onDelete(int position) {
                                // TODO 删除指定位置之后的回调
                            }
                        })
   											// 是否显示开始和退出动画
                        .showAnimation(true)
   											// 显示动画类型
   											// ANIMATION_SCALE_ALPHA 缩放透明度动画
   											// ANIMATION_TRANSLATION 平移动画
                        .setAnimationType(PhotoPagerViewProxy.ANIMATION_SCALE_ALPHA)
   											// 设置初始位置
                        .setStartPosition(0) 
                        .create();
pageView.show();
  ```
  **2.2 QQ主题**

```java
IPhotoPager pageView = new PhotoPagerViewProxy.Builder(MainActivity.this,TYPE_QQ)
												// 添加图片 
                        .addBitmaps(bitmaps)
                        // 是否显示动画
                        .showAnimation(true)
                       	// 设置初始位置
                        .setStartPosition(0)
                        // 设置弹幕数据 QQ主题特有
                        .setBarrages(barrages)
  											// 是否显示弹幕 QQ主题特有
                        .showBarrages(true)
                        .create();
pageView.show();
```

## 三、TODO

- [ ] 引入Glide加载图片
- [ ] 加入微信主题

## 四、感谢

[PhotoView](https://github.com/chrisbanes/PhotoView)

## License

  ```
  Copyright 2018 JieWang.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ```

