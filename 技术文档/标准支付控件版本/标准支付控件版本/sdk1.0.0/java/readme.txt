JAVA SDK rev 1.0.1 

2012-12-11

==== 基本要求 ====

JDK 1.5以上版本

==== 使用说明 ====

───────
 代码文件结构
───────

UpmpSDK
  │
  ├src┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈类文件夹
  │  │
  │  ├com.unionpay.upmp.sdk.conf
  │  │  │
  │  │  └UpmpConfig.java┈┈┈┈┈┈基础配置类文件
  │  │
  │  ├com.unionpay.upmp.sdk.examples
  │  │  │
  │  │  ├PurchaseExample.java┈┈┈ 订单推送请求接口实例类文件
  │  │  │
  │  │  ├QueryExample.java┈┈┈┈┈交易信息查询接口实例类文件
  │  │  │
  │  │  ├RefundExample.java┈┈┈┈ 退货接口实例类文件
  │  │  │
  │  │  └VoidExample.java┈┈┈┈┈ 消费撤销接口实例类文件
  │  │
  │  ├com.unionpay.upmp.sdk.service
  │  │  │
  │  │  └UpmpService.java┈┈┈┈┈ 接口处理核心类
  │  │
  │  └com.unionpay.upmp.sdk.util
  │      │
  │      ├HttpUtil.java┈┈┈┈┈┈ HttpClient处理类文件
  │      │ 
  │      ├httputil.properties┈┈┈ HttpClient配置文件
  │      │
  │      ├UpmpCore.java┈┈┈┈┈┈ 公用函数类文件
  │      │
  │      └UpmpMd5Encrypt.java┈┈┈ MD5签名类文件
  │
  ├web┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈WEB目录
  │  │  
  │  ├notify_url.jsp ┈┈┈┈┈┈┈ 异步通知页面
  │  │
  │  └WEB-INF
  │   	  │
  │       └lib（如果JAVA项目中包含这些架包，则不需要导入）
  │   	     │
  │   	     ├commons-codec-1.5.jar
  │   	     │
  │   	     ├commons-httpclient-3.1.jar
  │   	     │
  │   	     └commons-logging-1.1.1.jar
  │
  └readme.txt ┈┈┈┈┈┈┈┈┈使用说明文本
  

※注意※
需要修改配置的文件是：
src/upmp.properties

本代码示例中获取远程HTTP信息使用的是commons-httpclient-3.1版本的第三方架包。
如果您不想使用该方式实现获取远程HTTP功能，可用其他方式代替，此时需您自行编写代码。

──────────
 出现问题，求助方法
──────────

如果在集成接口时，有疑问或出现问题，请在qq群提出，我们会有专门的技术支持人员为您处理


