package ua.kpi.parallels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Socket clientSocket; // сокет клієнта
    private PrintWriter out; // стрім запису у сокет
    private BufferedReader in; //стрім зчитування з сокету
    private BufferedReader reader; //стрім зчитування з консолі

    public final int PORT = 7777;
    public final String IP = "127.0.0.1";

    public Client() {
        run(); //створюємо сокет на заданому ip та порті
    }

    private void run() {
        try {
            startConnection();
            try {
                String msg;
                if ((msg = in.readLine()) != null) {
                    System.out.println("Connected to server");
                    System.out.println(msg); //зчитуємо відповідь сервера після підключення до нього
                    while (!(msg = reader.readLine()).equals("exit")) {//зчитуємо ввод користувача з консолі
                        sendMessage(msg);//передаємо повідомлення клієнта в метод, що записує повідомлення у сокет і зчитує з нього відповід сервера
                    }
                } else {
                    System.out.println("No server response");
                }
            } catch (IOException e) {
                System.out.println("I/O mistake");
            } finally {
                stopConnection(); //зупиняємо з'єднання
                System.out.println("Connection stopped");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startConnection() throws IOException {
        clientSocket = new Socket(IP, PORT); //створюємо сокет клієнта
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    private void sendMessage(String msg) {
        out.println(msg); //записуємо повідомлення у сокет
        String resp;
        try {
            while (!(resp = in.readLine()).equals("end")) {//очікуємо відповіді від серверу (зчитуємо його запис у сокет)
                System.out.println(resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopConnection() throws IOException {
        out.println("exit");
        in.close();
        reader.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) {
        new Client();
    }
}

