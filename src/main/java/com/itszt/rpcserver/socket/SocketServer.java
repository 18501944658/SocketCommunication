package com.itszt.rpcserver.socket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 服务端
 */
@Slf4j
public class SocketServer extends Thread {

    ServerSocket serverSocket = null;

    Socket socket = null;


    public SocketServer(Integer port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 服务端监听连接得客户端,并不断读取客户端的数据输入
     */
    @SneakyThrows
    @Override
    public void run() {
        super.run();

        log.info("等待客户端链接。。。。。。。。");

        try {
            socket = serverSocket.accept();
            new SendMessageServer().start();
            log.info("客户端(" + socket.getInetAddress().getHostAddress() + ") 连接成功。。。。");
            InputStream in = socket.getInputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                log.info("客户端：(" + socket.getInetAddress().getHostAddress() + ")说：" + new String(buf, 0, len, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 服务端不断响应数据给客户端，输出流
     */
    class SendMessageServer extends Thread {
        @Override
        public void run() {
            super.run();
            Scanner scanner = null;
            OutputStream out = null;
            try {
                if (socket != null) {
                    scanner = new Scanner(System.in);
                    out = socket.getOutputStream();
                    String in = "";
                    do {
                        in = scanner.next();
                        out.write(("" + in).getBytes("UTF-8"));
                        out.flush();
                    } while (!in.equals("q"));
                    scanner.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer(1234);
        socketServer.start();
    }
}
