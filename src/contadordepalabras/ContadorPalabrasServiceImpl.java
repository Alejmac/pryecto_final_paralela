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
            // Dividir el texto en partes para procesarlo en paralelo
            int numThreads = Runtime.getRuntime().availableProcessors();
            int chunkSize = texto.length() / numThreads;
            WordCountThread[] threads = new WordCountThread[numThreads];

            int startIndex = 0;
            int endIndex = chunkSize;

            // Crear y ejecutar los threads para contar las palabras en paralelo
            for (int i = 0; i < numThreads; i++) {
                if (i == numThreads - 1) {
                    // El último thread procesa el resto del texto
                    endIndex = texto.length();
                }

                threads[i] = new WordCountThread(texto.substring(startIndex, endIndex));
                threads[i].start();

                startIndex = endIndex;
                endIndex += chunkSize;
            }

            int totalWords = 0;

            // Esperar a que todos los threads terminen y obtener el resultado
            for (int i = 0; i < numThreads; i++) {
                try {
                    threads[i].join();
                    totalWords += threads[i].getWordCount();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return totalWords;
        }

        return 0;
    }

    private static class WordCountThread extends Thread {
        private String text;
        private int wordCount;

        public WordCountThread(String text) {
            this.text = text;
            this.wordCount = 0;
        }

        public int getWordCount() {
            return wordCount;
        }

        @Override
        public void run() {
            if (text != null) {
                String[] words = text.trim().split("\\s+");
                wordCount = words.length;
            }
        }
    }
}

