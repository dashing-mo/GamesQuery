## 游讯阁
### 项目概述  
该APP的制作初衷是用于熟练Android开发的基本流程及各种控件的使用，因制作时间有限，部分功能未完成开发，但却已初具规模。  
目前的想法是做成一款游戏资讯聚合软件，使自己和大家能够更方便的来阅读兼具广度和深度的游戏文章。
### 功能需求设计  
* APK新版本检测更新
* 游戏资讯  
* 游戏库  
* 查价格  
* 我的（目前未完成）  
* 游戏搜索  
* 游戏推荐（仅完成DEMO阶段）
* 游戏识别（未完成）
* 游戏平台查看  
### 最后效果
![主页](http://47.103.11.19/Images/游讯阁成品预览.png)  
### 整体思路  
* 首先在APP启动时通过http连接服务器检测APK版本号，不相同时在主界面提示用户更新APP版本
* 主界面分为新闻资讯、游戏库、查价格、我的四个部分，核心实现组件时WebView
* 搜索界面获得用户输入的信息后通过SharedPreferences传递给搜索网址并显示给用户  
### 开发环境及软件
* Java JDK 12.0.1
* Android Studio 3.4.2
* Microsoft Windows  Internet Information Services
### Android程序设计

LaunchActivity  展示下Logo,还可以做一些耗时操作  
MainActivity 主界面  
NewsActivity 新闻资讯界面  
LibraryActivity 游戏库界面  
PriceActivty 价格查询界面  
PersonalActivity 我的界面  
PriceFindActivity 价格显示界面  
FindActivity 搜索结果显示界面  
GuideActivity 引导页  
PlatformWebViewActivity 游戏平台查看界面  
QuestionCardActivity 游戏问题卡界面  
SettingsActivity 设置界面  
AboutActivity 关于界面  

关系图：  
![主页](http://47.103.11.19/Images/游讯阁APP流程图.png)
### 主要涉及到的知识点
1. 联网更新部分使用了xutils3框架
2. Intent跳转，参数的传递
3. json数据的解析
4. handler的使用
5. 自定义Dialog
6. ViewPager和Fragment的结合使用
7. TabHost结合Activity的使用
8. Notifacation的使用
9. Navigation的使用
10. RadioButton的使用
11. 自定义WebView
12. listview数据填充及优化
13. 自定义Toolbar
14. 自定义SwipeRefreshLayout
