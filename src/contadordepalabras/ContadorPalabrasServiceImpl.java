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
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.JTextArea;

public class ContadorPalabrasServiceImpl extends UnicastRemoteObject implements ContadorPalabrasService {
    private JTextArea txaFinal;

    public ContadorPalabrasServiceImpl(JTextArea txaFinal) throws RemoteException {
        super();
        this.txaFinal = txaFinal;
    }

    @Override
    public int contarPalabras(String texto) throws RemoteException {
        String[] palabras = texto.split("\\s+");

        if (palabras.length > 1) {
            long startTime = System.nanoTime();

            int numHilos = Math.min(8, palabras.length);
            int palabrasPorHilo = (int) Math.ceil((double) palabras.length / numHilos);

            ContadorPalabras contador = new ContadorPalabras();

            ExecutorService executor = Executors.newFixedThreadPool(numHilos);
            for (int i = 0; i < numHilos; i++) {
                int inicio = i * palabrasPorHilo;
                int fin = Math.min((i + 1) * palabrasPorHilo, palabras.length);

                Runnable worker = new ContarPalabrasWorker(palabras, contador, inicio, fin);
                executor.execute(worker);
            }

            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int cantidadPalabras = contador.getCantidadPalabras();

            long endTime = System.nanoTime();
            double tiempoEjecucion = (endTime - startTime) / 1_000_000.0;

            System.out.println("El tiempo de ejecución es: " + tiempoEjecucion + " ms");
            txaFinal.setText("Cantidad de palabras: " + cantidadPalabras + "\n");
            txaFinal.append("Tiempo de ejecución: " + tiempoEjecucion + " ms");

            return cantidadPalabras;
        } else {
            return 0;
        }
    }

    private static class ContadorPalabras {
        private int cantidadPalabras;
        private Object lock = new Object();

        public void incrementar() {
            synchronized (lock) {
                cantidadPalabras++;
            }
        }

        public int getCantidadPalabras() {
            return cantidadPalabras;
        }
    }

    private static class ContarPalabrasWorker implements Runnable {
        private String[] palabras;
        private ContadorPalabras contador;
        private int inicio;
        private int fin;

        public ContarPalabrasWorker(String[] palabras, ContadorPalabras contador, int inicio, int fin) {
            this.palabras = palabras;
            this.contador = contador;
            this.inicio = inicio;
            this.fin = fin;
        }

        @Override
        public void run() {
            for (int i = inicio; i < fin; i++) {
                if (!palabras[i].isEmpty()) {
                    contador.incrementar();
                }
            }
        }
    }
}


