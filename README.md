# Trabajo Práctico - Programación Orientada a Objetos
## Etapa 2: Codificación, puesta a punto y testeo

### Integrantes:
- Aranguren Alejo - 1215686
- Raggio Gonzalo - 1214922
- Schipani Tiziano - 1214840

### Descripción del sistema:
En este proyecto desarrollamos la propuesta C: un sistema de movilidad compartida con bicicletas (comunes y eléctricas) y monopatines eléctricos distribuidos en estaciones de la ciudad.

El sistema permite:
- Registrar usuarios y cargar saldo en su cuenta (el medio de pago se asocia en la primera recarga).
- Retirar un vehículo de una estación, controlando que el usuario tenga saldo suficiente y no tenga otro viaje abierto.
- Devolver el vehículo en cualquier estación, calcular la tarifa según el tiempo de uso y el tipo de vehículo, aplicar promociones y debitar el monto de la cuenta.
- Suscribirse al plan mensual ($15.000). El plan se desactiva solo al usar los 20 viajes y se puede volver a suscribir.
- Como administrador, buscar usuarios, registrar nuevas estaciones y ver dos reportes: viajes de un usuario (más recientes primero) y uso de una estación.

Promociones:
- **Primer viaje:** los primeros 15 minutos del primer viaje de cada usuario son gratis.
- **Plan mensual:** los viajes de hasta 30 minutos son gratis, con un tope de 20 viajes. Si se pasa de los 30 minutos, se cobran solo los minutos excedentes.

**Alcance recortado respecto de la Etapa 1:** no se implementaron los técnicos, empleados/operadores, órdenes de mantenimiento, reporte de fallas, redistribución de vehículos. El detalle está en el informe final.

### Instalación y ejecución:
1. Ejecutar la clase `Main` (`src/Main.java`). El directorio de trabajo tiene que ser la raíz del proyecto, para que el programa encuentre la carpeta `datos/`.
2. Se abre la ventana principal con dos opciones: **Usuario** y **Administrador**. Cada operación pide los datos y muestra el resultado en ventanas emergentes.

### Persistencia:
Los datos se guardan en archivos de texto dentro de `datos/`:
- `datos/usuarios.txt`: se crea solo al registrar el primer usuario y se actualiza después de cada operación. No se sube al repositorio.
- `datos/estaciones.txt` y `datos/vehiculos.txt`: estaciones y vehículos precargados que se leen al iniciar.

### Datos de prueba:
**Estaciones precargadas**

| Código | Dirección | Capacidad |
|---|---|---|
| E01 | Av. Corrientes 1200 | 10 |
| E02 | Plaza Italia | 8 |
| E03 | Parque Centenario | 6 |

**Vehículos precargados**

| Código | Tipo | Estación |
|---|---|---|
| BC01, BC02 | Bicicleta común | E01 |
| BE01 | Bicicleta eléctrica | E01 |
| ME01 | Monopatín eléctrico | E01 |
| BC03 | Bicicleta común | E02 |
| BE02 | Bicicleta eléctrica | E02 |
| ME02 | Monopatín eléctrico | E02 |
| BC04 | Bicicleta común | E03 |

**Tarifas** (hasta 2 minutos es gratis; después de 120 minutos, el minuto excedente cuesta 1,5 veces más)

| Vehículo | Base | Por minuto |
|---|---|---|
| Bicicleta común | $200 | $20 |
| Bicicleta eléctrica | $300 | $45 |
| Monopatín eléctrico | $300 | $50 |

**Recorrido sugerido para probar el sistema:**
1. Usuario → Registrar usuario (por ejemplo: Juan, 12345678, juan@mail.com).
2. Usuario → Agregar saldo: documento 12345678, importe 20000, medio de pago "Débito".
3. Usuario → Retirar vehículo: documento 12345678, estación E01, vehículo BC01.
4. Usuario → Devolver vehículo: documento 12345678, estación E02. Como es su primer viaje y dura menos de 15 minutos, el costo es $0.
5. Usuario → Plan mensual: documento 12345678 (necesita al menos $15.000 de saldo).
6. Administrador → Buscar usuario: documento 12345678, muestra sus datos y su saldo.
7. Administrador → Reportes: viajes del usuario 12345678 y uso de la estación E02.

### Pruebas unitarias:
Están en `/test`. Cada clase tiene un `main` que ejecuta los casos e imprime el resultado por consola:
- `usuarios/CuentaTest`: recarga y débito de saldo.
- `usuarios/UsuarioTest`: condiciones de retiro y acreditación de saldo.
- `viajes/ViajeTest`: cambio de estado de los vehículos al finalizar un viaje, cálculo del costo y promoción de primer viaje.

Las pruebas funcionales (casos con entrada, resultado esperado y obtenido) están en `docs/plan_de_pruebas.md`.

### Estructura del proyecto:
- `/docs`: documentación general (`informe_final.md` y `plan_de_pruebas.md`, además del documento de la Etapa 1 en PDF).
  - `/docs/uml`: diagramas UML (archivos fuente y exportaciones de imágenes).
- `/datos`: archivos de persistencia.
- `/src`: código fuente en Java, organizado en paquetes:
  - `controladores`: orquestan los casos de uso usando el repositorio (`ControladorCuenta`, `ControladorViajes`, `ControladorEstacion`, `ControladorReporte`).
  - `excepciones`: excepciones propias para las reglas de negocio (`SaldoInsuficienteException`, `ImporteInvalidoException`, `VehiculoNoDisponibleException`, `PlanMensualActivoException`, `UsuarioNoEncontradoException`, `EstacionNoEncontradaException`, `RetiroNoPermitidoException`, `NoTieneViajeAbiertoException`).
  - `menu`: lógica de cada pantalla (`Menu` abstracta, `MenuUsuario`, `MenuAdmin`).
  - `repositorio`: guarda en memoria usuarios y estaciones, y los lee y escribe en archivos (`Repositorio`).
  - `usuarios`: usuarios, cuentas y movimientos de saldo (`Usuario`, `Cuenta`, `Movimiento`).
  - `vehiculos`: jerarquía de vehículos, estados y estaciones (`Vehiculo`, `BicicletaComun`, `BicicletaElectrica`, `MonopatinElectrico`, `Estacion`, `Estado`).
  - `viajes`: viajes y promociones (`Viaje`, `Promocion`, `PrimerViaje`, `PlanMensual`).
  - `vistas`: ventana principal en Swing (`Swing.java`, `Swing.form`).
  - `Main`: arma el repositorio, los controladores y los menús, y abre la ventana.
- `/test`: pruebas unitarias.
