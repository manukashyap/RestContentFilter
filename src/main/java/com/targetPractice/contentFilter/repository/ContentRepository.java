package com.targetPractice.contentFilter.repository;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.transcoder.JsonTranscoder;
import com.targetPractice.contentFilter.models.ContentDoc;

import java.util.Map;

public class ContentRepository {

    private Bucket bucket;
    static ObjectMapper mapper = new ObjectMapper();

    public ContentRepository() {
        Cluster cluster = CouchbaseCluster.create("127.0.0.1");
        Bucket localBucket = cluster.openBucket("ContentBucket","test123");
        this.bucket = localBucket;
    }


    public JsonObject convertToJson(String inputString) throws Exception{
        JsonTranscoder transcoder = new JsonTranscoder();
        JsonObject jsonObject = transcoder.stringToJsonObject(inputString);
        return jsonObject;
    }

    public void insertData(ContentDoc contentDoc, String documentKey) throws Exception{
        String inputString = mapper.writeValueAsString(contentDoc);
        JsonObject jsonToUpsert = convertToJson(inputString);
        JsonDocument documentToUpsert = JsonDocument.create(documentKey,jsonToUpsert);
        bucket.upsert(documentToUpsert);
    }

    public ContentDoc fetchData(String documentKey) throws Exception {
        JsonDocument document = bucket.get(documentKey);
        String docContent = document.content().toString();
        JsonTranscoder transcoder = new JsonTranscoder();
        Map<String,Object> docInDB = mapper.readValue(docContent,Map.class);
        ContentDoc contentDoc = new ContentDoc((Map<String,String>)docInDB.get("objectionableContentList"));
        return contentDoc;
    }
}
