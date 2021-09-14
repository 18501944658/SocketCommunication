package com.itszt.rpcserver.socket;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 一个客户端
 */
@Slf4j
public class SocketClientOne extends Thread {

    private Socket socket = null;

    public SocketClientOne(String ip, int port) {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /***
     * 监听服务器中响应的数据输入流
     */
    @Override
    public void run() {
        super.run();
        new ClientMessage().start();
        try {
            log.info("客户端开始接口服务器响应。。。。。。");
            InputStream in = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                log.info("服务端发来消息：【{}】", new String(buf, 0, len, "utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class ClientMessage extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                OutputStream out = socket.getOutputStream();
                Scanner scanner = new Scanner(System.in);
                String in = "";
                do {
                    in = scanner.next();
                    out.write(in.getBytes());
                    out.flush();
                } while (!in.equals("bye"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SocketClientOne socketClientOne= new SocketClientOne("127.0.0.1",1234);
        socketClientOne.start();
    }
}
