package com.zen.spark.etl.common;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.zen.spark.etl.conf.PropertiesUtil;
import com.zen.spark.etl.constant.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了<br>
 * 
 * @author tony
 *
 * @time 2017-8-17
 */
public class MongoDBUtil {


    private  MongoClient mongoClient;

    // 保证MongoDBUtil只有一个实例，实例中只有一份数据库连接池
    private static MongoDBUtil instance = null;
    
    
    public MongoDBUtil() throws InterruptedException{
/*    	// 链接池数量  
        String connectionsPerHost = "50";  
        // 最大等待时间  
        String maxWaitTime = "5000";  
        // scoket超时时间  
        String socketTimeout = "0";  
        // 设置连接池最长生命时间  
        String maxConnectionLifeTime = "";  
        // 连接超时时间  
        String connectTimeout = "15000";  
  
        MongoClientOptions options = MongoClientOptions.builder()  
                .connectionsPerHost(Integer.parseInt(connectionsPerHost))  
                .maxWaitTime(Integer.parseInt(maxWaitTime))  //
                .socketTimeout(Integer.parseInt(socketTimeout))  //套接字超时时间，0无限制
//                .maxConnectionLifeTime(Integer.parseInt(maxConnectionLifeTime))  
                .connectTimeout(Integer.parseInt(connectTimeout))//连接超时，推荐>3000毫秒
                .build(); */ 
  
        //所有主机  
    	String address = PropertiesUtil.getProperty(Constants.MONGO_TEST_ADDRESS);
    	String userName = PropertiesUtil.getProperty(Constants.MONGO_TEST_USERNAME);
    	String database = PropertiesUtil.getProperty(Constants.MONGO_TEST_DATABASE);
    	String password = PropertiesUtil.getProperty(Constants.MONGO_TEST_PASSWORD);
        List<ServerAddress> hosts = new ArrayList<ServerAddress>();  
        hosts.add(new ServerAddress(address));  
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
        if (true) {  
            // 需要验证   pymongo.MongoClient("mongodb://root:ZGmongo001@10.10.43.213")
            MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());  
            credentials.add(credential);  
        }  
        mongoClient = new MongoClient(hosts, credentials);  
    }
    
    // -----------------------------------共用方法---------------------------------------------------
    
    /**
     * 
    * @Description: 获取DB实例 - 指定DB 
    * @author tony
    *
    * @time 2017-8-17
     */
    public MongoDatabase getDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);            
            return database;
        }
        return null;
    }

    /**
     * 
    * @Description: 获取collection对象 - 指定Collection 
    * @author tony
    *
    * @time 2017-8-17
     */
    public MongoCollection<Document> getCollection(String dbName, String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        DBObject query = null;
		DBObject options = null;
		mongoClient.getDB("").getCollection("").find(query, options);
        return collection;
    }

    
    /**
     * 查询DB下的所有表名
     */
    public List<String> getAllCollections(String dbName) {
        MongoIterable<String> colls = getDB(dbName).listCollectionNames();
        List<String> _list = new ArrayList<String>();
        for (String s : colls) {
            _list.add(s);
        }
        return _list;
    }

    /**
     * 获取所有数据库名称列表
     * 
     * @return
     */
    public MongoIterable<String> getAllDBNames() {
        MongoIterable<String> s = mongoClient.listDatabaseNames();
        return s;
    }

    /**
     * 删除一个数据库
     */
    public void dropDB(String dbName) {
        getDB(dbName).drop();
    }

    /**
     * 查找对象 - 根据主键_id
     * 
     */
    public Document findById(MongoCollection<Document> coll, String id) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Document myDoc = coll.find(Filters.eq("_id", _idobj)).first();
        return myDoc;
    }

    /** 统计数 */
    public int getCount(MongoCollection<Document> coll) {
        int count = (int) coll.count();
        return count;
    }

    /** 条件查询 */
    public MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
        return coll.find(filter).iterator();
    }

    /** 分页查询 */
    public MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
        Bson orderBy = new BasicDBObject("_id", 1);
        return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
    }

    /**
     * 通过ID删除
     */
    public int deleteById(MongoCollection<Document> coll, String id) {
        int count = 0;
        ObjectId _id = null;
        try {
            _id = new ObjectId(id);
        } catch (Exception e) {
            return 0;
        }
        Bson filter = Filters.eq("_id", _id);
        DeleteResult deleteResult = coll.deleteOne(filter);
        count = (int) deleteResult.getDeletedCount();
        return count;
    }

    /**
     * 
     * 通过id更新文档
     */
    public Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Bson filter = Filters.eq("_id", _idobj);
        // coll.replaceOne(filter, newdoc); // 完全替代
        coll.updateOne(filter, new Document("$set", newdoc));
        return newdoc;
    }
    
    public void dropCollection(String dbName, String collName) {
        getDB(dbName).getCollection(collName).drop();
    }
    
    /**
     * 关闭Mongodb
     */
    public void close() {
        if (null != mongoClient) {
            mongoClient.close();
            mongoClient = null;            
        }      
//        System.out.println("===============MongoDBUtil关闭连接========================");
        
    }


      public static void write(String HL7Msg)
      {
      String dbName = "testDBName";
      String collName = "testcollName";
       MongoCollection<Document> coll = instance.getCollection(dbName, collName);

      //插入
       Document doc = new Document();
       doc.put("MsgID", "MSH-10");
       doc.put("HL7Body", HL7Msg );
       coll.insertOne(doc);
       }

   /**
    * 
    * 测试入口
    */
    public static void main(String[] args) {
        
        String dbName = "testDBName";
        String collName = "testcollName";
        MongoCollection<Document> coll = instance.getCollection(dbName, collName);
        
//         //插入多条
//         for (int i = 1; i <= 4; i++) {
//             Document doc = new Document();
//             doc.put("name", "yisheng");
//             doc.put("school", "NEFU" + i);
//             Document interests = new Document();
//             interests.put("game", "game" + i);
//             interests.put("ball", "ball" + i);
//             doc.put("interests", interests);
//             coll.insertOne(doc);
//         }

         // 根据ID查询
         String id = "58caa7c21320c81bbcfc5593";
         Document doc = instance.findById(coll, id);
         System.out.println(doc);

//         //查询多个
//         MongoCursor<Document> cursor1 = coll.find(Filters.eq("name", "yisheng")).iterator();
//         while (cursor1.hasNext()) {
//         org.bson.Document _doc = (Document) cursor1.next();
//         System.out.println(_doc.toString());
//         }
//         cursor1.close();

           // 查询多个
           // MongoCursor<Person> cursor2 = coll.find(Person.class).iterator();

//         //删除数据库
//         MongoDBUtil.instance.dropDB("testDBName");

           //删除表
           //MongoDBUtil2.instance.dropCollection(dbName, collName);

//         //修改数据
//         String id = "58caa6041320c814f4520129";
//         Document newdoc = new Document();
//         newdoc.put("name", "龚lei");
//         MongoDBUtil.instance.updateById(coll, id, newdoc);
            
           //统计表
           //System.out.println(MongoDBUtil.instance.getCount(coll));
           
//         //查询所有
//         Bson filter = Filters.eq("count", 0);
//         MongoDBUtil.instance.find(coll, filter);
           
    }

}