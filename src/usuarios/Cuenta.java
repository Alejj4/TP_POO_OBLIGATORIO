package usuarios;
import excepciones.ImporteInvalidoException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {
    private double saldo;
    private String medioPagoAsociado;
    private List<Movimiento> movimientos;

    public Cuenta(double saldo) {
        this.saldo = saldo;
        this.medioPagoAsociado = null;
        this.movimientos = new ArrayList<>();
    }

    public Cuenta() {
        this(0.0);
    }

    public double getSaldo() {
        return saldo;
    }

    public String getMedioPagoAsociado() {
        return medioPagoAsociado;
    }

    public List<Movimiento> getMovimientos() { return movimientos; }


    public double recargar(double importe, String medioPago) {
        if (medioPagoAsociado == null) {
            this.medioPagoAsociado = medioPago;
        }
        Movimiento mov = new Movimiento(LocalDateTime.now(), importe, "RECARGA");
        this.agregarMovimiento(mov);
        this.saldo += importe;
        return this.saldo;
    }

    public double debitar(double importe, String concepto) throws ImporteInvalidoException{
        if(importe > 0){
            this.saldo -= importe;
            Movimiento mov = new Movimiento(LocalDateTime.now(), importe,concepto);
            this.agregarMovimiento(mov);
        }else
            throw new ImporteInvalidoException("El importe debe ser mayor a 0");
        return this.saldo;
    }

    public void agregarMovimiento(Movimiento movimiento){
        this.movimientos.add(movimiento);
    }
}