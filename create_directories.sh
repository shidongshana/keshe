# 创建主项目目录
mkdir -p student-manage/src/main/java/com/example/studentmanage

# 进入主项目目录
cd student-manage/src/main/java/com/example/studentmanage

# 创建各个子目录
mkdir -p config
mkdir -p controller/dto
mkdir -p entity
mkdir -p filter
mkdir -p mapper
mkdir -p model
mkdir -p repository
mkdir -p service/impl
mkdir -p util

# 创建空文件
# config
touch config/PasswordEncoderConfig.java
touch config/RootConfiguration.java
touch config/SecurityConfiguration.java

# controller
touch controller/AccountInfoController.java
touch controller/AccountingController.java
touch controller/ExceptionController.java
touch controller/UserController.java

# entity
touch entity/RestBean.java

# filter
touch filter/JwtAuthenticationFilter.java

# mapper
touch mapper/AccountInfoMapper.java
touch mapper/AccountingMapper.java
touch mapper/UserMapper.java

# model
touch model/AccountInfo.java
touch model/Accounting.java
touch model/UserModel.java

# service
touch service/AccountInfoService.java
touch service/AccountingService.java
touch service/UserService.java

# util
touch util/JWTest.java
touch util/JwtUtils.java

# 主应用程序文件
touch StudentManageApplication.java 