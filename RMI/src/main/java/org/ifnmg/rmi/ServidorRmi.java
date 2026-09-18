package org.ifnmg.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Servidor Java RMI que registra o serviço da calculadora no RMI Registry.
 * 
 * @author JoaoKSS
 * @author andref03
 */
public class ServidorRmi {
    public static final int PORTA_PADRAO = 1099;
    public static final String NOME_SERVICO = "CalculadoraService";

    public static void main(String[] args) {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : PORTA_PADRAO;

        // Se fornecido um IP ou hostname de rede como segundo argumento, configura para permitir conexões remotas
        if (args.length > 1) {
            System.setProperty("java.rmi.server.hostname", args[1]);
        }

        try {
            System.out.println("Iniciando RMI Registry na porta " + porta + "...");
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(porta);
                System.out.println("RMI Registry criado com sucesso na porta " + porta + ".");
            } catch (Exception e) {
                // Caso o registro ja tenha sido criado anteriormente
                registry = LocateRegistry.getRegistry(porta);
                System.out.println("RMI Registry existente obtido na porta " + porta + ".");
            }

            CalculadoraRmi calculadora = new CalculadoraRmiImpl();
            registry.rebind(NOME_SERVICO, calculadora);

            System.out.println("Objeto remoto registrado como '" + NOME_SERVICO + "'.");
            System.out.println("Servidor RMI pronto e aguardando chamadas remotas...");

            // Mantém a thread principal viva para continuar atendendo os clientes
            Thread.currentThread().join();

        } catch (Exception e) {
            System.err.println("Erro ao iniciar o servidor RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
