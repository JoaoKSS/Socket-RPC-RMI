package org.ifnmg.rpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * Servidor gRPC para calculadora distribuída.
 * 
 * @author joao-kennedy
 * @author andref03
 */

public class ServidorRpc {
    public static final int PORTA_PADRAO = 8080;

    public static void main(String[] args) throws Exception {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : PORTA_PADRAO;
        Server servidor = ServerBuilder.forPort(porta)
                .addService(new CalculadoraRpc())
                .build()
                .start();

        System.out.println("Servidor gRPC pronto na porta " + porta + ".");
        Runtime.getRuntime().addShutdownHook(new Thread(servidor::shutdown));
        servidor.awaitTermination();
    }
}