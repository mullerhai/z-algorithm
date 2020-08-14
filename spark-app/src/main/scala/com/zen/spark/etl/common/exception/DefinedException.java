package com.zen.spark.etl.common.exception;

import com.zen.spark.etl.util.HttpClientUtil;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author tony
 *
 * 自定义的异常
 * 
 */
public class DefinedException extends Exception{

	/**
	 * 
	 */
	private Map<String, Object> param = new HashMap<String, Object>();
	private String[] str = new String[]{"tony@zen-game.cn,binbin@zen-game.cn,bruce@zen-game.cn"};
	private String mailUrl = "https://oss.365you.com/api/sendMail";
	
	
	
	private static final long serialVersionUID = 1L;

	private String errorCode;

    public DefinedException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public DefinedException(String errorCode, String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
    }

	public DefinedException(String errorCode, String msg, String clssName) {
		super(msg);
        this.errorCode = errorCode;
        
        if(clssName.startsWith("com.zengame.stat.realtime.RealTime")){//if==
        	Jedis redis  = new Jedis("stat2");
            
            String key = redis.get("sparkTask_"+clssName);
            if(null==key){
    			//说明第一次发
    			redis.incr("sparkTask_"+clssName);
    			redis.expire("sparkTask_"+clssName, 3600*24);//保留一天
    			 //发送邮件
    	    	param.put("sender", "root@oss.365you.com");//发送者邮箱
    	    	 for(int j=0;j<str.length;j++){
    				   param.put("receiver", str[j]);
    				   param.put("subject", "执行sql异常");//主题
    				   param.put("content",clssName+":"+msg);
    				   HttpClientUtil.doPost(mailUrl, param );
    			  }
    		}else{
    			if(Long.valueOf(key) <= 9){
        			redis.incr("sparkTask_"+clssName);
        			redis.expire("sparkTask_"+clssName, 3600*24);//保留一天
        			 //发送邮件
        	    	param.put("sender", "root@oss.365you.com");//发送者邮箱
        	    	 for(int j=0;j<str.length;j++){
        				   param.put("receiver", str[j]);
        				   param.put("subject", "执行sql异常");//主题
        				   param.put("content",clssName+":"+msg);
        				   HttpClientUtil.doPost(mailUrl, param );
        			  }
        		 }
    		}
            redis.close();
        }else{
        	 //发送邮件
	    	param.put("sender", "root@oss.365you.com");//发送者邮箱
	    	 for(int j=0;j<str.length;j++){
				   param.put("receiver", str[j]);
				   param.put("subject", "执行sql异常");//主题
				   param.put("content",clssName+":"+msg);
				   HttpClientUtil.doPost(mailUrl, param );
			  }
        }//if==
        
        
       
	}
	
}
