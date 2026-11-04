    package repositorio;

    import usuarios.Usuario;
    import vehiculos.*;
    import viajes.Viaje;

    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;
    import java.io.*;

    public class Repositorio {
        private List<Usuario> usuarios;
        private List<Estacion> estaciones;

        public Repositorio() {
            this.usuarios = new ArrayList<>();
            this.estaciones = new ArrayList<>();
        }
        private void guardarUsuario(Usuario usuario) {
            try (FileWriter fw = new FileWriter("datos/usuarios.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw)) {

                bw.write(usuario.contenidoArchivo());
                bw.newLine();

            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        public void cargarUsuarios() {
            File archivo = new File("datos/usuarios.txt");
            if (!archivo.exists()) {
                return;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.isBlank()) continue;
                    Usuario usuario = Usuario.desdeLinea(linea);
                    this.usuarios.add(usuario);
                }
            } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Error al cargar usuarios: " + e.getMessage());
            }
        }

        public void guardarTodos() {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("datos/usuarios.txt"))) {
                for (Usuario u : usuarios) {
                    bw.write(u.contenidoArchivo());
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error al guardar usuarios: " + e.getMessage());
            }
        }


        private void guardarEstacion(Estacion estacion) {
            try (FileWriter fw = new FileWriter("datos/estaciones.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw)) {

                bw.write(estacion.contenidoArchivo());
                bw.newLine();

            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        public void cargarEstaciones() {
            File archivo = new File("datos/estaciones.txt");
            if (!archivo.exists()) {
                return;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.isBlank()) continue;
                    String[] p = linea.split(";");
                    this.estaciones.add(new Estacion(p[0], p[1], p[2], Integer.parseInt(p[3])));
                }
            } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Error al cargar estaciones: " + e.getMessage());
            }
        }

        public void agregarUsuario(Usuario usuario) {
            if (usuario != null && !this.usuarios.contains(usuario)) {
                this.usuarios.add(usuario);
                guardarUsuario(usuario);
            }
        }

        public void agregarEstacion(Estacion estacion) {
            if (estacion != null && !this.estaciones.contains(estacion)) {
                this.estaciones.add(estacion);
                guardarEstacion(estacion);
            }
        }

        public Usuario buscarUsuario(String documento) {
            if (documento == null) {
                return null;
            }
            for (Usuario usuario : this.usuarios) {
                if (documento.equalsIgnoreCase(usuario.getDocumento())) {
                    return usuario;
                }
            }
            return null;
        }

        public Estacion buscarEstacion(String codigo) {
            if (codigo == null) {
                return null;
            }
            for (Estacion estacion : this.estaciones) {
                if (codigo.equalsIgnoreCase(estacion.getCodigo())) {
                    return estacion;
                }
            }
            return null;
        }

        public void cargarVehiculos() {
            File archivo = new File("datos/vehiculos.txt");
            if (!archivo.exists()) {
                return;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.isBlank()) continue;
                    String[] p = linea.split(";");
                    Vehiculo v = null;
                    switch (p[0]) {
                        case "BICICLETA_COMUN":
                            v = new BicicletaComun(p[1], LocalDate.parse(p[2]), Integer.parseInt(p[3]), Estado.DISPONIBLE);
                            break;
                        case "BICICLETA_ELECTRICA":
                            v = new BicicletaElectrica(p[1], LocalDate.parse(p[2]), Double.parseDouble(p[3]));
                            break;
                        case "MONOPATIN_ELECTRICO":
                            v = new MonopatinElectrico(p[1], LocalDate.parse(p[2]), Double.parseDouble(p[3]));
                            break;
                    }
                    Estacion estacion = buscarEstacion(p[4]);
                    if (v != null && estacion != null) estacion.agregarVehiculo(v);
                }
            } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Error al cargar vehículos: " + e.getMessage());
            }
        }

        public List<Viaje> viajesDeEstacion(Estacion estacion) {
            List<Viaje> resultado = new ArrayList<>();
            for (Usuario usuario : usuarios) {
                for (Viaje viaje : usuario.getViajes()) {
                    if (viaje.getEstacionOrigen() == estacion || viaje.getEstacionDestino() == estacion) {
                        resultado.add(viaje);
                    }
                }
            }
            return resultado;
        }

        public List<Usuario> getUsuarios() {
            return usuarios;
        }

        public List<Estacion> getEstaciones() {
            return estaciones;
        }
    }