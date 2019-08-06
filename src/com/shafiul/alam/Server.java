package com.shafiul.alam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Server {
    private ArrayList<PrintWriter> totalClients;
    //private PriorityQueue<String> totalMessages;
    ArrayList<String> totalMessages;
    private boolean startFlag;

    public void startServer(){
        totalClients = new ArrayList<PrintWriter>();
        //totalMessages = new PriorityQueue<>();
        totalMessages = new ArrayList<String>();

        try{
            ServerSocket serverSocket = new ServerSocket(5000);
            int socketId = 0;
            while(true){
                Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                totalClients.add(writer);
                ++socketId;
                new ClientReader(socket, socketId).start();
                System.out.println("Got a connection");
            }
        }catch(IOException e){
            System.out.println("IOException occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    class ClientReader extends Thread{
        Socket socket;
        String message;
        int socketId;
        public ClientReader(Socket socket, int socketId){
            this.socket = socket;
            this.socketId = socketId;
        }
        @Override
        public void run(){
            message = "";
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true ){
                    message = reader.readLine();
                    if(!message.equals(null)) {
                        tellEveryOne(message, socketId);
                        System.out.println("Message received from client "+ socketId + ": " + message);
                    }
                }

            }catch(IOException e){
                System.out.println("IOException occurred: " + e.getMessage());
            }

        }
    }

    public void tellEveryOne(String message, int clientId){
        Iterator it = totalClients.iterator();
        while (it.hasNext()){
            try{
                PrintWriter writer = (PrintWriter) it.next();
                writer.println("Friend: " + clientId + ": " + message);
                writer.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
