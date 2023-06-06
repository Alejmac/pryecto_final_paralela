/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contadordepalabras;

/**
 *
 * @author jorge
 */
 import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ContadorPalabrasServiceImpl extends UnicastRemoteObject implements ContadorPalabrasService {
    public ContadorPalabrasServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public int contarPalabras(String texto) throws RemoteException {
        if (texto != null) {
            String[] palabras = texto.split("\\s+");
            return palabras.length;
        }
        return 0;
    }
}

