package com.zen.spark.etl.conf;

import java.util.Arrays;
import java.util.List;

/**
 * 账单工具类
 *
 * @author howie
 * @since 2017-03-10
 */
public class BillUtils {

    /**
     * @param billId 账单ID,statDay:20170301
     * @return 账单路径
     */
    public static String getBillPath(String billId, String statDay) {

    	if("101001".equals(billId)){
    		 return "/data/bill/" + statDay + "/*/" + billId+"*";
    	}

    	if("200017".equals(billId)){
   		 return "/data/bill/" + statDay + "/*/" + billId+"/*";
    	}
    	
    	if("200007".equals(billId)){
      		 return "/data/bill/" + statDay + "/*/" + billId+"/*";
       	}
    	if("104005".equals(billId)){
    		return "/data/bill/" + statDay + "/*/" + billId+"/*";
    	}
    	
    	// push用户
    	if("101013".equals(billId)){
    		 return "/data/bill/" + statDay + "/*/" + billId+"*";
    	}
    	
        //return "/data/stat/" + statDay + "/" + billId; 直接对应ID
        List<String> stat_bill_file = Arrays.asList("lossUserBill-7", "lossUserBill-30", "userGuid", "UserBill", "UserBill2", "ChargeBill", "OrderBill", "DBChargeTemp", "stat_baiRenNiuNiu_firstEnter", "NewChannelIdUserBill", "userInfoChannelIdTouFangBill");
        //return "/data/stat/" + statDay + "/" + billId + "/*"; 对应文件夹，需要读取文件夹下内容
        List<String> stat_bill_dir = Arrays.asList("ImeiBill", "OpenIdBill", "ChannelImeiBill", "UserGameInfo", "UserGameInfo_v1", "ChargeBillDF");
        //return "/data/splitBigBill/" + statDay + "/200007_game/" + billId;
        List<String> splitBigBill_200007_game = Arrays.asList("200007_31050", "200007_31073", "200007_31083", "200007_30037");
        //return "/data/splitBigBill/" + statDay + "/200007_subBill/" + billId;
        List<String> splitBigBill_200007_subBill = Arrays.asList("200007_20", "200007_200", "200007_H5GC", "200007_201", "200007_3002", "200007_3003", "200007_gamecenter", "200007_privilege","200007_40001");
        if (stat_bill_file.contains(billId)) {
            return "/data/stat/" + statDay + "/" + billId;
        }
        if (stat_bill_dir.contains(billId)) {
            return "/data/stat/" + statDay + "/" + billId + "/*";
        }
        if (splitBigBill_200007_game.contains(billId)) {
            return "/data/splitBigBill/" + statDay + "/200007_game/" + billId;
        }
        if (splitBigBill_200007_subBill.contains(billId)) {
            return "/data/splitBigBill/" + statDay + "/200007_subBill/" + billId+ "@*";
        }

        //特殊指定
        if (billId.equals("UserBill-month")) {
            return "/data/stat/" + statDay + "*/UserBill";
        }
        if (billId.equals("ImeiBill-month")) {
            return "/data/stat/" + statDay + "*/ImeiBill";
        }
        if (billId.equals("TestUserBill")) {
            return "/test/stat/" + statDay + "/UserBill";
        }
        /*if (billId.equals("200007_subBill/200007_game")) {
            return "/data/splitBigBill/" + statDay + "/" + billId;
        }*/
        if (billId.equals("UserBill_boris")) {
            return "/data/stat/" + statDay + "/UserBill_boris/" + billId;
        }

        //取共同特征
        if (billId.startsWith("10500_1") || billId.equals("10500_60000") || billId.equals("10500_6650") || billId.equals("10500_60047") || billId.equals("10500_60049") || billId.equals("10500_60072")) {
            return "/data/splitBigBill/" + statDay + "/10500_subBill/" + billId;
        }
        if (billId.startsWith("10100_")) {
            return "/data/splitBigBill/" + statDay + "/10100_subBill/" + billId;
        }
        if (billId.startsWith("10500_31063") || billId.startsWith("10500_30037") || billId.startsWith("10500_32001")) {
            return "/data/splitBigBill/" + statDay + "/10500_game/" + billId;
        }
        if (billId.startsWith("200017_")) {
            if (billId.startsWith("200017_14_")) {
                return "/data/splitBigBill/" + statDay + "/200017_14/" + billId + "*";
            } else {
                return "/data/splitBigBill/" + statDay + "/200017_subBill/" + billId;
            }

        }
        if (billId.startsWith("200007_H5GC_")) {
            return "/data/splitBigBill/" + statDay + "/200007_h5gc/" + billId+ "@*";
        }
        if (billId.startsWith("地址库")) {
            return "/data/addressDB/" + billId;
        }
        if (billId.startsWith("101001_address")) {
            return "/data/stat/" + statDay + "/GetAddressSuper/*";
        }

        return "/data/bill/" + statDay + "/*/" + billId;
    }


    /**
     * @param billId 账单ID,statDay:20170301,hour:小时 ,例如:03
     * @return 账单路径
     */
    public static String getBillPath(String billId, String statDay, String hour) {
        return "/data/bill/" + statDay + "/" + hour + "/" + billId;
    }
}
