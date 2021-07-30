package com.zx.summary.work;

/**
 * Description:
 * Author: 张显
 * Date: 2021/7/30 14:32:13
 * Copyright (c) 2021, ewell.com. All Rights Reserved.
 */
public class summary {

    /**
     * 部署的mysql为非容器方式，mysql更新为容器方式
     */
    public void summary01(){
        /**
            1：更换初始/home/portal/docker-compose.yml
            2：将容器全部删除
               docker ps -aq |xargs docker stop   --停止容器
               docker ps -aq |xargs docker rm      --删除容器
            3：执行docker-compose -f /home/portal/docker-compose.yml up -d
         */
    }

    /**
     * 部署的mysql为非容器方式，mysql还继续使用非容器方式
     */
    public void summary02(){
        /**
            1：更改/home/portal/docker-compose.yml文件（删除下图红框的内容）
              图片：resource/images/mysql非容器方式1.png mysql非容器方式2.png
            2：将所有容器删除
                 docker ps -aq |xargs docker stop   --停止容器
                 docker ps -aq |xargs docker rm      --删除容器
            3：执行docker-compose -f /home/portal/docker-compose.yml up -d
         */

    }

    /**
     * 服务器ip与docker网桥ip冲突
     */
    public void summaryo3(){
        //参考连接：https://www.cnblogs.com/dabenxiang/p/13687519.html
        /**
         方法一：
         1：手工更改 原因：执行脚本后容器使用的网络并非默认docker0网络，而是通过docker-compose建立的自定义portal_ewell 网络
         步骤：
             1》docker network ls  #查看docker的网络
             2》docker network inspect portal_ewell
               -- #查看网络的具体信息，可看到网桥的子网网段及网关（主要查看portal_ewell网络下是否拥有所创建的容器。
               -- 容器之间要实现通信必须在同一个网络下，若不在同一网络下可将不在同一网络的容器删除，
               -- 执行docker-compose -f /home/portal/docker-compose.yml up -d
             3》docker network rm portal_ewell #删除当前网桥（删除之前需将容器删除）
                 docker stop $(docker ps -a -q)   #停止所有容器
                 docker rm $(docker ps -a -q)     #删除所有容器
             4》docker network create --subnet 10.10.0.1/16 --gateway 10.10.0.1 portal_ewell
                #重新创建portal_ewell网桥（子网网段和网关根据需求更改）
             5》docker-compose -f /home/portal/docker-compose.yml up -d
                #启动容器，此时容器的网络为新创建的portal_ewell网络
         */

        /**
         方法二：
           一开始自定义docker网段，更改docker-compose.yml如下图，subnet根据需求更改
           例如：
             networks:
             ewell:
             driver: bridge
             ipam:
             driver: default
             config:
             - submet: 10.88.0.1/24
         */

        /**
         方法三：
            如要更改默认docker0网络网段
            1》vi /etc/docker/daemon.json加入下图红框内容，网段根据需求更改
                 {
                    "bip": "10.16.0.1/16    #需要修改处
                    "log-driver": "json-file"
                    "log-opts" {
                                    "max-size:"500M",
                                    "max-file":"5"
                    }
                 }
            2》systemctl restart docker #重启docker网络
         */

        /**
         客户端无法登录，网段 冲突，删除无用网段
         -- virbr0：是centos的linux的虚拟网桥virbr0默认ip，导致这个网段下的
         ifconfig virbr0 down
         brctl delbr virbr0
         nmcli device delete virbr0-nic
         systemctl mask libvirtd.service
         systemctl disable libvirtd.service



         */
    }
}
