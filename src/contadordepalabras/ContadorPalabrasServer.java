/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contadordepalabras;

/**
 *
 * @author jorge
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JTextArea;

public class ContadorPalabrasServer {
    public static void main(String[] args) {
        try {
            // Crea un objeto JTextArea para pasar al constructor de ContadorPalabrasServiceImpl
            JTextArea txaFinal = new JTextArea();

            ContadorPalabrasService contadorPalabrasService = new ContadorPalabrasServiceImpl(txaFinal);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ContadorPalabrasService", contadorPalabrasService);
            System.out.println("Servidor RMI listo.");
        } catch (Exception e) {
            System.err.println("Error en el servidor RMI: " + e.toString());
        }
    }
}
