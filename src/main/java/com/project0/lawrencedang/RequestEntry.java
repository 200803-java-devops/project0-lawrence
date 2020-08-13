package com.project0.lawrencedang;

public class RequestEntry {
    private int requestorID;
    private ClientRequest clientRequest;

    public RequestEntry(int requestorID, ClientRequest clientRequest) {
        this.requestorID = requestorID;
        this.clientRequest = clientRequest;
    }

    public int getRequestorID() {
        return requestorID;
    }


    public ClientRequest getClientRequest() {
        return clientRequest;
    }  
    
}