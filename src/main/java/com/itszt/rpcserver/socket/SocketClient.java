package com.itszt.rpcserver.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/***
 * 客户端
 */
@Slf4j
public class SocketClient extends Thread {

    Socket socket = null;

    public SocketClient(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /****
     *
     *  客户端线程进行输入流得读取,即客户端发送来得消息得进行循环读取
     */
    @Override
    public void run() {
        new SendMessageClient().start();
        super.run();
        try {
            InputStream s = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = s.read(buf)) != -1) {
                log.info("服务器说：" + new String(buf, 0, len, "utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*****
     * 客户端写出数据到客户端实时监听
     */
    class SendMessageClient extends Thread {
        @Override
        public void run() {
            super.run();
            Scanner scanner = null;
            OutputStream os = null;
            String in = "";
            try {
                scanner = new Scanner(System.in);
                os = socket.getOutputStream();
                do {
                        in = scanner.next();
                            os.write(("" + in).getBytes());
                        os.flush();

                } while (!in.equals("bye"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanner.close();
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SocketClient socketClient = new SocketClient("127.0.0.1", 1234);
        socketClient.start();
    }
}
