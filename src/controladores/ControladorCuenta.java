package controladores;
import excepciones.ImporteInvalidoException;
import excepciones.PlanMensualActivoException;
import excepciones.SaldoInsuficienteException;
import excepciones.UsuarioNoEncontradoException;
import repositorio.Repositorio;
import usuarios.Usuario;
public class ControladorCuenta {

    private Repositorio repositorio;
    public ControladorCuenta(Repositorio repositorio) {
        this.repositorio = repositorio;
    }




    public double agregarSaldo(String documento, double importe, String medioPago) throws ImporteInvalidoException, UsuarioNoEncontradoException {
        Usuario usuario = repositorio.buscarUsuario(documento);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No existe un usuario con documento " + documento);
        }
        double nuevoSaldo = usuario.acreditarSaldo(importe, medioPago);
        repositorio.guardarTodos();
        return nuevoSaldo;
    }



    public Usuario registrarUsuario(String nombre, String documento, String email) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar un nombre válido");
        }
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar un documento");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("El email no es válido");
        }

        if (repositorio.buscarUsuario(documento) != null) {
            throw new IllegalStateException("Ya existe un usuario con el documento " + documento);
        }
        Usuario usuario = new Usuario(nombre, documento, email);
        repositorio.agregarUsuario(usuario);

        return usuario;
    }

    public Usuario buscarUsuario(String documento) {
        return repositorio.buscarUsuario(documento);
    }

    public void suscribirPlanMensual(String documento) throws SaldoInsuficienteException, PlanMensualActivoException, ImporteInvalidoException, UsuarioNoEncontradoException {

        Usuario usuario = repositorio.buscarUsuario(documento);
        if (usuario == null){
            throw new UsuarioNoEncontradoException("No existe un usuario con documento " + documento);
        }
        usuario.suscribirPlanMensual();
        repositorio.guardarTodos();

    }


}