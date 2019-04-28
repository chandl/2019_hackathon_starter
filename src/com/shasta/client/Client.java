package com.shasta.client;

import com.shasta.img.Img2Ascii;
import com.shasta.threaded.ClientRunnable;
import com.shasta.threaded.MultiThreadedServer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;


/**
 * Client object that contains methods to handle connects, disconnects, and messages for one client.
 *
 * @author Chandler Severson
 * @since 2019-12-10
 * <p>Made as a starter project for the 2019 Shasta Networks/SOU CS Club Hackathon.</p>
 */
public class Client extends ClientRunnable {

    private static String prompt = "==>";
    private int desiredWidth = 350;


    public Client(Socket clientSocket) {
        super(clientSocket);
        System.out.println(getClientSocket().getLocalAddress() + ": New Connection. Port: " + getClientSocket().getPort());
    }

    /**
     * Called when a client connects.
     */
    @Override
    public void handleConnect() {
        try {
            sendMessage("Welcome! Please enter an image URL: \n" + prompt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when a client disconnects
     */
    @Override
    public void handleDisconnect() {
        MultiThreadedServer.removeClient(this);
    }

    /**
     * Handle client input.
     * @param str The message to handle.
     */
    @Override
    protected void handleMessage(String str) {

        if(str.startsWith("/")){
            handleCommand(str);
            return;
        }

        try {
            URL url = new URL(str);
            Img2Ascii ascii = new Img2Ascii();

            sendMessage("Converting image to ASCII!\n");

            String out = ascii.convertToAscii(url, desiredWidth, false);
            sendMessage(out);

        } catch (MalformedURLException e) {
            try {
                sendMessage("Bad URL Entered: " + str + ".\n" );
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            try {
                sendMessage("Error Converting URL to ASCII. Try another.\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        try {
            sendMessage(prompt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCommand(String str) {
        String[] strArr = str.split(" ");
        switch(strArr[0]){
            case "/quit":
                try {
                    getClientSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "/width":
                try {
                    if(strArr.length > 1){
                        try {
                            desiredWidth = Integer.parseInt(strArr[1]);
                            sendMessage("Successfully set image width to " + desiredWidth + ".\n");
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        sendMessage("No width defined, keeping image width at " + desiredWidth + ".\n");
                    }

                    sendMessage(prompt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    sendMessage("Command Not Recognized: " + str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
