package com.example.narupak.myapplication;

import java.util.List;

public class GenericRequest<T> {
    private T request;
    private List<T> requests;

//    @JsonCreator
//    public GenericRequest(T request, List<T> requests) {
//        this.request = request;
//        this.requests = requests;
//    }
//
//    @JsonCreator
//    public GenericRequest(T request) {
//        this.request = request;
    //}


    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }

    public List<T> getRequests() {
        return requests;
    }

    public void setRequests(List<T> requests) {
        this.requests = requests;
    }
}
