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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
 import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

 public class ContadorPalabrasServiceImpl extends UnicastRemoteObject implements ContadorPalabrasService {
    public ContadorPalabrasServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public int contarPalabras(String texto) throws RemoteException {
        if (texto != null) {
            int numThreads = Runtime.getRuntime().availableProcessors();
            int chunkSize = texto.length() / numThreads;

            ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

            int startIndex = 0;
            int endIndex = chunkSize;

            List<Future<Integer>> results = new ArrayList<>();

            // Crear y ejecutar las tareas para contar las palabras en paralelo
            for (int i = 0; i < numThreads; i++) {
                if (i == numThreads - 1) {
                    // El último thread procesa el resto del texto
                    endIndex = texto.length();
                }

                String subText = texto.substring(startIndex, endIndex);
                WordCountTask task = new WordCountTask(subText);
                Future<Integer> result = executorService.submit(task);
                results.add(result);

                startIndex = endIndex;
                endIndex += chunkSize;
            }

            executorService.shutdown();

            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int totalWords = 0;

            // Obtener los resultados de las tareas completadas
            for (Future<Integer> result : results) {
                try {
                    totalWords += result.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            return totalWords;
        }

        return 0;
    }

    private static class WordCountTask implements Callable<Integer> {
        private String text;

        public WordCountTask(String text) {
            this.text = text;
        }

        @Override
        public Integer call() throws Exception {
            if (text != null) {
                String[] words = text.trim().split("\\s+");
                return words.length;
            }

            return 0;
        }
    }
}

