package com.example.project_2;

import java.net.Socket;

public class Client {

        public static void main(String []args) throws Exception {
            Socket socket = new Socket("192.168.0.19",8888);
            System.out.println("connected to a Server");
        }

}
