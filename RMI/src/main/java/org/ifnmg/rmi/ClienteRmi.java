package org.ifnmg.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Cliente de console para calculadora distribuída via Java RMI.
 * 
 * @author JoaoKSS
 * @author andref03
 */
public class ClienteRmi {
    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 1099;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : HOST_PADRAO;
        int porta = args.length > 1 ? Integer.parseInt(args[1]) : PORTA_PADRAO;

        System.out.println("Conectando ao RMI Registry em " + host + ":" + porta + "...");

        try {
            Registry registry = LocateRegistry.getRegistry(host, porta);
            CalculadoraRmi calculadora = (CalculadoraRmi) registry.lookup(ServidorRmi.NOME_SERVICO);

            System.out.println("Conectado ao servico remoto com sucesso!");
            System.out.println("Use: SOMA|SUB|MULT|DIV <num1> <num2> ou SAIR");

            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("> ");
                    String comando = scanner.nextLine().trim();

                    if (comando.equalsIgnoreCase("SAIR")) {
                        System.out.println("Encerrando cliente RMI.");
                        break;
                    }

                    if (comando.isEmpty()) {
                        continue;
                    }

                    String[] partes = comando.split("\\s+");
                    if (partes.length != 3) {
                        System.out.println("Formato invalido. Exemplo: SOMA 10 20");
                        continue;
                    }

                    String operacao = partes[0].toUpperCase();
                    try {
                        double a = Double.parseDouble(partes[1]);
                        double b = Double.parseDouble(partes[2]);
                        double resultado;

                        switch (operacao) {
                            case "SOMA":
                                resultado = calculadora.somar(a, b);
                                break;
                            case "SUB":
                                resultado = calculadora.subtrair(a, b);
                                break;
                            case "MULT":
                                resultado = calculadora.multiplicar(a, b);
                                break;
                            case "DIV":
                                resultado = calculadora.dividir(a, b);
                                break;
                            default:
                                System.out.println("Operacao desconhecida. Use SOMA, SUB, MULT ou DIV");
                                continue;
                        }

                        System.out.println("Servidor respondeu: " + resultado);

                    } catch (NumberFormatException e) {
                        System.out.println("Erro: Parametros devem ser numeros validos");
                    } catch (ArithmeticException e) {
                        System.out.println("Erro na operacao: " + e.getMessage());
                    } catch (RemoteException e) {
                        System.out.println("Erro na invocacao remota RMI: " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao comunicar com o servidor RMI: " + e.getMessage());
        }
    }
}

