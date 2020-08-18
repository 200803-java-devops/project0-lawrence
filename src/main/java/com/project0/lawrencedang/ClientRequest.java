package com.project0.lawrencedang;

/**
 * ClientRequest is used internally by CommunicationHandler and Game to allow CommunicationHandler to
 * pass user input to the Game.
 */
public enum ClientRequest {
    GET_STATE, DO_SURRENDER, DO_HIT, DO_STAND, DO_DOUBLE, DISCONNECT, INVALID;

    /**
     * Returns the ClientRequest value that corresponds to the string, returning INVALID if there is no match.
     * @param name a string possibly corresponding to a ClientRequest value
     * @return The corresponding ClientRequest value, or INVALID if there is no match.
     */
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