package com.coderiders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainServer {

    private ServerSocket serverSocket;
    private final List<Socket> connections = new ArrayList<>();

    public void startServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            new Handler(serverSocket.accept()).start();
        }

    }

    private class Handler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public Handler(Socket socket) {
            connections.add(socket);
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if ("!bb".equals(inputLine)) {
                        out.println("good bye");
                        break;
                    }

                    for (var socket : connections) {
                        if (Boolean.FALSE.equals(socket.equals(clientSocket))) {
                            var outs = new PrintWriter(socket.getOutputStream(), true);
                            outs.println(inputLine);
                        }
                    }
                }
                close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        private void close() throws Exception {
            in.close();
            out.close();
            clientSocket.close();
            connections.remove(clientSocket);
        }
    }

    public static void main(String[] args) {
        MainServer server = new MainServer();
        try {
            server.startServer(9090);
        } catch (IOException e) {
            //Opps...
            System.out.println(e.getMessage());
        }
    }
}
