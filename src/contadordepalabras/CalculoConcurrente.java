/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contadordepalabras;

/**
 *
 * @author jorge
 */
 import java.text.DecimalFormat;
import java.util.Arrays;
import javax.swing.JTextArea;

public class CalculoConcurrente implements Runnable {
    private String[] palabras;
    private JTextArea txaFinalConcurrente;
    
  // constructor de la clase 
    public CalculoConcurrente(String texto, JTextArea txaFinalConcurrente) {
        this.palabras = texto.split("\\s+");
        this.txaFinalConcurrente = txaFinalConcurrente;
    }

    public void contarPalabrasConcurrente()  {
        //System.out.println("El texto es: " + Arrays.toString(palabras));
        if (palabras.length > 1) {
            // se inicia el conteo del programa 
            long startTime = System.nanoTime();

            // Definir la cantidad de hilos a utilizar (puedes ajustar este valor según tus necesidades)
            int numHilos =Math.min(8, palabras.length); // Limitar el número de hilos a 8 o la cantidad total de palabras si es menor
            int palabrasPorHilo = (int) Math.ceil((double) palabras.length / numHilos); // cantidad de hilos que se necesitan dependiendo de las palabras 
            
             
            // Crear un contador compartido
            ContadorPalabras contador = new ContadorPalabras();

            // Crear los hilos y ejecutarlos
            Thread[] hilos = new Thread[numHilos];
            for (int i = 0; i < numHilos; i++) {
                int inicio = i * palabrasPorHilo;
                int fin = Math.min((i + 1) * palabrasPorHilo, palabras.length); // asegura qu eel ultimo hilo  se detenga en el indice correcto 
                Runnable worker = new ContarPalabrasWorker(palabras, contador, inicio, fin);
                hilos[i] = new Thread(worker);
                hilos[i].start();
            }

            // Esperar a que todos los hilos finalicen
            for (Thread hilo : hilos) {
                try {
                    hilo.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Obtener el resultado del contador
            int cantidadPalabras = contador.getCantidadPalabras();

            long endTime = System.nanoTime();
            double tiempoEjecucion = (endTime - startTime) / 1_000_000.0; // Convertir a milisegundos
            System.out.println("El tiempo de ejecución es: " + tiempoEjecucion + " ms");

            txaFinalConcurrente.setText("Resultado: " + cantidadPalabras + " palabras\n");
            txaFinalConcurrente.append("Tiempo: " + new DecimalFormat("#0.00").format(tiempoEjecucion) + " ms");
        
        int numeroh = Thread.activeCount();     
            System.out.println("numero de e hilos es "+ numeroh);
        } 
        else {
            txaFinalConcurrente.setText("Resultado: No hay texto");
        }
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
                // Realizar el conteo de palabras
                if (!palabras[i].isEmpty()) {
                    contador.incrementar();
                }
            }
        }
    }
}