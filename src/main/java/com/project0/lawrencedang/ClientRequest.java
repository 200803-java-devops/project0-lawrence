package com.project0.lawrencedang;

public enum ClientRequest {
    GET_STATE, DO_SURRENDER, DO_HIT, DO_STAND, DO_DOUBLE, DISCONNECT, INVALID;

    public static ClientRequest fromString(String name)
    {
        for(ClientRequest cr: ClientRequest.values())
        {
            if (cr.name().equals(name))
            {
                return cr;
            }
        }
        return INVALID;
    }
}