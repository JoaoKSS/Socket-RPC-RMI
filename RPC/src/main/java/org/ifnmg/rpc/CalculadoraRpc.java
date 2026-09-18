package org.ifnmg.rpc;

import io.grpc.stub.StreamObserver;


/**
 * Servidor gRPC para calculadora distribuída.
 * 
 * @author joao-kennedy
 * @author andref03
 */

public class CalculadoraRpc extends CalculadoraServiceGrpc.CalculadoraServiceImplBase {
    @Override
    public void somar(OperacaoRequest request, StreamObserver<ResultadoResponse> resposta) {
        responder(request.getPrimeiro() + request.getSegundo(), resposta);
    }

    @Override
    public void subtrair(OperacaoRequest request, StreamObserver<ResultadoResponse> resposta) {
        responder(request.getPrimeiro() - request.getSegundo(), resposta);
    }

    @Override
    public void multiplicar(OperacaoRequest request, StreamObserver<ResultadoResponse> resposta) {
        responder(request.getPrimeiro() * request.getSegundo(), resposta);
    }

    @Override
    public void dividir(OperacaoRequest request, StreamObserver<ResultadoResponse> resposta) {
        if (request.getSegundo() == 0) {
            resposta.onError(new IllegalArgumentException("Divisao por zero"));
            return;
        }
        responder(request.getPrimeiro() / request.getSegundo(), resposta);
    }

    private void responder(double resultado, StreamObserver<ResultadoResponse> resposta) {
        resposta.onNext(ResultadoResponse.newBuilder().setResultado(resultado).build());
        resposta.onCompleted();
    }
}