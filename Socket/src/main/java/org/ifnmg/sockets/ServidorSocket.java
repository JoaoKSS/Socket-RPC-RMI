package org.ifnmg.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor multithread para calculadora distribuída via Sockets TCP.
 * 
 * @author joao-kennedy
 */
public class ServidorSocket {
    public static final int PORTA_PADRAO = 5000;

    public static void main(String[] args) {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : PORTA_PADRAO;
        System.out.println("Iniciando servidor de socket na porta " + porta + "...");

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor pronto e aguardando conexoes...");

            // Loop para aceitar multiplos clientes continuamente
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getRemoteSocketAddress());

                // Atende cada cliente em uma thread separada
                new Thread(new AtendenteCliente(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor de socket: " + e.getMessage());
        }
    }
}

class AtendenteCliente implements Runnable {
    private final Socket socket;

    public AtendenteCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String requisicao;
            while ((requisicao = in.readLine()) != null) {
                requisicao = requisicao.trim();
                if (requisicao.equalsIgnoreCase("SAIR")) {
                    out.println("Conexao encerrada.");
                    break;
                }

                // Divide por um ou mais espacos em branco
                String[] partes = requisicao.split("\\s+");
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
        } catch (IOException e) {
            System.out.println("Conexao interrompida com " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            System.out.println("Cliente desconectado: " + socket.getRemoteSocketAddress());
        }
    }
}
