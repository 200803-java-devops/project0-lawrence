package com.project0.lawrencedang;

/**
 * Class to wrap ClientRequests that are placed in the ThreadCommunication channel.
 * RequestEntry stores information about the client that made the request, along with the ClientRequest itself.
 */
public class RequestEntry {
    private int requestorID;
    private ClientRequest clientRequest;

    /**
     * Create a new RequestEntry associated with the specified client id and ClientRequest
     * @param requestorID the id of the requesting client.
     * @param clientRequest the type of request.
     */
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