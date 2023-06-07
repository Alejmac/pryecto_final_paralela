/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contadordepalabras;

/**
 *
 * @author jorge
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.swing.JTextArea;

public interface ContadorPalabrasService extends Remote {
    int contarPalabras(String texto) throws RemoteException;
}

