stethoscope(听诊器)
======================
智能终端应用，负责LOG的获取，解析与上传

## How to build

### MacOS

brew install jsoncpp curl git libzip boost cmake aws-cpp-sdk</br>
git clone https://git.medevicedata.com/medatc/stethoscope.git</br>
cd stethoscope</br>
mkdir build && cd build</br>
cmake -DCMAKE_INSTALL_PREFIX={install directory} -DCMAKE_BUILD_TYPE=Debug .. </br></br>
make install</br>

### Ubuntu 安装路径为/opt/stethoscope

sudo apt-get install build-essential git libboost-chrono1.55-dev libboost-filesystem1.55-dev libboost-python1.55-dev libboost-regex1.55-dev libboost-system1.55-dev libboost-thread1.55-dev libcurl4-openssl-dev libjsoncpp-dev libpython2.7-dev libsqlite3-dev libssl-dev libzip-dev zlib1g-dev cmake3</br>
</br>
git clone https://github.com/aws/aws-sdk-cpp.git</br>
cd aws-sdk-cpp</br>
mkdir build && cd build</br>
cmake -DCMAKE_INSTALL_PREFIX={install directory} .. </br></br>
make install</br>
</br>
git clone https://git.medevicedata.com/medatc/stethoscope.git</br>
cd stethoscope</br>
mkdir build && cd build</br>
cmake -DCMAKE_INSTALL_PREFIX={install directory} -DCMAKE_BUILD_TYPE=Debug .. </br></br>
make install</br>

### Windows

install msys2 (https://sourceforge.net/projects/msys2/)</br>
launch mingw32</br>
pacman -S cmake make git mingw-w64-i686-boost mingw-w64-i686-curl mingw-w64-i686-gcc mingw-w64-i686-jsoncpp mingw-w64-i686-libzip mingw-w64-i686-openssl mingw-w64-i686-sqlite3 mingw-w64-i686-zlib</br>
git clone https://git.medevicedata.com/medatc/stethoscope.git</br>
cd stethoscope</br>
mkdir build && cd build</br>
export CC=cc</br>
export CXX=g++</br>
cmake -DCMAKE_INSTALL_PREFIX=<install directory> -DCMAKE_BUILD_TYPE=Debug .. </br></br>
make install</br>


## How to package

### Ubuntu

tar cvf stethoscope.tar {Install Path} </br>
cat {Install Path}/script/inst_script stethoscope.tar > inst_stethoscope </br>

### Windows

{Install Path}/tools/7zip/7za a package.exe {Install Path} </br>
cat {Install Path}/tools/7zip/7zsd.sfx {Install Path}/tools/config.txt package.exe > inst_stethoscope.exe </br>

## 制作升级包

### Ubuntu 版本号在根目录CMakeLists.txt中的APP_VERSION配置，打包前确认上个版本，然后修改此版本，重新编译安装，安装目录会有个version.info文件。里边写有版本号和平台

cd {Install Path} </br>
zip -r ../stethoscope_{version}.zip . -x \\*var\\* -x \\*etc\\* </br></br>
</br>
上传升级包：curl -F "file=@stethoscope_{version}.zip" -H "token:{管理员Token}" https://api.medatc.com/fdn/v1/stethoscope/application </br>


## Source Structure

- include  -- header files
- script   -- scripts
- src       
    - base  -- common module and utils
    - collector  -- task manage, log fetch, log upload
    - helper  -- config file encript/decript, app manage and upgrade
    - plugin  -- plugins
    - wincontainer  -- windows dialog app (for windows message)
    - test  -- test files
- third-party  -- prebuild library for windows/linux
- tools  -- windows tools


## Configuration

### Settings.json

```javascrips
// 对于盒子而言一般是不需要配置的
{
    "interface_name":"Service", // 网卡名称，使用此网卡的MAC地址，不配置默认用第一个
    "proxy": { // 代理
        "host":"192.168.1.100", // 代理服务器IP
        "port": 3128, // 代理服务端口
        "scheme": "https" // 代理类型
    }
}
```


### Task
```javascript
{
    "device_id":"mini_mac", // 设备ID
    "pull_interval":300,    // 抓取间隔，单位秒
    "upload_plan":"20:00",  // 上传计划，06:00;12:00;...（6点，12点，...） 或者 immediately（立刻）
    "file_spliter": { // 文件分割配置
        "class":"keyword", // 分割实现类，目前只支持keyword
        "limit": 512000, // 单次最大上传大小
        "delimiter":"END " // 分割符, 如果是不可见字符，转换成hex，例如\n ascii为10，则应填写"0x0A"
    },
    "protocol":{ // 插件配置，见下文
    }
}
```

### Protocol

- Local Storage

```javascript
{
    "type":"local_storage", // 插件名称
    "srcset": { // 日志文件集
        "filename":"LogFile\\S*", // 文件名，正则表达式，所有插件通用
        "base_dir":"/Users/benny/Documents/ruike/192.168.0.200", // 日志所在目录
        "converter": {  // 日志文件预处理，所有插件通用
            "filter":".*\\.gz", // 过滤，匹配才处理
            "executable":"gunzip", // 命令
            "args":"-c $1 > $2"  // 参数，$1 表示原文件，$2表示处理后存储路径
        }
    }
}
```

- FTP

```javascript
{
    "type":"ftp", 
    "srcset": { 
        "filename":"LogFile\\S*", 
        "url":"ftp://wangbin:stethoscope@192.168.100.100/var/log/",  // ftp url
        "hash_tail": "1", //从尾部获取内容做hash, 所有插件通用
        "converter": {  
            "filter":".*\\.gz", 
            "executable":"gunzip", 
            "args":"-c $1 > $2"
        }
        "pre_execute": { // 抓取之前执行的命令，例如关闭防火墙，所有插件通用
            "executable":"iptables_ctrl.py",
            "args":"-H 192.168.100.100 -u wangbin -p stethoscope off"
        },
        "post_execute": { // 抓取之后执行的命令，例如打开防火墙，所有插件通用
            "executable":"iptables_ctrl.py",
            "args":"-H 192.168.100.100 -u wangbin -p stethoscope on"
        }
    }
}
```

- Serial

```javascript
{
    "type":"serial",
    // 设置波特率
    "baudrate": 4800
}
```

- Passive FTP

```javascript
{
    "type":"passive_ftp", 
    "srcset": { 
        "filename":"LogFile\\S*", 
        "url":"telnet://wangbin:stethoscope@192.168.100.100:23",
        "converter": {  
            "filter":".*\\.gz", 
            "executable":"gunzip", 
            "args":"-c $1 > $2"
        }
        "pre_execute": { 
            "executable":"xxxx",
            "args":"xxxx"
        },
        "post_execute": { 
            "executable":"xxxx",
            "args":"xxxx"
        }
    }
}
```


### Command

- remote control(目前已经支持自动开启远程控制，此命令后续会被删除)

```javascript
{
    "tunnel_port":3010, // ssh 映射端口
    "duration": 600, // 有效时长，单位秒
}
```

- raw command

```javascript
{
    "expr":"reboot" // 任何Shell命令，例如重启
}
```

## F&Q


### 盒子没有心跳
1、是否网络欠费</br>
2、登录到盒子上（远程或者本地），查看程序日志定位问题

### 日志没有上传
1、登录到盒子上，查看程序日志，看是否有上传日志记录，没有的话定位原因（设备未开机或者网络问题导致连不上，设备无新日志，或者程序存在运行错误）</br>
2、盒子有正常上传日志，服务器未接收到。登录服务器，查看foundation_kinesis镜像日志查看是否kinesis服务出现问题