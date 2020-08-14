package com.zen.spark.bean

/**
 * @Author: xiongjun
 * @Date: 2020/8/6 17:25
 * @description
 * @reviewer
 */
case class UserProfile(userId:String,deviceId:String,device:Int,province:Int,city:Int,registerDate:String=null,os:String,dataDate:String)
case class UserBehavior(clickedAdList:Array[String],histGameList:Array[String],last15consumption:Double,lastVisitDate:Int,numOfGame:Int,skipAdTimes:Int,
                        totalConsumption:Double,userId:String,visitTimesAverage:Double,dataDate:String)
case class AdProfile(adId:String,advertiserId:String,appname:String,materialId:String)
case class HistImpl(requestId:String,timestamp:Long,sceneId:String,adId:String,materialId:String,adType:String,connectType:Int,deviceId:String,curAppId:String,channel:String,label:Int,dataDate:String)
