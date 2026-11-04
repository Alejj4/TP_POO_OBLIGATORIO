# Informe final – TP POO, Etapa 2

**Integrantes:** 
- Aranguren Alejo (1215686),
- Raggio Gonzalo (1214922),
- Schipani Tiziano (1214840)

**Propuesta elegida:** C – Sistema de movilidad compartida (bicicletas comunes y eléctricas, monopatines eléctricos).

## 1. Qué se implementó

- Registro de usuarios y carga de saldo (el medio de pago se asocia en la primera recarga).
- Retiro de vehículo con control de saldo mínimo (costo de 30 minutos), de viaje abierto y de disponibilidad (estado y batería).
- Devolución en cualquier estación: cálculo de tarifa por tipo de vehículo, promociones y débito en la cuenta.
- Plan mensual ($15.000): viajes de hasta 30 min gratis, tope de 20 viajes; si se pasa de 30 min se cobran sólo los minutos excedentes. Al usar los 20 viajes el plan se desactiva y el usuario puede volver a suscribirse.
- Promoción de primer viaje (15 min gratis).
- Administrador: buscar usuarios, registrar estaciones, reporte de viajes por usuario (más recientes primero) y reporte de uso por estación.
- Persistencia en archivos de texto (`datos/`), interfaz gráfica en Swing y pruebas (ver `plan_de_pruebas.md`).

**Alcance recortado respecto de la Etapa 1.** Acordamos no implementar técnicos, empleados/operadores, órdenes de mantenimiento, reporte de fallas ni redistribución de vehículos. El enunciado pide una parte reducida del diseño y preferimos tener lo implementado bien probado. Por eso se eliminaron esas clases del código y del diagrama.

## 2. Cambios respecto del diseño de la Etapa 1

| Tema | Etapa 1 | Etapa 2 (final) | Motivo |
|---|---|---|---|
| Interfaz | `MenuConsola` único | `Menu` abstracta con `MenuUsuario` y `MenuAdmin`, y ventanas Swing (`Swing.java`/`Swing.form`) | Separar casos de uso y tener una interfaz usable |
| Controladores | `ControladorMantenimiento` | `ControladorCuenta`, `ControladorViajes`, `ControladorEstacion`, `ControladorReporte` | Los técnicos/mantenimiento quedaron fuera del alcance |
| Administrador | Clase `Administrador` | Eliminada | No tenía datos ni comportamiento propio; las operaciones están en los controladores |
| Reportes | Lógica en el administrador/controlador | El controlador busca y valida; la lista se arma en `Repositorio.viajesDeEstacion` y el orden lo define `Viaje.compareTo` | Experto: cada clase hace lo que conoce |
| Devolución | Lógica de promoción dentro de un `if` | El controlador elige la `Promocion` (`PlanMensual` o `PrimerViaje`) y `Viaje.calcularCosto(promo)` la aplica | Polimorfismo |
| Plan mensual | Vencía por fecha | No vence por fecha: se desactiva solo al usar 20 viajes | Evita manejar fechas; regla más simple de probar |
| Viaje abierto | Sin excepción propia | `NoTieneViajeAbiertoException` | Mensaje claro al devolver sin haber retirado |
| Persistencia | Sin definir | `usuarios.txt`, `estaciones.txt`, `vehiculos.txt` | Requisito de la Etapa 2 |
| Orden | – | `Comparable` en `Viaje` y `Comparator` en `ControladorEstacion` | Ordenar reportes y estaciones |

Los diagramas actualizados están en `docs/uml` (`diagrama_clases_final.jpg`).

## 3. Patrones GRASP y principios SOLID

**GRASP (elegidos en la Etapa 1):**
- **Creador:** `Usuario.crearViaje` crea el `Viaje` porque el usuario contiene sus viajes y tiene los datos para inicializarlo.
- **Experto:** `Usuario` sabe si tiene un viaje abierto y cuántos viajes previos tiene; `Viaje` calcula su duración y costo; `Estacion` busca sus vehículos; `Repositorio` encuentra los viajes de una estación.
- **Controlador:** `ControladorCuenta`, `ControladorViajes`, `ControladorEstacion` y `ControladorReporte` reciben las operaciones de los menús, buscan los objetos, validan y delegan.
- **Polimorfismo:** `Vehiculo` (abstracta) con `calcularCosto` y `registrarUso` propios de cada tipo; `Promocion` con `PrimerViaje` y `PlanMensual`.
- **Fabricación pura:** `Repositorio`, que se ocupa de guardar y leer archivos sin que el modelo tenga que saberlo.

**SOLID:**
- **S (responsabilidad única):** cada clase tiene una razón de cambio (cuenta, viaje, tarifa, persistencia, menú).
- **O (abierto/cerrado):** agregar un nuevo tipo de vehículo o una nueva promoción no obliga a modificar `Viaje` ni los controladores.
- **L (sustitución de Liskov):** cualquier `Vehiculo` y cualquier `Promocion` se puede usar donde se espera su tipo base.

## 4. Decisiones y supuestos

1. Saldo mínimo para retirar = costo de 30 minutos del vehículo elegido.
2. Hasta 2 minutos el viaje es gratis y se considera cancelado (no cuenta como "primer viaje" ni como viaje con plan).
3. Pasados 120 minutos, cada minuto excedente cuesta 1,5 veces; pasadas 24 horas se cobra una tarifa fija y el vehículo queda fuera de servicio.
4. Plan mensual: $15.000, no vence por fecha; queda activo hasta usar 20 viajes. Si un viaje supera los 30 minutos se cobran los minutos excedentes.
5. Batería: −0,5% por minuto; menos de 15% el vehículo no se puede retirar.
6. Los reportes muestran los viajes más recientes primero.

## 5. Dificultades y cómo las resolvimos

- **Reglas del plan mensual:** el contador de viajes gratis llegaba a 21 en vez de 20 y la cancelación en los primeros 2 minutos no se contemplaba. Se agregó un contador de viajes con plan por usuario, que se guarda en el archivo, y la desactivación al llegar al tope.
- **Lógica en los controladores:** al revisar el diseño notamos que `ControladorReporte` y `ControladorViajes` tenían lógica que correspondía a otras clases. La movimos al modelo (`Repositorio`, `Usuario`, `Viaje`) y los controladores quedaron sólo con búsqueda, validación y delegación.
- **Polimorfismo en `Viaje.finalizar`:** el comportamiento por tipo de vehículo se resolvía con condicionales; se pasó a `registrarUso` en cada subclase.
- **Persistencia:** hubo una ruta mal escrita y conflictos entre ramas por `usuarios.txt`; lo resolvimos con rutas relativas a la raíz del proyecto y agregando `datos/usuarios.txt` al `.gitignore`.
- **Trabajo en paralelo con Git:** conflictos de merge en `Usuario` y en archivos de IntelliJ (`.idea`); los resolvimos con ramas por integrante, pull requests revisados entre nosotros y dejando de versionar archivos de configuración del IDE.

## 6. Limitaciones conocidas

- Una estación puede quedar por encima de su capacidad máxima si se devuelven más vehículos que lugares (se eliminó el control a propósito; ver plan de pruebas, caso L1).
- Los viajes, la posición de los vehículos y la batería no se guardan entre ejecuciones (caso L2). Sí se guardan usuarios, saldo, plan y viajes con plan usados.
- Las pruebas unitarias son clases con `main`.

## 7. Distribución de tareas

El reparto surge del historial de commits y pull requests del repositorio.

| Integrante | Principales aportes |
|---|---|
| **Aranguren Alejo** | `Usuario`, `Cuenta`/`Movimiento`, promociones (`PlanMensual`, `PrimerViaje`) y suscripción al plan, menús (`Menu`, `MenuUsuario`, `MenuAdmin`), persistencia de estaciones/vehículos, interfaz Swing (formulario, iconos), pruebas de `Cuenta` y `Usuario`, README y diagrama de clases final, ajustes de patrón Controlador/Experto |
| **Raggio Gonzalo** | Jerarquía de vehículos y enum de estados, tarifas y consumo de batería, `Viaje.finalizar`, `Estacion`, `ControladorViajes.devolverVehiculo`, primeras vistas Swing, `Comparable`/`Comparator`, pruebas de `Viaje`, mensajes de excepciones y `finally` |
| **Schipani Tiziano** | `Repositorio` y archivo de usuarios, `ControladorCuenta`, `ControladorViajes`, `ControladorEstacion`, `ControladorReporte` y reportes en la interfaz, excepciones propias, corrección del plan mensual y de la cancelación de viajes, correcciones de polimorfismo en `Viaje` |

## 8. Reflexión

Gracias a este trabajo, pudimos ver la utilidad que tienen los distintos diagramas (secuencia y de clases) y cómo estos nos facilitaron mucho trabajo a la hora de pasar a la codificación y pensar la lógica detrás de cada método. Gracias a los diferentes problemas que tuvimos, logramos aprender a ajustar el diseño. Separar responsabilidades (controladores que sólo coordinan, objetos que conocen sus datos) hizo que las reglas fueran más fáciles de cambiar y de probar. También vimos la importancia de acordar el alcance temprano, probar con casos concretos y coordinar el trabajo en Git con ramas y revisiones cruzadas. Con más tiempo implementaríamos los técnicos y la redistribución, persistiríamos los viajes.
