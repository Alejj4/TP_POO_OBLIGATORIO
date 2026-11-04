package menu;

import controladores.ControladorCuenta;
import controladores.ControladorEstacion;
import controladores.ControladorReporte;
import excepciones.EstacionNoEncontradaException;
import excepciones.UsuarioNoEncontradoException;
import usuarios.Usuario;
import vehiculos.Estacion;
import viajes.Viaje;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

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
        String codigo = JOptionPane.showInputDialog("Codigo:");
        if (codigo == null) return;
        String direccion = JOptionPane.showInputDialog("Direccion:");
        if (direccion == null) return;
        String coordenadas = JOptionPane.showInputDialog("Coordenadas:");
        if (coordenadas == null) return;
        int capacidadMaxima = leerEntero("Capacidad maxima: ");

        try {
            Estacion estacion = controladorEstacion.registrarEstacion(codigo, direccion, coordenadas, capacidadMaxima);
            JOptionPane.showMessageDialog(null,"Estacion registrada: " + estacion);
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
        }
    }
    public void verReporteViajesPorUsuario() {
        String documento = JOptionPane.showInputDialog("Documento del usuario:");
        if (documento == null) return;

        try {
            List<Viaje> viajes = controladorReporte.reporteViajesPorUsuario(documento);

            if (viajes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El usuario no tiene viajes registrados.");
            } else {
                String reporte = "Viajes del usuario " + documento + ":\n";
                for (Viaje v : viajes) {
                    reporte += v.toString() + "\n";
                }
                JOptionPane.showMessageDialog(null, reporte);
            }
        } catch (UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void verReporteUsoPorEstacion() {
        List<Estacion> estacionesOrdenadas = controladorEstacion.obtenerEstacionesOrdenadasPorCapacidad();
        String listadoEstaciones = "Estaciones disponibles (ordenadas por capacidad):\n";
        for (Estacion estacion : estacionesOrdenadas) {
            listadoEstaciones += estacion.toString() + "\n";
        }

        String codigo = JOptionPane.showInputDialog(listadoEstaciones + "\nCódigo de la estación:");
        if (codigo == null) return;

        try {
            List<Viaje> viajes = controladorReporte.reporteUsoPorEstacion(codigo);

            if (viajes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay viajes registrados en esta estación.");
            } else {
                String reporte = "Uso de la estación " + codigo + ":\n\n";
                for (Viaje v : viajes) {
                    reporte += v.toString() + "\n";
                }
                JOptionPane.showMessageDialog(null, reporte);
            }
        } catch (EstacionNoEncontradaException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }


}
