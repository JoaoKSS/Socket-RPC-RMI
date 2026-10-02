package org.ifnmg.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente de console para calculadora distribuída via Sockets TCP.
 * 
 * @author JoaoKSS
 * @author andref03
 */
public class ClienteSocket {
    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 5000;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : HOST_PADRAO;
        int porta = args.length > 1 ? Integer.parseInt(args[1]) : PORTA_PADRAO;

        System.out.println("Conectando ao servidor em " + host + ":" + porta + "...");

        try (Socket socket = new Socket(host, porta);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectado com sucesso!");
            System.out.println("Use: SOMA|SUB|MULT|DIV <num1> <num2> ou SAIR");

            while (true) {
                System.out.print("> ");
                String comando = scanner.nextLine();

                if (comando.trim().isEmpty()) {
                    continue;
                }

                if (comando.trim().equalsIgnoreCase("SAIR")) {
                    out.println(comando);
                    System.out.println("Encerrando cliente Socket.");
                    break;
                }

                // Envia ao servidor
                out.println(comando);

                // Le resposta do servidor
                String resposta = in.readLine();
                if (resposta == null) {
                    System.out.println("Servidor encerrou a conexao.");
                    break;
                }

                if (resposta.startsWith("RESULTADO: ")) {
                    System.out.println("Servidor respondeu: " + resposta.substring(11));
                } else {
                    System.out.println(resposta);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao comunicar com o servidor: " + e.getMessage());
        }
    }
}
