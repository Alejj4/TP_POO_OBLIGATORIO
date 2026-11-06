package menu;

import controladores.ControladorCuenta;
import controladores.ControladorViajes;
import excepciones.*;
import usuarios.Usuario;
import javax.swing.*;

public class MenuUsuario extends Menu{
    private ControladorCuenta controladorCuenta;
    private ControladorViajes controladorViajes;

    public MenuUsuario(ControladorCuenta controladorCuenta, ControladorViajes controladorViajes) {

        this.controladorCuenta = controladorCuenta;
        this.controladorViajes = controladorViajes;

    }

    public ControladorCuenta getControladorCuenta() {
        return controladorCuenta;
    }

    public ControladorViajes getControladorViajes() {
        return controladorViajes;
    }

    public void registrarUsuario() {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        if(nombre == null) return;
        String documento = JOptionPane.showInputDialog("Documento:");
        if(documento == null) return;
        String email = JOptionPane.showInputDialog("Email:");
        if (email == null) return;

        try {
            Usuario usuario = controladorCuenta.registrarUsuario(nombre, documento, email);
            JOptionPane.showMessageDialog(null,"Usuario registrado: " + nombre + ", Email: " +email);
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void agregarSaldo() {
        String documento = JOptionPane.showInputDialog("Documento:");
        if(documento == null) return;
        double importe = leerDouble("Importe que desea agregar: ");
        if(importe == -1) return;
        String medioPago = JOptionPane.showInputDialog("Medio de pago:");
        if (medioPago == null) return;

        try {
            double nuevoSaldo = controladorCuenta.agregarSaldo(documento, importe, medioPago);
            JOptionPane.showMessageDialog(null,"Saldo agregado correctamente. Nuevo saldo: $" + nuevoSaldo);
        } catch (ImporteInvalidoException | UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
        }
    }

    public void retirarVehiculo() {
        String documento = JOptionPane.showInputDialog("Documento:");
        if(documento == null) return;
        String codigoEstacion = JOptionPane.showInputDialog("Código de estación:");
        if(codigoEstacion == null) return;
        String codigoVehiculo = JOptionPane.showInputDialog("Código de vehículo:");
        if (codigoVehiculo == null) return;

        try {
            controladorViajes.retirarVehiculo(documento, codigoVehiculo, codigoEstacion);
            JOptionPane.showMessageDialog(null,"Vehículo retirado. Buen viaje!");
        } catch (UsuarioNoEncontradoException | EstacionNoEncontradaException | VehiculoNoDisponibleException | RetiroNoPermitidoException e) {
            JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
        }
    }

    public void devolverVehiculo() {
        String documento = JOptionPane.showInputDialog("Documento:");
        if (documento == null) return;
        String codigoEstacion = JOptionPane.showInputDialog("Código de estación de devolución:");
        if (codigoEstacion == null) return;

        try {
            double costo = controladorViajes.devolverVehiculo(documento, codigoEstacion);
            JOptionPane.showMessageDialog(null,"Vehículo devuelto. Costo del viaje: $" + costo);
        } catch (UsuarioNoEncontradoException | EstacionNoEncontradaException | NoTieneViajeAbiertoException e) {
            JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
        }
    }

    public void suscribirPlanMensual() {
        String documento = JOptionPane.showInputDialog("Documento:");
        if (documento == null) return;

        try {
            controladorCuenta.suscribirPlanMensual(documento);
            JOptionPane.showMessageDialog(null,"Te suscribiste al plan mensual.");
        } catch (SaldoInsuficienteException | PlanMensualActivoException | ImporteInvalidoException | UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
        }
    }
}
