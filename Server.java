/*********************************************************************
Purpose/Description: Write a program in Java to implement an efficient function.
Author’s Panther ID: 3816842
Certification:
I hereby certify that this work is my own and none of it is the work of
any other person.
********************************************************************/
package serverclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Joseph
 */
public class Server {

    Thread alphaChat;
    Thread betaChat;
    ChatroomAlpha alpha = new ChatroomAlpha(4000);

    public ChatroomAlpha getAlpha() {
        return alpha;
    }

    public ChatroomBeta getBeta() {
        return beta;
    }
    ChatroomBeta beta = new ChatroomBeta(5000);

    void runServer() {
        Thread alphaChat = new Thread(alpha);
        Thread betaChat = new Thread(beta);
        alphaChat.start();
        betaChat.start();
    }

    public static void main(String[] args) {
        new Server().runServer();
    }
}
