package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class Client {


    private BufferedReader reader;
    private PrintStream writer;
    private BufferedReader userReader;
    private GameStateView state;
    private int id;
    public Client(BufferedReader br, PrintStream ps, BufferedReader uinput)
    {
        reader = br;
        writer = ps;
        state = null;
        userReader = uinput;
        id = -1;
    }

    public void run() throws IOException
    {
        waitForStart();
        boolean turnEnd = false;
        ClientServerProtocol.waitForReady(reader);
        writer.println("GET_STATE");
        processStateMessage();
        renderState();
        while(turnEnd == false)
        {
            String uOption = promptUserInput();
            ClientServerProtocol.waitForReady(reader);
            if(uOption.equals("DO_HIT"))
            {
                writer.println("DO_HIT");
                expectReceived();
                ClientServerProtocol.waitForReady(reader);
                writer.println("GET_STATE");
            }
            else if (uOption.equals("DO_STAND"))
            {
                writer.println("DO_STAND");
                expectReceived();
                ClientServerProtocol.waitForReady(reader);
                writer.println("GET_STATE");
            }
            else
            {
                writer.println("GET_STATE");
            }
            processStateMessage();
            renderState();
            if(checkTurnEnded())
            {
                if(state.playerStates[id] == PlayerState.BUST)
                {
                    System.out.println("Bust!");
                }
                System.out.println("Please wait for the other players to finish.");
                turnEnd = true;
            }
        }
        waitForEnd();
        printEndMessage();
        renderState();
        writer.println("DISCONNECT");
    }

    private void waitForEnd() throws IOException
    {
        while(state.endStates[id] == EndState.NA)
        {
            ClientServerProtocol.waitForReady(reader);
            writer.println("GET_STATE");
            processStateMessage();
            try
            {
                Thread.sleep(2000);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean expectReceived() throws IOException
    {
        String message = reader.readLine();
        if(ClientServerProtocol.getType(message).equals("RECEIVED"))
        {
            return true;
        }
        else
        {
            System.err.println("Expected RECEIVED| but got "+message);
            return false;
        }
    }

    private void waitForStart() throws IOException
    {
        String serverMsg = "";
        while(!ClientServerProtocol.getType(serverMsg).equals("BEGIN"))
        {
            serverMsg = reader.readLine();
            System.out.println(ClientServerProtocol.getMessage(serverMsg));
        }
        this.id = Integer.valueOf(ClientServerProtocol.getMessage(serverMsg));
    }

    private void processStateMessage() throws IOException
    {
        String response = reader.readLine();
        String type = ClientServerProtocol.getType(response);
        String message = ClientServerProtocol.getMessage(response);
        if(type.equals("STATE"))
        {
            updateState(message);
        }
        else
        {
            System.err.println("Unexpected server response: " + type +"|" + message);
        }
        
    }

    private String promptUserInput() throws IOException
    {
        char option = (char)0;
        while(option != '1' && option != '2' && option != '3')
        {
            System.out.print("Options\n1: Hit\n2: Stand\n3: Refresh\nPlease select an option: ");
            int userInput = userReader.read();
            //Clear input
            while(userReader.ready())
            {
                userReader.readLine();
            }
            option = (char)userInput;
            if(option != '1' && option !='2' && option != '3')
            {
                System.out.println("That is not a valid option. Please try again.\n\n");
            }
        }
        if(option == '1')
        {
            return "DO_HIT";
        }
        else if (option == '2')
        {
            return "DO_STAND";
        }
        else
        {
            return "GET_STATE";
        }

    }

    private void updateState(String stateString)
    {
        Gson deserializer = new Gson();
        this.state = deserializer.fromJson(stateString, GameStateView.class);
    }

    private void renderState()
    {
        String renderTemplate = "Dealer's cards: %s\tTotal: %d(%d)\nYour cards: %s\tTotal:%d(%d)\n";
        List<Card> dealerHand = Arrays.asList(state.dealerHand);
        List<Card> playerHand = Arrays.asList(state.playerHands[id]);
        String output = String.format(renderTemplate, displayHand(dealerHand), Card.lowValueOf(dealerHand), Card.highValueOf(dealerHand),
        displayHand(playerHand), Card.lowValueOf(playerHand), Card.highValueOf(playerHand));
        System.out.println(output);
        System.out.println();
    }

    private String displayHand(List<Card> cards)
    {
        String[] cardNames = new String[cards.size()];
        for(int i = 0; i<cards.size(); i++)
        {
            cardNames[i] = cards.get(i).name();
        }
        return String.join(", ", cardNames);
    }

    private boolean checkTurnEnded()
    {
        return state.playerStates[id] != PlayerState.PLAYING;
    }

    private void printEndMessage()
    {
        switch(state.endStates[id])
        {
            case BLACKJACK:
                System.out.println("You win!");
                break;
            case WIN:
                System.out.println("You win!");
                break;
            case TIE:
                System.out.println("Draw!");
                break;
            case LOSE:
                System.out.println("You lose...");
                break;
            case NA:
                System.out.println("This is a bug.");
                break;
        }
    }

    
    
}