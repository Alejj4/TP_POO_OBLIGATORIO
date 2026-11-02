package controladores;
import repositorio.Repositorio;
import viajes.*;
import usuarios.Usuario;
import excepciones.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import vehiculos.Estacion;

public class ControladorReporte {

    private Repositorio repositorio;

    public ControladorReporte(Repositorio repositorio) {
        this.repositorio = repositorio; }

    public List<Viaje> reporteViajesPorUsuario(String documento) throws UsuarioNoEncontradoException {
        Usuario u = repositorio.buscarUsuario(documento);
        if (u == null){
            throw new UsuarioNoEncontradoException("No existe el usuario " + documento);
        }
        List<Viaje> viajes = new ArrayList<>(u.getViajes());
        Collections.sort(viajes);
        return viajes;

    }



    public List<Viaje> reporteUsoPorEstacion(String codigo) throws EstacionNoEncontradaException {
        Estacion e = repositorio.buscarEstacion(codigo);
        if (e == null){
            throw new EstacionNoEncontradaException("No existe la estacion " + codigo);
        }
        List<Viaje> resultado = new ArrayList<>();
        for (Usuario u : repositorio.getUsuarios())
            for (Viaje v : u.getViajes())
                if (v.getEstacionOrigen() == e || v.getEstacionDestino() == e){
                    resultado.add(v);
                }
        Collections.sort(resultado);
        return resultado;
    }
}

