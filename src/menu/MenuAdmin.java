package menu;

import controladores.ControladorCuenta;
import controladores.ControladorEstacion;
import controladores.ControladorReporte;
import usuarios.Usuario;
import vehiculos.Estacion;

import javax.swing.*;
import java.util.Collections;

public class MenuAdmin extends Menu{
    private ControladorCuenta controladorCuenta;
    private ControladorEstacion controladorEstacion;
    private ControladorReporte controladorReporte;

    public MenuAdmin(ControladorCuenta controladorCuenta, ControladorEstacion controladorEstacion, ControladorReporte controladorReporte) {
        this.controladorCuenta = controladorCuenta;
        this.controladorEstacion = controladorEstacion;
        this.controladorReporte = controladorReporte;
    }
    // hay que hacer la logica para que aparezca en pantalla el tema de los reportes

    public void buscarUsuario() {
        String documento = JOptionPane.showInputDialog("Documento:");

        Usuario usuario = controladorCuenta.buscarUsuario(documento);

        if (usuario != null) {
            Collections.sort(usuario.getViajes());
            JOptionPane.showMessageDialog(null,"Usuario encontrado: " + usuario);
        } else {
            JOptionPane.showMessageDialog(null,"No se encontro un usuario con ese documento.");
        }
    }

    public void agregarEstacion() {
        String codigo = JOptionPane.showInputDialog("Código:");
        if (codigo == null) return;
        String direccion = JOptionPane.showInputDialog("Dirección:");
        if (direccion == null) return;
        String coordenadas = JOptionPane.showInputDialog("Coordenadas:");
        if (coordenadas == null) return;
        int capacidadMaxima = leerEntero("Capacidad maxima: ");

        try {
            Estacion estacion = controladorEstacion.registrarEstacion(codigo, direccion, coordenadas, capacidadMaxima);
            JOptionPane.showMessageDialog(null,"Estación registrada: " + estacion);
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
        }
    }
}
