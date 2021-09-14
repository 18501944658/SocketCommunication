package com.itszt.rpcserver.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

@Slf4j
public class ScoketServerOne extends Thread {

    private ServerSocket serverSocket = null;

    private Socket socket = null;

    public ScoketServerOne(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端需要监听来自客户端的通话输入流
     */
    @Override
    public void run() {
        super.run();
        log.info("服务端启动成功,等待客户端的连接。。。。。");
        try {
            socket = serverSocket.accept();
            log.info("客户端连接成功,【{}】", socket.getInetAddress());
            new ServerMessage().start();
            InputStream inputStream = socket.getInputStream();

            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf)) != -1) {
                log.info("客户端发送的消息为:【{}】", new String(buf, 0, len, "utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class ServerMessage extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                OutputStream outputStream = socket.getOutputStream();
                Scanner scanner = new Scanner(System.in);
                String in = "";
                do {
                    in = scanner.next();
                    outputStream.write(in.getBytes());
                    outputStream.flush();
                } while (in.equals("bye"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        ScoketServerOne scoketServerOne = new ScoketServerOne(1234);
        scoketServerOne.start();
    }
}
