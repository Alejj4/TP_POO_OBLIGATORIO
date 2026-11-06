package usuarios;

import excepciones.*;
import vehiculos.Estacion;
import vehiculos.Estado;
import vehiculos.Vehiculo;
import viajes.PlanMensual;
import viajes.Viaje;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nombre;
    private String documento;
    private String email;
    private int viajesConPlanUsados;
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

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
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
        if (tieneViajeAbierto()) {
            throw new RetiroNoPermitidoException("Ya tenés un viaje en curso. Devolvé el vehículo antes de retirar otro.");
        }
        double minimo = vehiculo.calcularCosto(30);
        if (!puedeRetirar(minimo)) {
            throw new RetiroNoPermitidoException("Saldo insuficiente: necesitás al menos $" + minimo + " para retirar este vehículo.");
        }
        if (!vehiculo.estaDisponible()) {
            throw new VehiculoNoDisponibleException("El vehículo seleccionado no se encuentra disponible.");
        }
        Viaje viaje = new Viaje(this, vehiculo, estacionOrigen);
        viajes.add(viaje);
        vehiculo.cambiarEstado(Estado.EN_USO);
        estacionOrigen.quitarVehiculo(vehiculo);
        return viaje;
    }

    public String contenidoArchivo() {
        return nombre + ";" + documento + ";" + email + ";" + tienePlanMensual + ";" + cuenta.getSaldo() + ";" + viajesConPlanUsados;
    }

    public void suscribirPlanMensual() throws ImporteInvalidoException, SaldoInsuficienteException, PlanMensualActivoException {
        if (!tienePlanMensual){
            if(cuenta.getSaldo() >= PRECIO_PLAN_MENSUAL){
            cuenta.debitar(PRECIO_PLAN_MENSUAL,"Plan mensual");
            tienePlanMensual = true;
            viajesConPlanUsados = 0;
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

        Usuario usuario = new Usuario(nombre, documento, email);

        boolean plan = Boolean.parseBoolean(partes[3]);
        double saldo = Double.parseDouble(partes[4]);
        usuario.viajesConPlanUsados = Integer.parseInt(partes[5]);

        usuario.tienePlanMensual = plan;
        usuario.cuenta = new Cuenta(saldo);
        return usuario;
    }

    public int cantidadViajesPrevios(Viaje actual) {
        int viajesPrevios = 0;
        for (Viaje viaje : viajes) {
            if (viaje != actual && viaje.estaFinalizado() && !viaje.fueCancelado()) {
                viajesPrevios++;
            }
        }
        return viajesPrevios;
    }


    public void registrarViajeConPlan() {
        viajesConPlanUsados++;
        if(viajesConPlanUsados >= PlanMensual.TOPE_VIAJES){
            tienePlanMensual = false;
        }
    }

    public int getViajesConPlanUsados() {
        return viajesConPlanUsados;
    }


    public Cuenta getCuenta() {
        return cuenta;
    }

    public Viaje getViajeAbierto() {
        for (Viaje viaje : viajes) {
            if (!viaje.estaFinalizado()) {
                return viaje;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return nombre + " (DNI " + documento + ") - Email: " + email
                + " - Saldo: $" + cuenta.getSaldo()
                + " - Plan mensual: " + (tienePlanMensual ? "Si" : "No");
    }
}
