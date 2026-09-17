/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.ifnmg.sockets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author joao-kennedy
 */

public class ClienteSocket {
    public static final String HOST = "localhost";
    public static final int PORTA = 5000;

    public static void main(String[] args) {
        System.out.println("Conectando ao servidor em " + HOST + ":" + PORTA + "...");

        try (Socket socket = new Socket(HOST, PORTA);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectado! Digite comandos no formato: SOMA 10 20 (ou 'SAIR')");

            while (true) {
                System.out.print("> ");
                String comando = scanner.nextLine();

                if (comando.trim().isEmpty()) {
                    continue;
                }

                // Envia ao servidor
                out.println(comando);

                // Lê resposta do servidor
                String resposta = in.readLine();
                System.out.println("Servidor respondeu: " + resposta);

                if (comando.equalsIgnoreCase("SAIR")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao comunicar com o servidor: " + e.getMessage());
        }
    }
}
