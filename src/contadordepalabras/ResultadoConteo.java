/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contadordepalabras;

/**
 *
 * @author jorge
 */
    import java.io.Serializable;

    public class ResultadoConteo implements Serializable {
        private int cantidadPalabras;
        private double tiempoEjecucion;

        public ResultadoConteo(int cantidadPalabras, double tiempoEjecucion) {
            this.cantidadPalabras = cantidadPalabras;
            this.tiempoEjecucion = tiempoEjecucion;
        }

        public int getCantidadPalabras() {
            return cantidadPalabras;
        }

        public double getTiempoEjecucion() {
            return tiempoEjecucion;
        }
    }

