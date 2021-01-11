package dataBaseConnection;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import okio.Options;

public class AtlasMongoDBConnection {
	
	public static MongoDatabase database;
	public static String currentDate;
	public static String collectionName;
	public AtlasMongoDBConnection() {

		try {
			System.setProperty("jdk.tls.trustNameService", "true");

			MongoClientURI uri = new MongoClientURI(
					"mongodb://dbHulk:dbHulk@cluster0-shard-00-00.zpedc.mongodb.net:27017,cluster0-shard-00-01.zpedc.mongodb.net:27017,cluster0-shard-00-02.zpedc.mongodb.net:27017/testdashboard?ssl=true&replicaSet=atlas-wcoewv-shard-0&authSource=admin&retryWrites=true&w=majority");

			MongoClient mongoClient = new MongoClient(uri);
			database = mongoClient.getDatabase("testdashboard");
			
			System.out.println("connected");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("mongo connection exception");
		}

	}
	
	public void createCollection(String collectionName, Set<String> testCaseClassNames) {
		MongoIterable<String> collectionNames = database.listCollectionNames();
		this.collectionName = collectionName;
		boolean isCollectionExisting = false;
		for(String name:collectionNames) {
			if(name.equalsIgnoreCase(collectionName)) {
				isCollectionExisting = true;
				break;
			}
		}
		if(!isCollectionExisting) {
			database.createCollection(collectionName);
		}
		
//		List<Document> listOfTestClasses = new ArrayList<Document>();
		LinkedHashMap<String, Document> listOfTestClasses = new LinkedHashMap<String, Document>();
		for(String testCaseClassName : testCaseClassNames) {
			testCaseClassName = testCaseClassName.replace(".", "_");
//			Document testcaseClassNameDocument = new Document(testCaseClassName, new Document("status","-1"));
//			listOfTestClasses.add(testcaseClassNameDocument);
			
			listOfTestClasses.put(testCaseClassName, new Document("status","-1"));
		}
		MongoCollection<Document> collection = database.getCollection(collectionName);
		
		java.util.Date date=new java.util.Date();  
		System.out.println(date);
		currentDate = date.toString();
		Document document1 = new Document();
		document1.append("date_and_time", currentDate);
		document1.append("list_of_testClasses", listOfTestClasses);
		
		collection.insertOne(document1);
		
		System.out.println("document created");
	}

	public void addTestCaseDataToSuiteDocument(String testCaseName, String testMethodName, String status) {
		// TODO Auto-generated method stub
		
		try {
			LinkedHashMap<String, String> statusMap = new LinkedHashMap<String, String>();
			statusMap.put("status", status);
			Document statusDocument = new Document("status", status);
			Document methodNameDocument = new Document(testMethodName, statusMap);
			
			Bson filter = Filters.eq("date_and_time", currentDate);
		Bson update = Updates.set("list_of_testClasses."+testCaseName+"."+testMethodName, statusDocument);
//			Bson update = Updates.set("list_of_testClasses."+testCaseName, "2");
//			Bson set = new Document().append("$set", update);
		
//		update.put("$set", new BasicDBObject("data.2", new BasicDBObject("info",info).append("info1",info)); 
			
			MongoCollection<Document> collection = database.getCollection(collectionName);
			
			
//			BsonDocument bsonDocument = filter.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
//			System.out.println(bsonDocument);
			FindIterable<Document> found = collection.find(filter);
			for(Document doc: found) {
				System.out.println(doc.toJson());
				Object value = doc.get("list_of_testClasses");
				System.out.println(value.toString());
			}
			FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().upsert(true);
			collection.findOneAndUpdate(filter, update, options);
			System.out.println("updated");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addTestCaseStatusSuiteDocument(String testCaseName, String status) {
		// TODO Auto-generated method stub
		
		try {
			Document statusDocument = new Document("status", status);
			
			Bson filter = Filters.eq("date_and_time", currentDate);
			Bson update = Updates.set("list_of_testClasses."+testCaseName+".status", statusDocument);
			
			MongoCollection<Document> collection = database.getCollection(collectionName);

			FindIterable<Document> found = collection.find(filter);
			for(Document doc: found) {
				System.out.println(doc.toJson());
				Object value = doc.get("list_of_testClasses");
				System.out.println(value.toString());
			}
			FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().upsert(true);
			collection.findOneAndUpdate(filter, update, options);
			System.out.println("updated");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
