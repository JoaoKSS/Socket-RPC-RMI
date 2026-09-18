package org.ifnmg.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementação do objeto remoto da calculadora via Java RMI.
 * 
 * @author JoaoKSS
 * @author andref03
 */
public class CalculadoraRmiImpl extends UnicastRemoteObject implements CalculadoraRmi {
    
    public CalculadoraRmiImpl() throws RemoteException {
        super();
    }

    public CalculadoraRmiImpl(int port) throws RemoteException {
        super(port);
    }

    @Override
    public double somar(double a, double b) throws RemoteException {
        return a + b;
    }

    @Override
    public double subtrair(double a, double b) throws RemoteException {
        return a - b;
    }

    @Override
    public double multiplicar(double a, double b) throws RemoteException {
        return a * b;
    }

    @Override
    public double dividir(double a, double b) throws RemoteException, ArithmeticException {
        if (b == 0) {
            throw new ArithmeticException("Divisao por zero");
        }
        return a / b;
    }
}

