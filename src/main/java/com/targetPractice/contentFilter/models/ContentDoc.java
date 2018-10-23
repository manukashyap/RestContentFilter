package com.targetPractice.contentFilter.models;

import java.util.Map;

public class ContentDoc {

    Map<String,String> objectionableContentList;

    public Map<String,String> getObjectionableContentList() {
        return objectionableContentList;
    }

    public void setObjectionableContentList(Map<String,String> objectionableContentList) {
        this.objectionableContentList = objectionableContentList;
    }

    public ContentDoc() {

    }

    public ContentDoc(Map<String,String> objectionableContentList) {
        this.objectionableContentList = objectionableContentList;
    }
}
