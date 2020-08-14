package com.zen.spark.etl.conf;

import java.util.Arrays;
import java.util.List;

/**
 * 过滤约束
 *
 * @author Tom
 * @time 2018/1/11
 */
public class FilterConfig {


    //地方棋牌游戏
    public static List<String> filterDFGameId() {
        String[] gameId = {
                "34001", "34002", "34004", "34005", "34006",
                "34007", "34009", "34010", "34500", "34501",
                "34502", "34503", "34509", "34507", "34011",
                "34511", "34512", "34515", "34504", "34521"
        };
        return Arrays.asList(gameId);
    }

    //捕鱼游戏过滤
    public static List<String> filterBYGameId() {
        String[] gameId = {
                "31063", "30054", "30058", "30063"
        };
        return Arrays.asList(gameId);
    }

}
