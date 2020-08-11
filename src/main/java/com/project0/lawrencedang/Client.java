package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import com.google.gson.Gson;

public class Client {


    private BufferedReader reader;
    private PrintStream writer;
    private BufferedReader userReader;
    private GameState state;
    public Client(BufferedReader br, PrintStream ps, BufferedReader uinput)
    {
        reader = br;
        writer = ps;
        state = null;
        userReader = uinput;
    }

    public void run() throws IOException
    {
        System.out.println(reader.readLine());
        while(true)
        {
            String[] msgArray = readServerResponse();
            if(msgArray[0].equals("MSG"))
            {
                System.out.println(msgArray[1]);
            }
            else if(msgArray[0].equals("STATE"))
            {
                updateState(msgArray[1]);
                renderState();
                if(checkGameEnded())
                {
                    printEndMessage();
                    System.out.print("Press enter to exit...");
                    System.in.read();
                    break;
                }
            }
            else if(msgArray[0].equals("READY"))
            {
                String option = promptUserInput();
                writer.println(option);
            }
        }
    }

    private String[] readServerResponse() throws IOException
    {
        return reader.readLine().split("\\|");
    }

    private String promptUserInput() throws IOException
    {
        char option = (char)0;
        while(option != '1' && option != '2')
        {
            System.out.print("Options\n1: Hit\n2:Stand\nPlease select an option: ");
            int userInput = userReader.read();
            while(userReader.ready())
            {
                userReader.readLine();
            }
            option = (char)userInput;
            if(option != '1' && option !='2')
            {
                System.out.println("That is not a valid option. Please try again.\n\n");
            }
        }
        return String.valueOf(option);
    }

    private void updateState(String stateString)
    {
        String[] stateKVs = stateString.split("/");
        String[] stateVals = new String[stateKVs.length];
        for(int i = 0; i<stateKVs.length; i++)
        {
            stateVals[i] = stateKVs[i].split(":")[1];
        }
        GameState newState = new GameState();
        Gson deserializer = new Gson();
        Card[] dealerHand = deserializer.fromJson(stateVals[0], Card[].class);
        Card[] playerHand = deserializer.fromJson(stateVals[1], Card[].class);
        for(Card card: dealerHand){newState.addDealerHand(card);}
        for(Card card: playerHand){newState.addPlayerHand(card);}
        newState.setPlayerState(deserializer.fromJson(stateVals[2], PlayerState.class));
        newState.setEndState(deserializer.fromJson(stateVals[3], EndState.class));
        this.state = newState;
    }

    private void renderState()
    {
        String renderTemplate = "Dealer's cards: %s\tTotal: %d(%d)\nYour cards: %s\tTotal:%d(%d)\n";
        ArrayList<Card> dealerHand = state.getDealerHand();
        ArrayList<Card> playerHand = state.getPlayerHand();
        String output = String.format(renderTemplate, displayHand(dealerHand), Card.lowValueOf(dealerHand), Card.highValueOf(dealerHand),
        displayHand(playerHand), Card.lowValueOf(playerHand), Card.highValueOf(playerHand));
        System.out.println(output);
        System.out.println();
    }

    private String displayHand(ArrayList<Card> cards)
    {
        String[] cardNames = new String[cards.size()];
        for(int i = 0; i<cards.size(); i++)
        {
            cardNames[i] = cards.get(i).name();
        }
        return String.join(", ", cardNames);
    }

    private boolean checkGameEnded()
    {
        return state.getEndState() != EndState.NA;
    }

    private void printEndMessage()
    {
        switch(state.getEndState())
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