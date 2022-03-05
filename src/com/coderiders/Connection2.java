package com.coderiders;

import java.io.IOException;
import java.util.Scanner;

public class Connection2 {

    public static void main(String[] args) {
        var client = new PingClient();

        try {
            client.startClient("127.0.0.1", 9090);
            var scanner = new Scanner(System.in);
            System.out.println("Введите сообщение:");
            String ms;
            while (scanner.hasNextLine()) {
                ms = scanner.nextLine();
                String response = client.sendMessage(ms);
                System.out.println(response);

                if ("good bye".equals(response)) {
                    break;
                }
            }

            client.stopConnection();

        } catch (IOException e) {
            //Opps...
            System.out.println(e.getMessage());
        }
    }
}