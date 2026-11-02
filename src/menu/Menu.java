package menu;

import javax.swing.*;

public abstract class Menu {

    protected int leerEntero(String mensaje) {
        while (true) {
            String texto = JOptionPane.showInputDialog(mensaje);
            if (texto == null){
                return -1;}
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,"Ingresa un número válido.");
            }
        }
    }

    protected double leerDouble(String mensaje) {
        while (true) {
            String texto = JOptionPane.showInputDialog(mensaje);
            if (texto == null){
                return -1;}
            try {
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,"Ingresa un importe válido.");
            }
        }
    }
}