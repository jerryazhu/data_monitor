package com.qa.data.visualization.entities.jishu.web;

/**
 * Created by dykj on 2016/12/19.
 */
public class WebCrmActionGroupCount {
    private String controller;
    private String method;
    private String count;

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
