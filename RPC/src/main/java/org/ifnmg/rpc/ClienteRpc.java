package org.ifnmg.rpc;

import java.util.Scanner;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

/**
 * Cliente de console para calculadora distribuída via gRPC.
 * 
 * @author joao-kennedy
 * @author andref03
 */

public class ClienteRpc {
    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 8080;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : HOST_PADRAO;
        int porta = args.length > 1 ? Integer.parseInt(args[1]) : PORTA_PADRAO;

        ManagedChannel canal = ManagedChannelBuilder.forAddress(host, porta)
                .usePlaintext()
                .build();
        CalculadoraServiceGrpc.CalculadoraServiceBlockingStub cliente =
                CalculadoraServiceGrpc.newBlockingStub(canal);

        System.out.println("Cliente gRPC conectado a " + host + ":" + porta);
        System.out.println("Use: SOMA|SUB|MULT|DIV <num1> <num2> ou SAIR");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String comando = scanner.nextLine().trim();
                if (comando.equalsIgnoreCase("SAIR")) {
                    break;
                }

                String[] partes = comando.split("\\s+");
                if (partes.length != 3) {
                    System.out.println("Formato invalido. Exemplo: SOMA 10 20");
                    continue;
                }

                try {
                    OperacaoRequest parametros = OperacaoRequest.newBuilder()
                            .setPrimeiro(Double.parseDouble(partes[1]))
                            .setSegundo(Double.parseDouble(partes[2]))
                            .build();
                    ResultadoResponse resposta = chamarOperacao(cliente, partes[0], parametros);
                    System.out.println("Servidor respondeu: " + resposta.getResultado());
                } catch (NumberFormatException | StatusRuntimeException e) {
                    System.out.println("Erro na chamada RPC: " + e.getMessage());
                }
            }
        } finally {
            canal.shutdown();
        }
    }

    private static ResultadoResponse chamarOperacao(
            CalculadoraServiceGrpc.CalculadoraServiceBlockingStub cliente,
            String operacao,
            OperacaoRequest parametros) {
        return switch (operacao.toUpperCase()) {
            case "SOMA" -> cliente.somar(parametros);
            case "SUB" -> cliente.subtrair(parametros);
            case "MULT" -> cliente.multiplicar(parametros);
            case "DIV" -> cliente.dividir(parametros);
            default -> throw new IllegalArgumentException("Operacao desconhecida");
        };
    }
}