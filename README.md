#智能家居
##简介
智能设备一步步进入了我们的生活中，如果能将它很好的用于家居生活，将能给人们带来极大的方便。

##业务场景
###智能问答
有了这个，才像有智能的感觉。 当你问你的设备一些话时，它能够按你的意愿去实施，比如，现在几点了，天气如何，能回答上来，觉得很帅。
###天气警报
当早晨起来后，如果不想跑下楼后，再重新跑回家，那么有这个是个不错的主意。
###提醒
好朋友的生日、结婚纪念日以及一对有意义的日子，如果能提前个一段时间提醒，不会让你手忙脚乱。
###家居设备控制
开关个灯，播放个音乐，是不是很神奇？
###特殊事务警报
离家模式时，有人进入；   
天然气泄漏；   
火灾、地震。
###定时自动处理事务
记录下每天24小时的温度、空气质量；   
如有兴趣，可拿照相机定时拍取夜空，看星星的变化。

##业务平台架构
![业务架构][1]
>说明：   
1. 这是一个浩大的工程，如果希望稳定好，服务器需放在云端。如果只求娱乐，可以直接放在家，如下面将介绍的直接使用手机做服务器。这样和室内主机公用一个手机即可。   
2. 终端设备较多，单片机上选型上要使用较好一点的。89C51估计有点困难了。   
3. 部分功能直接在室内主机上实现比较简单，如语音识别等。   


##基于智能手机的安防系统
###简介
按照上面的业务架构，目前基于智能手机，已经实现了该系统中安防系统的原型。   

###系统部署图
![部署图][2]
###软件介绍
手机服务器：   
通讯的核心部件，存储各手机的相关业务信息，以及提供手机控制端辅助的功能。   

手机控制端：   
可以进行远程查看各手机服务的位置，以及通过JPush发送控制命令。   

手机伺服端：   
提供拍照、发送邮件、与服务器通讯以及手机通讯的功能。


下位机：   
通过对传感器进行采集数据，并蓝牙的方式上报给手机。 

   
百度LBS/百度Map/JPush/Email：    
第三方软件/jar包，协同工作完成软件功能。   

###工作流程
1. 手机伺服启动时，上报硬件信息到手机服务器进行注册登记。
1. 启动百度定位服务，获取当前手机信息，并上报给手机服务器。
1. 启动JPush服务，获取当前JPush的注册信息，并上报给手机服务器。
1. 手机控制端打开后，可以从手机服务器获取到所登记的设备信息。
1. 下位机后，通过蓝牙直接和手机连接；
1. 下位机连接的设备检测到相关事件时，启动拍照，并发送邮件。

###效果截图
Mserver及Mcontrol软件：   
![MControl软件][3]

Mcontrol:  
功能列表    
![MControl菜单][4]

设备位置   
![MControl菜单][5]

发送消息通知   
![MControl菜单][6]

手机伺服：   
接受到消息通知:   
![MControl菜单][7]

接收到邮件：   
![MControl菜单][8]

[1]: pics/arch.png "业务架构图"
[2]: pics/deploy_arch.png "系统部署图"
[3]: pics/preview.png "软件图标"
[4]: pics/mcontrol_menu.png "功能菜单"
[5]: pics/mcontrol_location.png "设备位置"
[6]: pics/mcontrol_send_notice.png "发送消息"
[7]: pics/receive_notice.png "软件图标"
[8]: pics/mail.png "软件图标"



