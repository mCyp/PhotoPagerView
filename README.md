### [![bintray](https://img.shields.io/bintray/v/jiewang19951030/Maven/PhotoPagerView.svg)](https://bintray.com/jiewang19951030/Maven/PhotoPagerView)  ![](https://img.shields.io/badge/language-java-orange.svg) [![](https://img.shields.io/badge/license-Apache2.0-green.svg)](https://opensource.org/licenses/apache2.0.php) ![](https://img.shields.io/badge/qq-200522649-red.svg)
# PhotoPagerView
**PhotoPagerView** 以ViewPager的形式展示多张图片 <br>
## 感谢
[PhotoView](https://github.com/chrisbanes/PhotoView)
## 演示
![缩放](https://github.com/mCyp/PhotoPagerView/blob/master/app/src/main/res/drawable/scale.gif)<br>
<cite>缩放动画.gif<cite><br>
![缩放](https://github.com/mCyp/PhotoPagerView/blob/master/app/src/main/res/drawable/translate.gif)<br>
横移.gif
## 使用方法
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
### 2.使用
  动画类型包括平移动画（ANIMATION_TRANSLATION）和透明度缩放动画（ANIMATION_SCALE_ALPHA）：
  ```
  PhotoPageView pageView = new PhotoPageView.Builder(MainActivity.this) 
                        .addPaths(paths)  // 每张图片的路径，包里面的FileUtils类支持查询一个目录下的所有的图片路径
                        .showDelete(false)  // 是否显示删除
                        .showAnimation(true)  // 是否显示动画
                        .setAnimationType(PhotoPageView.ANIMATION_TRANSLATION) // 动画类型 
                        .setStartPosition(0) // 图片打开的位置
                        .create();
  pageView.show();
  ```
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
  
