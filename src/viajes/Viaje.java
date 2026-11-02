package viajes;

import usuarios.Usuario;
import vehiculos.*;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Viaje implements Comparable<Viaje>{
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private double distanciaRecorrido;
    private Usuario usuario;
    private Vehiculo vehiculo;
    private Estacion estacionOrigen;
    private Estacion estacionDestino;
    private static final int minutosDeCancelacion = 2;


    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }
    public LocalDateTime getFechaHoraInicio(){ return fechaHoraInicio; }
    public Usuario getUsuario(){return usuario;}
    public Vehiculo getVehiculo(){return vehiculo;}

    /*
     * Nota sobre atributos del diseño inicial que no se utilizan en la lógica actual:
     * - distanciaRecorrido (atributo) y puntosRecorrido (lista eliminada respecto al diseño original):
     *   Aunque formaban parte del modelo conceptual inicial del viaje, se decidió no utilizarlos
     *   porque tanto el consumo de batería (en finalizar) como el cálculo de la tarifa (en calcularCosto)
     *   se determinan en función de la duración en minutos del viaje (entre fechaHoraInicio y fechaHoraFin).
     * - estacionOrigen y estacionDestino:
     *   Se registran al iniciar y finalizar el viaje para mantener la trazabilidad del trayecto,
     *   aunque no intervienen directamente en el cálculo del costo.
     */
    public Viaje(Usuario usuario, Vehiculo vehiculo, Estacion estacionOrigen) {
        this.usuario = usuario;
        this.vehiculo = vehiculo;
        this.estacionOrigen = estacionOrigen;
        this.fechaHoraInicio = LocalDateTime.now();
        this.fechaHoraFin = null;
        this.estacionDestino = null;
        this.distanciaRecorrido = 0.0;
    }
    @Override
    public int compareTo(Viaje otroViaje) {
        return otroViaje.fechaHoraInicio.compareTo(this.fechaHoraInicio);
    }

    public boolean estaFinalizado() {
        return fechaHoraFin != null;
    }

    public long getDuracionMinutos() {
        return Duration.between(fechaHoraInicio, fechaHoraFin).toMinutes();
    }

    public boolean fueCancelado() {
        return estaFinalizado() && getDuracionMinutos() <= minutosDeCancelacion;
    }


    public void finalizar(Estacion estacionDestino) {
        this.estacionDestino = estacionDestino;
        this.fechaHoraFin = LocalDateTime.now();
        long tiempoViaje = Duration.between(this.fechaHoraInicio, this.fechaHoraFin).toMinutes();
        if (tiempoViaje > 1440) {
            vehiculo.cambiarEstado(Estado.FUERA_DE_SERVICIO);
        } else if (this.vehiculo instanceof BicicletaElectrica bici) {
            int bateria = bici.getbateria();
            int bateriaRestante = (int) (bateria - (0.5 * tiempoViaje));
            if (bateriaRestante < 0) {
                bateriaRestante = 0;
            }
            bici.setbateria(bateriaRestante);
            if (bici.getbateria() < 15) {
                bici.cambiarEstado(Estado.BATERIA_BAJA);
            } else {
                bici.cambiarEstado(Estado.DISPONIBLE);

            }
        } else if (this.vehiculo instanceof MonopatinElectrico monopatin) {
            int bateria = monopatin.getbateria();
            int bateriaRestante = (int) (bateria - (0.5 * tiempoViaje));
            if (bateriaRestante < 0) {
                bateriaRestante = 0;
            }
            monopatin.setbateria(bateriaRestante);
            if (monopatin.getbateria() < 15) {
                monopatin.cambiarEstado(Estado.BATERIA_BAJA);
            } else {
                monopatin.cambiarEstado(Estado.DISPONIBLE);
            }


        } else {
            vehiculo.cambiarEstado(Estado.DISPONIBLE);

        }


        }

    public double calcularCosto(Promocion promo) {
        int minutos = (int) Duration.between(this.fechaHoraInicio, this.fechaHoraFin).toMinutes();
        double costo = this.vehiculo.calcularCosto(minutos);

        if (promo != null) {
            costo = costo - promo.calcularDescuento(this, costo);
        }

        return costo;
    }




    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Estacion getEstacionOrigen() {
        return estacionOrigen;
    }

    public Estacion getEstacionDestino() {
        return estacionDestino;
    }

    @Override
    public String toString() {
        String destino;
        if (estacionDestino != null) {
            destino = estacionDestino.getCodigo();
        } else {
            destino = "-";
        }

        String fin;
        if (fechaHoraFin != null) {
            long minutos = java.time.Duration.between(fechaHoraInicio, fechaHoraFin).toMinutes();
            fin = fechaHoraFin.toString() + " (" + minutos + " min)";
        } else {
            fin = "En curso";
        }

        return "Vehiculo: " + vehiculo.getCodigo() +
                " | Origen: " + estacionOrigen.getCodigo() +
                " | Destino: " + destino +
                " | Inicio: " + fechaHoraInicio +
                " | Fin: " + fin;
    }


}





