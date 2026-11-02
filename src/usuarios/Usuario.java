package usuarios;

import excepciones.*;
import vehiculos.Estacion;
import vehiculos.Estado;
import vehiculos.Vehiculo;
import viajes.Viaje;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nombre;
    private String documento;
    private String email;
    private Cuenta cuenta;
    private boolean tienePlanMensual;
    private List<Viaje> viajes;
    private static final double PRECIO_PLAN_MENSUAL = 15000;

    public List<Viaje> getViajes() {
        return viajes;
    }
    public boolean getTienePlanMensual(){return tienePlanMensual;}
    public String getDocumento() {
        return documento;
    }

    public Usuario(String nombre, String documento, String email){
        this.nombre = nombre;
        this.documento = documento;
        this.email = email;
        this.tienePlanMensual = false;
        this.cuenta = new Cuenta();
        this.viajes = new ArrayList<>();
    }

    public boolean puedeRetirar(double saldoMinimo){
            return this.cuenta.getSaldo() >= saldoMinimo;
    }

    public double acreditarSaldo(double importe, String medioPago) throws ImporteInvalidoException {
        if(importe <= 0 ){
            throw new ImporteInvalidoException("El importe debe ser mayor a 0");
        }else
            return this.cuenta.recargar(importe,medioPago);
    }

    public boolean tieneViajeAbierto(){
        for(Viaje viaje : this.viajes){
            if (viaje.getFechaHoraFin() == null){
                return true;
            }
        }
        return false;
    }

    public Viaje crearViaje(Vehiculo vehiculo, Estacion estacionOrigen) throws RetiroNoPermitidoException, VehiculoNoDisponibleException{
        if(!tieneViajeAbierto() && puedeRetirar(vehiculo.calcularCosto(30))){
            if(vehiculo.estaDisponible()){
                Viaje viaje = new Viaje(this, vehiculo, estacionOrigen );
                viajes.add(viaje);
                vehiculo.cambiarEstado(Estado.EN_USO);
                estacionOrigen.quitarVehiculo(vehiculo);
                return viaje;
            } else
                throw new VehiculoNoDisponibleException("El vehiculo seleccionado no se encuentra disponible.");
        } else
            throw new RetiroNoPermitidoException("Su cuenta no cuenta con el saldo minimo requerido.");
    }

    public String contenidoArchivo() {
        return nombre + ";" + documento + ";" + email + ";" + tienePlanMensual + ";" + cuenta.getSaldo();
    }

    public void suscribirPlanMensual() throws ImporteInvalidoException, SaldoInsuficienteException, PlanMensualActivoException {
        if (!tienePlanMensual){
            if(cuenta.getSaldo() >= PRECIO_PLAN_MENSUAL){
            cuenta.debitar(PRECIO_PLAN_MENSUAL,"Plan mensual");
            tienePlanMensual = true;
            }else
                throw new SaldoInsuficienteException("Saldo insuficiente");
        } else
            throw new PlanMensualActivoException("Ya ténes el plan mensual.");
    }

    public static Usuario desdeLinea(String linea) {
        String[] partes = linea.split(";");
        String nombre = partes[0];
        String documento = partes[1];
        String email = partes[2];
        boolean plan = Boolean.parseBoolean(partes[3]);
        double saldo = Double.parseDouble(partes[4]);

        Usuario usuario = new Usuario(nombre, documento, email);
        usuario.tienePlanMensual = plan;
        usuario.cuenta = new Cuenta(saldo);
        return usuario;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    @Override
    public String toString() {
        return nombre + " (DNI " + documento + ") - Email: " + email
                + " - Saldo: $" + cuenta.getSaldo()
                + " - Plan mensual: " + (tienePlanMensual ? "Si" : "No");
    }
}
