package com.itszt.rpcserver.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

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

    @Override
    public void run() {
        new SendMessage().start();
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

    class SendMessage extends Thread {
        @Override
        public void run() {
            super.run();
            Scanner scanner = null;
            OutputStream os = null;
            String in = "";
            try {
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
