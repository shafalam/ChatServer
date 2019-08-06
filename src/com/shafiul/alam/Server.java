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
    private ArrayList totalClients;
    //private PriorityQueue<String> totalMessages;
    ArrayList<String> totalMessages;
    private boolean startFlag;

    public void startServer(){
        totalClients = new ArrayList();
        //totalMessages = new PriorityQueue<>();
        totalMessages = new ArrayList<String>();

        try{
            ServerSocket serverSocket = new ServerSocket(5000);
            while(true){
                Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                totalClients.add(writer);
                new ClientReader(socket).start();
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
        public ClientReader(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run(){
            message = "";
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true ){
                    message = reader.readLine();
                    if(!message.equals(null)) {
                        tellEveryOne(message);
                        System.out.println("Message received: " + message);
                    }
                }

            }catch(IOException e){
                System.out.println("IOException occurred: " + e.getMessage());
            }

        }
    }

    public void tellEveryOne(String message){
        Iterator it = totalClients.iterator();
        while (it.hasNext()){
            try{
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
