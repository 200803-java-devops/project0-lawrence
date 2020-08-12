package com.project0.lawrencedang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommunicationHandlerTest {
    ThreadCommunicationChannel commChannel;
    ServerSocket server;

    @Before
    public void setup()
    {
        commChannel = new ThreadCommunicationChannel();
        try
        {
            server = new ServerSocket(9999);
        }
        catch(IOException e)
        {
            System.err.println("Could not open server socket.");
        }
        
    }

    @After
    public void cleanup()
    {
        try
        {
            server.close();
        }
        catch(IOException e)
        {
            System.err.println("Failed to close server socket.");
        }
    }

    public abstract class CommunicationHandlerTestClient implements Runnable
    {
        Socket clientSocket;
        public boolean success;
        public String msg;
        public CommunicationHandlerTestClient()
        {
            clientSocket = null;
            success = false;
            msg = "";
        }

        protected abstract void helperCode();

        public void run()
        {
            this.connect();
            helperCode();
        }

        private void connect()
        {
            Socket tempSocket = null;
            while(clientSocket == null)
            {
                try
                {
                    tempSocket = new Socket("localhost", 9999);
                } 
                catch (IOException e) 
                {
                    try
                    {
                        Thread.sleep(200);
                    }
                    catch (InterruptedException i)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
                clientSocket = tempSocket;
            }   
        }

        protected void waitUntilReady (BufferedReader reader) throws InterruptedException, IOException
        {
            while(!reader.ready())
                {
                    Thread.sleep(100);
                }
        }
    }

    public class SendsCorrectStateHelper extends CommunicationHandlerTestClient
    {
        protected void helperCode()
        {
            BufferedReader bufferedReader = null;
            try
            {
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                waitUntilReady(bufferedReader);
                bufferedReader.readLine(); // Greeting
                waitUntilReady(bufferedReader);
                String stateMessage = bufferedReader.readLine();
                String messageType = stateMessage.split("\\|")[0];
                String stateKV = stateMessage.split("\\|")[1];
                String dealerHandString = stateKV.split("/")[0].split(":")[1];
                Card[] cards = new Gson().fromJson(dealerHandString, Card[].class);
                if(messageType.equals("STATE") && cards[0] == Card.Ten)
                {
                    this.success = true;
                }
                this.msg = stateMessage;
                
            }
            catch (IOException e)
            {
                System.err.println("Problem reading socket input stream.");
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    public class PutsCorrectOptionHelper extends CommunicationHandlerTestClient
    {
        protected void helperCode()
        {
            BufferedReader bufferedReader = null;
            PrintStream printStream = null;
            try 
            {
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                printStream = new PrintStream(clientSocket.getOutputStream());
                waitUntilReady(bufferedReader);
                bufferedReader.readLine(); // Greeting
                waitUntilReady(bufferedReader);
                bufferedReader.readLine(); // State message
                waitUntilReady(bufferedReader);
                String ready = bufferedReader.readLine().trim(); // READY
                if (ready.split("\\|")[0].equals("READY"))
                {
                    printStream.println(5);
                }
            } 
            catch (IOException e) 
            {
                System.err.println("Problem reading socket input stream.");
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @Test 
    public void generatesCorrectStateString()
    {
        GameState state = new GameState();
        state.addPlayerHand(Card.Four);
        state.addPlayerHand(Card.Ace);
        String stateString = CommunicationHandler.generateStateString(state);
        String[] stateKV = stateString.split("\\|")[1].trim().split("/");
        System.out.println(stateKV[2]);
        assertTrue((stateKV[0].split(":")[0].equals("DEALER_HAND") && stateKV[0].split(":")[1].equals("[]")));
        assertTrue((stateKV[1].split(":")[0].equals("PLAYER_HAND") && stateKV[1].split(":")[1].equals("[\"Four\",\"Ace\"]")));
        assertTrue((stateKV[2].split(":")[0].equals("PLAYER_STATE") && stateKV[2].split(":")[1].equals("\"PLAYING\"")));
        assertTrue((stateKV[3].split(":")[0].equals("END_STATE") && stateKV[3].split(":")[1].equals("\"NA\"")));

    }

    @Test(timeout = 5000)
    public void sendsCorrectState() throws IOException, InterruptedException
    {
        GameState state = new GameState();
        state.addDealerHand(Card.Ten);
        commChannel.putState(state);
        SendsCorrectStateHelper helper = new SendsCorrectStateHelper();
        Thread helperThread = new Thread(helper);
        helperThread.start();
        Socket socket = server.accept();
        CommunicationHandler handler = new CommunicationHandler(socket, commChannel);
        new Thread(handler).start();
        helperThread.join();
        assertTrue(helper.success);
    }

    @Test(timeout = 5000)
    public void putsCorrectOption() throws IOException, InterruptedException
    {
        GameState state = new GameState();
        commChannel.putState(state);
        PutsCorrectOptionHelper helper = new PutsCorrectOptionHelper();
        Thread helperThread = new Thread(helper);
        helperThread.start();
        Socket socket = server.accept();
        CommunicationHandler handler = new CommunicationHandler(socket, commChannel);
        Thread handlerThread = new Thread(handler);
        handlerThread.start();
        helperThread.join();
        int option = commChannel.takeOption();
        assertEquals(option, 5);
    }
}