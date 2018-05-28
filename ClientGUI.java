/*********************************************************************
Purpose/Description: Write a program in Java to implement an efficient function.
Author’s Panther ID: 3816842
Certification:
I hereby certify that this work is my own and none of it is the work of
any other person.
********************************************************************/
package serverclient;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class ClientGUI {

    // GUI components
    JFrame frame;
    JPanel mainPanel;
    JButton sendButton;
    JButton exitButton;
    JButton portButton;
    JButton closeButton;
    JTextField enterPort;
    JTextField outgoing;
    JTextArea incoming;

    //Networking Components
    private Socket socket;
    Thread readerThread; 
    RecieveThread recieveThread;
    BufferedReader reader;
    PrintWriter writer;

    //Global Variables
    boolean isChatting = true;
    private int portNumber;

    /**
     * THIS METHOD WILL CREATE A GUI WINDOW
     */
    public void run() {
        frame = new JFrame("Chatroom Client");
        frame.setResizable(false);
        mainPanel = new JPanel();
        incoming = new JTextArea(15, 20);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        enterPort = new JTextField(20);
        outgoing = new JTextField(20);

        sendButton = new JButton("send");
        sendButton.addActionListener(e -> {
            //ACTION LISTENER
            writer.println(outgoing.getText());
            writer.flush();
            //Reset text after user send messages
            outgoing.setText("");
            outgoing.requestFocus();
        });

        closeButton = new JButton("Close Program");
        closeButton.addActionListener(e -> {
            //ACTION LISTENER
            System.exit(0);
        });
        portButton = new JButton("Connect");
        portButton.addActionListener(e -> {
            //ACTION LISTENER
            if(!isChatting){
                try{
                    portNumber = Integer.parseInt(enterPort.getText());
                    System.out.println("Client set port to: " + portNumber);
                    setPort(portNumber);
                    //Lunch a new connection thread
                    setUpNetworking();
                    new Thread(new RecieveThread()).start();
                     enterPort.setText("");
                }catch(NumberFormatException n){
                    System.out.println(n.getMessage());
                    incoming.append("PLEASE ENTER A FOUR DIGIT NUMBER\n"
                            + "Port: 4000 or 5000\n");
                }
            }
        });
        exitButton = new JButton("Exit Chat Room");
        exitButton.addActionListener(e -> {
            //ACTION LISTENER
            System.out.println("Client Disconnect...");
            incoming.append("You have closeded the channel...\n");
            writer.print("exit");
            writer.close();
            this.isChatting = false;
        });
        getPortNumber();
        setUpNetworking();

        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        mainPanel.add(enterPort);
        mainPanel.add(portButton);
        mainPanel.add(closeButton);
        mainPanel.add(exitButton);

        new Thread(new RecieveThread()).start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(400, 500);
        frame.setVisible(true);

    }
    
    void setPort(int portnumber){
        portNumber = portnumber;
    }
    /**
     * GETS PORT NUMBER 
     */
    void getPortNumber(){
        try{
                portNumber = Integer.parseInt(JOptionPane.showInputDialog(
                        new JFrame("WELCOME TO THE JAVA CHATROOMS"), "Current Chatroom:\n"
                    + "Port: 4000\n"
                    + "Port: 5000"));
        }catch(Exception e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(new JFrame(), "Failed Connection", "Networking Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    /**
     * THIS METHOD WILL SET UP ALL THE NETWORKING CONNECTIONS
     */
    void setUpNetworking() {
        try {
            socket = new Socket("localhost", portNumber);
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(new JFrame(), "Failed Connection", "Networking Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    //RECIEVETREAD INNER CLASS: LISTENS TO THE SERVER'S BROADCASTING REPLIES
    class RecieveThread implements Runnable {

        String echoRecieved;
   
        public void run() {
            isChatting = true;
            try {
                while ((echoRecieved = reader.readLine()) != null && isChatting) {
                    System.out.println("From Server: " + echoRecieved);
                    System.out.println("Please enter something to send to server..");
                    System.out.println(echoRecieved + "\n");
                    incoming.append(echoRecieved + "\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }//end run

        public String echoFromServer() {
            return this.echoRecieved;
        }
    }//end class recievethread


    /**
     * MAIN METHOD FOR THIS CLASS
     *
     * @param args
     */
    public static void main(String[] args) {
        new ClientGUI().run();
    }//end main method
}//end class
