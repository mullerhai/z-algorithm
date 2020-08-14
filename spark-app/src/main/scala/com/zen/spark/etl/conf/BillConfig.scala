package com.zen.spark.etl.conf

import scala.collection.mutable


object BillConfig {

    def renderIndexMap(headerArr: Array[String]): Map[String, Int] = {
        val indexMap = new mutable.HashMap[String, Int]()
        for(i <- headerArr.indices) {
            indexMap(headerArr(i)) = i
        }
        indexMap.toMap
    }

    // UserBill
    def userBillIndexMap(): Map[String, Int] = {
        renderIndexMap(Array(
            // 1.时间戳 2.游戏ID 3.用户ID 4.渠道 5.版本 6.是否注册 7.是否激活设备 8.登录平台 9.VIP等级
            "ts", "gameId", "userId", "channelId", "version", "isRegist", "isRegistImeiUser", "loginPlat", "vipLevel"
        ))
    }

    // ImeiBill
    def imeiBillIndexMap(): Map[String, Int] = {
        renderIndexMap(Array(
            // 1.时间戳 2.游戏ID 3.IMEI 4.渠道 5.版本 6.新进设备 7.真实新进（历史去重） 8.只玩单机场 9.登陆平台 10.AB标识
            "ts", "gameId", "imei", "channelId", "version", "isRegist", "7", "isSingle", "loginPlat", "abTag"
        ))
    }

    // ChannelImeiBill
    def channelImeiBillIndexMap(): Map[String, Int] = {
        renderIndexMap(Array(
            // 1.时间戳 2.游戏ID 3.IMEI 4.渠道 5.版本 6.新进设备 7.真实新进（历史去重） 8.只玩单机场 9.登陆平台 10.AB标识
            "ts", "gameId", "imei", "channelId", "version", "isRegist", "7", "isSingle", "loginPlat", "abTag"
        ))
    }

    // OrderBill
    def orderBillIndexMap(): Map[String, Int] = {
        renderIndexMap(Array(
            // 1.订单号 2.时间戳 3.游戏ID 4.用户ID 5.Imei 6.支付渠道 7.注册渠道 8.版本 9.支付类型 10.省份
            // 11.道具ID 12.道具数量 13.DB金额 14.金额1 15.注册用户 16.尝试 17.点击 18.发送 19.发送结果 20.回调
            // 21.回调结果 22.回调时间 23.数据库充值 24.支付场景 25.金额3 26.子代码 27.金额4 28.金额2 29.折扣1 30.折扣2
            // 31.折扣3 32.折扣4 33.三张牌游戏ID 34.APK版本 35.子支付场景 36.SDK版本
            "paymentId", "ts", "gameId", "userId", "imei", "channelId", "7", "version", "payType", "province",
            "boxId", "boxNum", "dbMoney", "money1", "isRegist", "isTry", "isClick", "isSend", "sendResult", "isCallback",
            "callbackResult", "callbackTime", "chargeSuccess", "payScene", "money3", "subCode", "money4", "money2", "discount1", "discount2",
            "discount3", "discount4", "szpGameId", "apkVersion", "subPayScene", "sdkVersion"
        ))
    }
    def chargeBillIndexMap = orderBillIndexMap

    // 游戏注册
    def bill10021IndexMap(): Map[String, Int] = {
        renderIndexMap(Array(
            // 1.账单ID 2.游戏ID 3.时间戳 4.用户ID 5.IMEI 6.渠道 7.IP 8.版本
            "billId", "gameId", "ts", "userId", "imei", "channelId", "ip", "version"
        ))
    }

    // 游戏登录
    def bill10008IndexMap(): Map[String, Int] ={
        renderIndexMap(Array(
            // 1.账单ID 2.游戏ID 3.时间戳 4.用户ID 5.筹码 6.积分 7.等级 8.IMEI 9.渠道 10.IP
            // 11.版本 12.运营商 13.网络类型 14.VIP等级 15.登陆平台
            "billId", "gameId", "ts", "userId", "coin", "point", "level", "imei", "channelId", "ip",
            "version", "carrier", "network", "vipLevel", "loginPlat"
        ))
    }

    // Web注册
    def bill101002IndexMap(): Map[String, Int] ={
        renderIndexMap(Array(
            // 1.账单ID 2.游戏ID 3.用户ID 4.渠道 5.时间 6.注册平台 7.ip 9.浏览器 9.系统 10.版本号
            // 11.设备号 12.apkversion 13.sdkversion 14.device 15.运营商 16.网络 17.平台ID 18.imsi 19.iccid
            "billId", "gameId", "userId", "channelId", "ts", "plat", "ip", "browser", "system", "version",
            "imei", "apkVersion", "sdkVersion", "device", "carrier", "network", "platId", "imsi", "iccid"
        ))
    }

    // Web登录
    def bill101001IndexMap(): Map[String, Int] ={
        renderIndexMap(Array(
            // 1.账单ID 2.游戏ID 3.用户ID 4.渠道 5.时间 6.注册平台 7.ip 8.浏览器 9.系统 10.版本号
            // 11.运营商代码 12.网络 13.设备号 14.apkversion 15.sdkversion 16.device 17.平台ID 18.imsi 19.iccid 20.省份
            // 21.系统版本 22.定位省份 23.定位城市 24.厂商 25.分辨率 26.手机号 27.用户名 28.第三方扩展 29.登录间隔 30.ip省份
            // 31.ip城市 32.运营商 33.网络运营商
            "billId", "gameId", "userId", "channelId", "ts", "plat", "ip", "browser", "system", "version",
            "carrierCode", "network", "imei", "apkVersion", "sdkVersion", "device", "platId", "imsi", "iccid", "province",
            "androidVersion", "gpsProvince", "gpsCity", "devideCompany", "resolution", "mobile", "userName", "28", "loginInterval", "ipProvince",
            "ipCity", "carrier", "33"
        ))
    }

    // 尝试
    def bill100010IndexMap(): Map[String, Int] = {
        renderIndexMap(Array(
            // 1.账单ID 2.时间戳 3.订单号 4.游戏ID 5.游戏服ID 6.用户ID 7.渠道号 8.支付方式 9.下单金额 10.道具ID
            "billId", "ts", "paymentId", "gameId", "gameServerId", "userId", "channelId", "payType", "money", "boxId",
            // 11.道具数量 12.游戏币 13.手机设备号 14.扩展字段 15.支付状态 16.省份 17.运营商 18.APK版本 19.SDK版本 20.Game版本
            "boxNum", "coinNum", "imei", "extend", "payStatus", "province", "carrier", "apkVersion", "sdkVersion", "version",
            // 21.支付场景 22.子代码 23.场景详情 24.定位省份 25.定位城市 26.联网方式 27.imsi 28.iccid 29.设备名 30.接口来源
            "payScene", "subCode", "paySceneDetail", "gpsProvince", "gpsCity", "network", "imsi", "iccid", "device", "interfaceOrigin",
            // 31.web二次 32.实际金额 33.手机号 34.sdk支持 35.省份来源 36.运营商ID 37.特殊配置账单 38.特殊配置ID 39.优先级主键
            "webTwice", "actualMoney", "mobile", "sdkSupport", "provinceOrigin", "carrierId", "specialConfigBill", "specialConfigId", "priorityKey"
        ))
    }

    // 发送
    def bill200003IndexMap(): Map[String, Int] = {
        renderIndexMap(Array(
            // 1.账单ID 2.时间戳 3.用户ID 4.游戏ID 5.支付类型 6.道具ID 7.道具金额 8.订单号 9.渠道 10.版本
            // 11.IMEI 12.网络环境 13.运营商 14.省份 15.本地支付结果 16.支付结果描述 17.游戏版本 18.IMSI 19.ICCID 20.子代码
            // 21.运营商代码 22.场景ID 23.场景详情 24.sdk版本 25.手机号 26.sdk扩展参数 27.oppo支付类型
            "billId", "ts", "userId", "gameId", "payType", "boxId", "boxNum", "paymentId", "channelId", "version",
            "imei", "network", "carrier", "province", "result", "16", "17", "imsi", "iccid", "subCode",
            "carrierCode", "sceneId", "23", "sdkVersion", "mobile", "26", "27"
        ))
    }

    // 回调
    def bill100011IndexMap(): Map[String, Int] = {
        renderIndexMap(Array(
            // 1.账单ID 2.时间戳 3.订单号 4.第三方交易号 5.游戏ID 6.游戏服ID 7.用户ID 8.渠道号 9.支付方式 10.实际支付金额
            // 11.下单金额 12.道具ID 13.道具数量 14.游戏币 15.手机设备号 16.扩展字段 17.支付状态 18.省份 19.结果码 20.第三方结果码
            // 21.第三方状态描述 22.运营商 23.APK版本 24.SDK版本 25.Game版本 26.支付场景 27.支付消耗时间(回调-下单时间戳) 28.子代码 29.场景详情 30.定位省份
            // 31.定位城市 32.联网方式 33.imsi 34.iccid 35.设备名 36.尝试接口 37.第三方记录 38.手机号
            "billId", "ts", "paymentId", "outTradeNo", "gameId", "6", "userId", "channelId", "payType", "money",
            "billMoney", "boxId", "boxNum", "coin", "imei", "16", "status", "province", "19", "20",
            "21", "carrier", "apkVersion", "sdkVersion", "version", "paySceneId", "27", "subCode", "29", "30",
            "31", "network", "imsi", "iccid", "device", "interfaceOrigin", "37", "mobile"
        ))
    }

    // 上报
    def bill200007IndexMap(): Map[String, Int] = {
        renderIndexMap(Array(
            // 1.账单ID 2.时间戳 3.游戏ID 4.渠道 5.版本 6.IMEI 7.网络环境 8.运营商 9.游戏自定义 10.游戏版本
            "billId", "ts", "gameId", "channelId", "version", "imei", "network", "carrier", "subBill", "version", "11"
        ))
    }

    def billIndexCountMap(): Map[String, Int] = {
        Map(
            "10021" -> bill10021IndexMap().size,
            "10008" -> bill10008IndexMap().size,
            "101002" -> bill101002IndexMap().size,
            "101001" -> bill101001IndexMap().size,
            "100010" -> bill100010IndexMap().size,
            "200003" -> bill200003IndexMap().size,
            "100011" -> bill100011IndexMap().size,
            "200007" -> bill200007IndexMap().size
        )
    }

    def main(args: Array[String]): Unit = {
        println(billIndexCountMap())
    }
}
