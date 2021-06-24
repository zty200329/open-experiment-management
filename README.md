# OpenExperiment
开放性实验管理平台
## 使用的技术栈
### 项目开发
SpringBoot+Mybatis+Redis+OpenOffice
### 项目构建
Maven
### 项目部署
Nginx
## 需求分析
### 系统思维导图
![lW3VFf.png](https://s2.ax1x.com/2020/01/09/lW3VFf.png)

### 立项流程图
![lWUBvT.png](https://s2.ax1x.com/2020/01/09/lWUBvT.png)

### 用户模块
* 角色:
    * 学生
        * 普通成员
        * 项目组长(由老师进行指定)
    * 指导老师
    * 实验室主任
    * 学院领导
    * 职能部门
    * 管理员
* 登录注册:
    * 普通用户通过信息绑定注册
    * 管理部门相关帐号需要管理员进行审核注册
    
### 项目板块
#### 学生功能
* 普通成员:
    * 在线填报个人信息
    * 查询全校课申请参加项目(附表2)
    * 在线申请
    * 申请回复查看
* 项目组长:
    * 在线提交申请表(限时)
    * 提交中期审核材料(限时),提交具体内容参考文档
#### 教师功能
* 实验立项申请表()
    * 在线填报 or 编辑后进行上传
* 查看学生选题情况
* 批准某学生的参与
* 帮某学生待选
* 拒绝申请
* 查看项目进度(审批情况,结题进度和结果)
* 查看某项目组学生信息
#### 实验室主任
* 审批项目(附表1)
* 汇总进行提交至学院领导
* 查看领导及职能部门审批结果和反馈意见
#### 学院领导
* 审批
#### 职能部门
* 审批项目
* 制定项目计划
* 查看下载二级单位审批后的材料
* 反馈意见
* 发布立项和结题结果
#### 职能部门领导
* 审批职能部门发布的立项和结题结果


### 主要的项目难点
1.数据可视化处理：
   系统需要将学生上传的文档实现在线预览，并将学生上传的证明材料和创建项目是输入的表单信息进行合并，实现文件预览
  >解决方案：使用OpenOffice+JConvert的方式显示doc文件转换成PDF（由表单部分和上传部分合并成）实现文件的在线预览。后端使用POI生成表单部分存在一定的格式问题，由前端先生成表单部分的HTML上传到后端转换为PDF
  ![Q2mNrt.png](https://s2.ax1x.com/2019/12/13/Q2mNrt.png)
  
2.学生的模糊条件查询
根据需求，教师在选择学生的时候需要选择指定的学生，并且需要验证学生信息是否正确，学生的查询需要支持姓名查询和学号查询（上线项目，学生的数量在6000+  两个学院的学生数量）
1.对于全局查询使用内置的locate函数的查询效率要高于like %name%
2.限制返回数据的数量，使用limit关键字，避免全局查询，同时也可以减小I/O开销
