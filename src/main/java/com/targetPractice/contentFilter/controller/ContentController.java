package com.targetPractice.contentFilter.controller;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.targetPractice.contentFilter.models.ContentDoc;
import com.targetPractice.contentFilter.repository.ContentRepository;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content")
class ContentController {

    static ContentRepository repository = new ContentRepository();
    static ObjectMapper mapper = new ObjectMapper();
    static String contentDocumentKey = "OBJECTIONABLE_CONTENTS";

    @RequestMapping(path = "/add", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Response.Status addNewObjContent(HttpEntity<String> httpEntity) throws Exception {
        try {
            String newObjContent = httpEntity.getBody();
            ContentDoc docInDB;
          try {
              docInDB = repository.fetchData(contentDocumentKey);
          } catch (Exception e) {
              System.out.println("No document exists in DB, creating a new empty document");
              Map<String,String> emptyMap = new HashMap<>();
              docInDB = new ContentDoc(emptyMap);
          }
            String dateTime = Long.toString(System.currentTimeMillis());
            docInDB.getObjectionableContentList().put(newObjContent, dateTime);
            repository.insertData(docInDB, contentDocumentKey);
        }
        catch (Exception e) {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
        return Response.Status.OK;
    }

    @RequestMapping(path = "/check", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public String checkContent (HttpEntity<String> httpEntity) {
        try {
            String content = httpEntity.getBody();
            ContentDoc docInDB = repository.fetchData(contentDocumentKey);
            List<String> wordsInContent = Arrays.asList(content.split(" "));
            Map<String,String> objContent = docInDB.getObjectionableContentList();
            for(String word : wordsInContent ) {
                if(objContent.containsKey(word))
                    return "Objectionable content";
            }
        }
        catch (Exception e) {
            return "Internal Server error";
        }
        return "Normal content";
    }

//    @RequestMapping(path = "/viewAll", method = RequestMethod.GET)
//    public Response getAllObjContent() {
//
//    }
}
