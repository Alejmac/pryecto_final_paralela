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

public class ClienteRMI {
    private JTextArea txaFinalParalelo;

    public ClienteRMI(String texto, JTextArea txaFinalParalelo) {
        this.txaFinalParalelo = txaFinalParalelo;
    }

    public void conectarServidorRMI(String texto, JTextArea txaFinalParalelo1) {
        try {
            Registry registry = LocateRegistry.getRegistry("192.168.100.47", 1099);
            ContadorPalabrasService contadorPalabrasService = (ContadorPalabrasService) registry.lookup("ContadorPalabrasService");

            long startTime = System.nanoTime();
            int cantidadPalabras = contadorPalabrasService.contarPalabras(texto);
            long endTime = System.nanoTime();
            double tiempoEjecucion = (endTime - startTime) / 1_000_000.0;

            txaFinalParalelo.setText("Cantidad de palabras: " + cantidadPalabras + "\n");
            txaFinalParalelo.append("Tiempo de ejecución: " + tiempoEjecucion + " ms");
        } catch (Exception e) {
            System.err.println("Error en el cliente RMI: " + e.toString());
        }
    }
}


