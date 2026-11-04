package controladores;

import excepciones.*;
import repositorio.Repositorio;
import usuarios.Usuario;
import vehiculos.Estacion;
import vehiculos.Vehiculo;
import viajes.PlanMensual;
import viajes.PrimerViaje;
import viajes.Promocion;
import viajes.Viaje;


public class ControladorViajes {
    private Repositorio repositorio;




    public ControladorViajes(Repositorio repositorio) {
        this.repositorio = repositorio;
    }




    public Viaje retirarVehiculo(String documento, String codigoVehiculo, String codigoEstacion) throws UsuarioNoEncontradoException, VehiculoNoDisponibleException, RetiroNoPermitidoException, EstacionNoEncontradaException {
        Usuario usuario = repositorio.buscarUsuario(documento);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No existe un usuario con documento " + documento);
        }

        Estacion estacion = repositorio.buscarEstacion(codigoEstacion);
        if (estacion == null) {
            throw new EstacionNoEncontradaException("No existe la estación " + codigoEstacion);
        }

        Vehiculo vehiculo = estacion.buscarVehiculo(codigoVehiculo);
        if (vehiculo == null) {
            throw new VehiculoNoDisponibleException("El vehículo " + codigoVehiculo + " no está en la estación " + codigoEstacion);
        }
        Viaje viaje = usuario.crearViaje(vehiculo, estacion);
        estacion.quitarVehiculo(vehiculo);
        return viaje;
    }




    public double devolverVehiculo(String documento, String codigoEstacion) throws UsuarioNoEncontradoException, EstacionNoEncontradaException, NoTieneViajeAbiertoException {
        Usuario usuario = repositorio.buscarUsuario(documento);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No existe un usuario con documento " + documento);
        }

        Estacion estacion = repositorio.buscarEstacion(codigoEstacion);
        if (estacion == null) {
            throw new EstacionNoEncontradaException("No existe la estación " + codigoEstacion);
        }

        Viaje viaje = usuario.getViajeAbierto();
        if (viaje == null) {
            throw new NoTieneViajeAbiertoException("No tenés ningún viaje en curso.");
        }

        viaje.finalizar(estacion);
        estacion.agregarVehiculo(viaje.getVehiculo());

        Promocion promo = null;
        if (usuario.getTienePlanMensual()) {
            promo = new PlanMensual();
        } else if (usuario.cantidadViajesPrevios(viaje) == 0) {
            promo = new PrimerViaje();
        }
        double totalViaje = viaje.calcularCosto(promo);

        if (usuario.getTienePlanMensual() && !viaje.fueCancelado()) {
            usuario.registrarViajeConPlan();
        }

        try {
            if (totalViaje > 0) {
                usuario.getCuenta().debitar(totalViaje, "Se ha debitado el viaje en la cuenta del usuario.");
            }
        } catch (ImporteInvalidoException e) {
            System.out.println(e.getMessage());
        } finally {
            repositorio.guardarTodos();
        }

        return totalViaje;
    }
}
