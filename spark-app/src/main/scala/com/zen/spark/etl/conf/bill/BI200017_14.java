package com.zen.spark.etl.conf.bill;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public  class BI200017_14 implements Serializable{
	/**
	 */
	 String ts;// 时间戳 2
	 String userId;// 用户ID 3
	 String appId; // appId 4
	 String gameId;// 游戏ID 5
	 String channelId;// 渠道 6
	 String apkVerison;// APK版本 7
	 String sdkVerison;// SDK版本 8
	 String gameVersion;// 游戏版本 9
	 String imei;// IMEI 10
	 String imsi;// imsi 11
	 String iccid;// iccid 12
	 String network;// 网络 13
	 String type;// 型号 14
	 String systemVersion;// 系统版本 15
	 String mainId;// 主事件ID 16
	 String sonId;// 子事件ID 17
	 String chargeGameId;//chargeGameId 19
	 String guid;//guid 20
	 String country;// 国家 21
	 String hour;// 小时 22
	 String statDate;// 天  yyyyMMdd格式 23
	 

	 String extra;//扩展的账单
	 String bill;//整条账单
	 
	 public Map<String,Integer> mapIndex = new HashMap<String, Integer>();
	 
	 public BI200017_14() {
		 mapIndex.put("ts", 2);
		 mapIndex.put("userId", 3);
		 mapIndex.put("appId", 4);
		 mapIndex.put("gameId", 5);
		 mapIndex.put("channelId", 6);
		 mapIndex.put("apkVerison", 7);
		 mapIndex.put("sdkVerison", 8);
		 mapIndex.put("gameVersion", 9);
		 mapIndex.put("imei", 10);
		 mapIndex.put("imsi", 11);
		 mapIndex.put("iccid", 12);
		 mapIndex.put("network", 13);
		 mapIndex.put("type", 14);
		 mapIndex.put("systemVersion", 15);
		 mapIndex.put("mainId", 16);
		 mapIndex.put("sonId", 17);
		 mapIndex.put("chargeGameId", 19);
		 mapIndex.put("guid", 20);
		 mapIndex.put("country", 21);
		 mapIndex.put("hour", 22);
		 mapIndex.put("statDate", 23);
		 
		 
		 mapIndex.put("cuAdType", 31);
		 mapIndex.put("cuSceneId", 32);
		 mapIndex.put("cuSonSceneId", 33);
		 mapIndex.put("cuAdvertiserId", 34);
		 mapIndex.put("cuSerialNumber", 35);
		 mapIndex.put("cuMsg", 36);
		 mapIndex.put("cuGameSerialNumber", 37);
		 mapIndex.put("cuGameExtra", 38);
		 mapIndex.put("cuFlag", 39);
		 mapIndex.put("cuAdvertisingFlag", 40);
		 mapIndex.put("cuOriginalUI", 41);
		 mapIndex.put("cuSonAdvertiser", 42);
		 mapIndex.put("cuMediaId", 43);
		 mapIndex.put("cuAdTpyeShowCount", 44);
		 mapIndex.put("cuAdId", 45);
		 mapIndex.put("cuAppName", 46);
		 mapIndex.put("cuDesc", 47);
		 mapIndex.put("cuDownloadUrl", 48);
		 mapIndex.put("cuEndCardUrl", 49);
		 mapIndex.put("cuIconUrl", 50);
		 mapIndex.put("cuImageMode", 51);
		 mapIndex.put("cuImageUrl", 52);
		 mapIndex.put("cuLandingPageUrl", 53);
		 mapIndex.put("cuPackageName", 54);
		 mapIndex.put("cuTitle", 55);
		 mapIndex.put("cuVideoUrl", 56);
		 mapIndex.put("cuTargetUrl", 57);
		 mapIndex.put("cuMarketUrl", 58);
		 mapIndex.put("cuEcpm", 59);
	}
	 
	/**
	 * 下面是自定义账单的字段
	 */
	 String cuAdType;// 广告类型  31
	 String cuSceneId; // 场景id 32
	 String cuSonSceneId; // 子场景id 33
	 String cuAdvertiserId; // 广告商id 34
	 String cuSerialNumber; // 流水号 35
	 String cuMsg;// msg 36
	 String cuGameSerialNumber; // 游戏透传流水号 37
	 String cuGameExtra; // 游戏扩展信息 38
	 String cuFlag; // 广告商应用标识 39
	 String cuAdvertisingFlag; // 广告位标识 40
	 String cuOriginalUI; // 原生插屏UI样式 41
	 String cuSonAdvertiser; // 子广告商 42
	 String cuMediaId; // mediaId素材ID 43
	 String cuAdTpyeShowCount; // 当前广告商广告类型展示次数 44
	 String cuAdId; // ad_id 45
	 String cuAppName; // appName 46
	 String cuDesc; // desc 47
	 String cuDownloadUrl; // downloadUrl 48
	 String cuEndCardUrl; // endCardUrl 49
	 String cuIconUrl; // iconUrl 50
	 String cuImageMode;// imageMode 51
	 String cuImageUrl; // imageUrl 52
	 String cuLandingPageUrl; // landingPageUrl 53
	 String cuPackageName; // packageName 54 
	 String cuTitle; // title 55
	 String cuVideoUrl; // videoUrl 56
	 String cuTargetUrl; // targetUrl 57
	 String cuMarketUrl; // marketUrl 58
	 String cuEcpm; // ecpm 59
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getApkVerison() {
		return apkVerison;
	}
	public void setApkVerison(String apkVerison) {
		this.apkVerison = apkVerison;
	}
	public String getSdkVerison() {
		return sdkVerison;
	}
	public void setSdkVerison(String sdkVerison) {
		this.sdkVerison = sdkVerison;
	}
	public String getGameVersion() {
		return gameVersion;
	}
	public void setGameVersion(String gameVersion) {
		this.gameVersion = gameVersion;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public String getSonId() {
		return sonId;
	}
	public void setSonId(String sonId) {
		this.sonId = sonId;
	}
	public String getChargeGameId() {
		return chargeGameId;
	}
	public void setChargeGameId(String chargeGameId) {
		this.chargeGameId = chargeGameId;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCuAdType() {
		return cuAdType;
	}
	public void setCuAdType(String cuAdType) {
		this.cuAdType = cuAdType;
	}
	public String getCuSceneId() {
		return cuSceneId;
	}
	public void setCuSceneId(String cuSceneId) {
		this.cuSceneId = cuSceneId;
	}
	public String getCuSonSceneId() {
		return cuSonSceneId;
	}
	public void setCuSonSceneId(String cuSonSceneId) {
		this.cuSonSceneId = cuSonSceneId;
	}
	public String getCuAdvertiserId() {
		return cuAdvertiserId;
	}
	public void setCuAdvertiserId(String cuAdvertiserId) {
		this.cuAdvertiserId = cuAdvertiserId;
	}
	public String getCuSerialNumber() {
		return cuSerialNumber;
	}
	public void setCuSerialNumber(String cuSerialNumber) {
		this.cuSerialNumber = cuSerialNumber;
	}
	public String getCuMsg() {
		return cuMsg;
	}
	public void setCuMsg(String cuMsg) {
		this.cuMsg = cuMsg;
	}
	public String getCuGameSerialNumber() {
		return cuGameSerialNumber;
	}
	public void setCuGameSerialNumber(String cuGameSerialNumber) {
		this.cuGameSerialNumber = cuGameSerialNumber;
	}
	public String getCuGameExtra() {
		return cuGameExtra;
	}
	public void setCuGameExtra(String cuGameExtra) {
		this.cuGameExtra = cuGameExtra;
	}
	public String getCuFlag() {
		return cuFlag;
	}
	public void setCuFlag(String cuFlag) {
		this.cuFlag = cuFlag;
	}
	public String getCuAdvertisingFlag() {
		return cuAdvertisingFlag;
	}
	public void setCuAdvertisingFlag(String cuAdvertisingFlag) {
		this.cuAdvertisingFlag = cuAdvertisingFlag;
	}
	public String getCuOriginalUI() {
		return cuOriginalUI;
	}
	public void setCuOriginalUI(String cuOriginalUI) {
		this.cuOriginalUI = cuOriginalUI;
	}
	public String getCuSonAdvertiser() {
		return cuSonAdvertiser;
	}
	public void setCuSonAdvertiser(String cuSonAdvertiser) {
		this.cuSonAdvertiser = cuSonAdvertiser;
	}
	public String getCuMediaId() {
		return cuMediaId;
	}
	public void setCuMediaId(String cuMediaId) {
		this.cuMediaId = cuMediaId;
	}
	public String getCuAdTpyeShowCount() {
		return cuAdTpyeShowCount;
	}
	public void setCuAdTpyeShowCount(String cuAdTpyeShowCount) {
		this.cuAdTpyeShowCount = cuAdTpyeShowCount;
	}
	public String getCuAdId() {
		return cuAdId;
	}
	public void setCuAdId(String cuAdId) {
		this.cuAdId = cuAdId;
	}
	public String getCuAppName() {
		return cuAppName;
	}
	public void setCuAppName(String cuAppName) {
		this.cuAppName = cuAppName;
	}
	public String getCuDesc() {
		return cuDesc;
	}
	public void setCuDesc(String cuDesc) {
		this.cuDesc = cuDesc;
	}
	public String getCuDownloadUrl() {
		return cuDownloadUrl;
	}
	public void setCuDownloadUrl(String cuDownloadUrl) {
		this.cuDownloadUrl = cuDownloadUrl;
	}
	public String getCuEndCardUrl() {
		return cuEndCardUrl;
	}
	public void setCuEndCardUrl(String cuEndCardUrl) {
		this.cuEndCardUrl = cuEndCardUrl;
	}
	public String getCuIconUrl() {
		return cuIconUrl;
	}
	public void setCuIconUrl(String cuIconUrl) {
		this.cuIconUrl = cuIconUrl;
	}
	public String getCuImageMode() {
		return cuImageMode;
	}
	public void setCuImageMode(String cuImageMode) {
		this.cuImageMode = cuImageMode;
	}
	public String getCuImageUrl() {
		return cuImageUrl;
	}
	public void setCuImageUrl(String cuImageUrl) {
		this.cuImageUrl = cuImageUrl;
	}
	public String getCuLandingPageUrl() {
		return cuLandingPageUrl;
	}
	public void setCuLandingPageUrl(String cuLandingPageUrl) {
		this.cuLandingPageUrl = cuLandingPageUrl;
	}
	public String getCuPackageName() {
		return cuPackageName;
	}
	public void setCuPackageName(String cuPackageName) {
		this.cuPackageName = cuPackageName;
	}
	public String getCuTitle() {
		return cuTitle;
	}
	public void setCuTitle(String cuTitle) {
		this.cuTitle = cuTitle;
	}
	public String getCuVideoUrl() {
		return cuVideoUrl;
	}
	public void setCuVideoUrl(String cuVideoUrl) {
		this.cuVideoUrl = cuVideoUrl;
	}
	public String getCuTargetUrl() {
		return cuTargetUrl;
	}
	public void setCuTargetUrl(String cuTargetUrl) {
		this.cuTargetUrl = cuTargetUrl;
	}
	public String getCuMarketUrl() {
		return cuMarketUrl;
	}
	public void setCuMarketUrl(String cuMarketUrl) {
		this.cuMarketUrl = cuMarketUrl;
	}
	public String getCuEcpm() {
		return cuEcpm;
	}
	public void setCuEcpm(String cuEcpm) {
		this.cuEcpm = cuEcpm;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public String getBill() {
		return bill;
	}
	public void setBill(String bill) {
		this.bill = bill;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getStatDate() {
		return statDate;
	}
	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}
	public Map<String, Integer> getMapIndex() {
		return mapIndex;
	}
	public void setMapIndex(Map<String, Integer> mapIndex) {
		this.mapIndex = mapIndex;
	}
	@Override
	public String toString() {
		return "{\"ts\":\"" + ts + "\",\"userId\":\"" + userId + "\",\"appId\":\"" + appId + "\",\"gameId\":\"" + gameId
				+ "\",\"channelId\":\"" + channelId + "\",\"apkVerison\":\"" + apkVerison + "\",\"sdkVerison\":\""
				+ sdkVerison + "\",\"gameVersion\":\"" + gameVersion + "\",\"imei\":\"" + imei + "\",\"imsi\":\"" + imsi
				+ "\",\"iccid\":\"" + iccid + "\",\"network\":\"" + network + "\",\"type\":\"" + type
				+ "\",\"systemVersion\":\"" + systemVersion + "\",\"mainId\":\"" + mainId + "\",\"sonId\":\"" + sonId
				+ "\",\"chargeGameId\":\"" + chargeGameId + "\",\"guid\":\"" + guid + "\",\"country\":\"" + country
				+ "\",\"hour\":\"" + hour + "\",\"statDate\":\"" + statDate + "\",\"extra\":\"" + extra
				+ "\",\"bill\":\"" + bill + "\",\"mapIndex\":\"" + mapIndex + "\",\"cuAdType\":\"" + cuAdType
				+ "\",\"cuSceneId\":\"" + cuSceneId + "\",\"cuSonSceneId\":\"" + cuSonSceneId
				+ "\",\"cuAdvertiserId\":\"" + cuAdvertiserId + "\",\"cuSerialNumber\":\"" + cuSerialNumber
				+ "\",\"cuMsg\":\"" + cuMsg + "\",\"cuGameSerialNumber\":\"" + cuGameSerialNumber
				+ "\",\"cuGameExtra\":\"" + cuGameExtra + "\",\"cuFlag\":\"" + cuFlag + "\",\"cuAdvertisingFlag\":\""
				+ cuAdvertisingFlag + "\",\"cuOriginalUI\":\"" + cuOriginalUI + "\",\"cuSonAdvertiser\":\""
				+ cuSonAdvertiser + "\",\"cuMediaId\":\"" + cuMediaId + "\",\"cuAdTpyeShowCount\":\""
				+ cuAdTpyeShowCount + "\",\"cuAdId\":\"" + cuAdId + "\",\"cuAppName\":\"" + cuAppName
				+ "\",\"cuDesc\":\"" + cuDesc + "\",\"cuDownloadUrl\":\"" + cuDownloadUrl + "\",\"cuEndCardUrl\":\""
				+ cuEndCardUrl + "\",\"cuIconUrl\":\"" + cuIconUrl + "\",\"cuImageMode\":\"" + cuImageMode
				+ "\",\"cuImageUrl\":\"" + cuImageUrl + "\",\"cuLandingPageUrl\":\"" + cuLandingPageUrl
				+ "\",\"cuPackageName\":\"" + cuPackageName + "\",\"cuTitle\":\"" + cuTitle + "\",\"cuVideoUrl\":\""
				+ cuVideoUrl + "\",\"cuTargetUrl\":\"" + cuTargetUrl + "\",\"cuMarketUrl\":\"" + cuMarketUrl
				+ "\",\"cuEcpm\":\"" + cuEcpm + "\",\"getTs()\":\"" + getTs() + "\",\"getUserId()\":\"" + getUserId()
				+ "\",\"getAppId()\":\"" + getAppId() + "\",\"getGameId()\":\"" + getGameId()
				+ "\",\"getChannelId()\":\"" + getChannelId() + "\",\"getApkVerison()\":\"" + getApkVerison()
				+ "\",\"getSdkVerison()\":\"" + getSdkVerison() + "\",\"getGameVersion()\":\"" + getGameVersion()
				+ "\",\"getImei()\":\"" + getImei() + "\",\"getImsi()\":\"" + getImsi() + "\",\"getIccid()\":\""
				+ getIccid() + "\",\"getNetwork()\":\"" + getNetwork() + "\",\"getType()\":\"" + getType()
				+ "\",\"getSystemVersion()\":\"" + getSystemVersion() + "\",\"getMainId()\":\"" + getMainId()
				+ "\",\"getSonId()\":\"" + getSonId() + "\",\"getChargeGameId()\":\"" + getChargeGameId()
				+ "\",\"getGuid()\":\"" + getGuid() + "\",\"getCountry()\":\"" + getCountry()
				+ "\",\"getCuAdType()\":\"" + getCuAdType() + "\",\"getCuSceneId()\":\"" + getCuSceneId()
				+ "\",\"getCuSonSceneId()\":\"" + getCuSonSceneId() + "\",\"getCuAdvertiserId()\":\""
				+ getCuAdvertiserId() + "\",\"getCuSerialNumber()\":\"" + getCuSerialNumber() + "\",\"getCuMsg()\":\""
				+ getCuMsg() + "\",\"getCuGameSerialNumber()\":\"" + getCuGameSerialNumber()
				+ "\",\"getCuGameExtra()\":\"" + getCuGameExtra() + "\",\"getCuFlag()\":\"" + getCuFlag()
				+ "\",\"getCuAdvertisingFlag()\":\"" + getCuAdvertisingFlag() + "\",\"getCuOriginalUI()\":\""
				+ getCuOriginalUI() + "\",\"getCuSonAdvertiser()\":\"" + getCuSonAdvertiser()
				+ "\",\"getCuMediaId()\":\"" + getCuMediaId() + "\",\"getCuAdTpyeShowCount()\":\""
				+ getCuAdTpyeShowCount() + "\",\"getCuAdId()\":\"" + getCuAdId() + "\",\"getCuAppName()\":\""
				+ getCuAppName() + "\",\"getCuDesc()\":\"" + getCuDesc() + "\",\"getCuDownloadUrl()\":\""
				+ getCuDownloadUrl() + "\",\"getCuEndCardUrl()\":\"" + getCuEndCardUrl() + "\",\"getCuIconUrl()\":\""
				+ getCuIconUrl() + "\",\"getCuImageMode()\":\"" + getCuImageMode() + "\",\"getCuImageUrl()\":\""
				+ getCuImageUrl() + "\",\"getCuLandingPageUrl()\":\"" + getCuLandingPageUrl()
				+ "\",\"getCuPackageName()\":\"" + getCuPackageName() + "\",\"getCuTitle()\":\"" + getCuTitle()
				+ "\",\"getCuVideoUrl()\":\"" + getCuVideoUrl() + "\",\"getCuTargetUrl()\":\"" + getCuTargetUrl()
				+ "\",\"getCuMarketUrl()\":\"" + getCuMarketUrl() + "\",\"getCuEcpm()\":\"" + getCuEcpm()
				+ "\",\"getExtra()\":\"" + getExtra() + "\",\"getBill()\":\"" + getBill() + "\",\"getHour()\":\""
				+ getHour() + "\",\"getStatDate()\":\"" + getStatDate() + "\",\"getMapIndex()\":\"" + getMapIndex()
				+ "\",\"getClass()\":\"" + getClass() + "\",\"hashCode()\":\"" + hashCode() + "\",\"toString()\":\""
				+ super.toString() + "\"}";
	}
	
	

}

