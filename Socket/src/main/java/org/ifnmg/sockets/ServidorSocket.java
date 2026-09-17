package org.ifnmg.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author joao-kennedy
 */

public class ServidorSocket {
    public static final int PORTA = 5000;

    public static void main(String[] args) {
        System.out.println("Iniciando servidor de socket na porta " + PORTA + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Aguardando conexão de cliente...");

            // Fica bloqueado aqui até um cliente conectar
            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                String requisicao;
                // Lê linhas enviadas pelo cliente até ele fechar a conexão
                while ((requisicao = in.readLine()) != null) {
                    if (requisicao.equalsIgnoreCase("SAIR")) {
                        out.println("Conexão encerrada.");
                        break;
                    }

                    // Protocolo esperado: OPERACAO VALOR1 VALOR2 (ex: SOMA 10 20)
                    String[] partes = requisicao.trim().split(" ");
                    if (partes.length != 3) {
                        out.println("ERRO: Formato invalido. Use: SOMA|SUB|MULT|DIV <num1> <num2>");
                        continue;
                    }

                    String operacao = partes[0].toUpperCase();
                    try {
                        double a = Double.parseDouble(partes[1]);
                        double b = Double.parseDouble(partes[2]);
                        double resultado;

                        switch (operacao) {
                            case "SOMA":
                                resultado = a + b;
                                break;
                            case "SUB":
                                resultado = a - b;
                                break;
                            case "MULT":
                                resultado = a * b;
                                break;
                            case "DIV":
                                if (b == 0) {
                                    out.println("ERRO: Divisao por zero");
                                    continue;
                                }
                                resultado = a / b;
                                break;
                            default:
                                out.println("ERRO: Operacao desconhecida. Use SOMA, SUB, MULT ou DIV");
                                continue;
                        }

                        out.println("RESULTADO: " + resultado);
                    } catch (NumberFormatException e) {
                        out.println("ERRO: Parametros devem ser numeros validos");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
