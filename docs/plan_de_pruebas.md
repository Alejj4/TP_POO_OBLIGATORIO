# Plan de pruebas funcionales – TP POO, Etapa 2

**Sistema:** movilidad compartida (bicicletas comunes/eléctricas y monopatines eléctricos).
**Cómo se ejecutaron:** los casos se corrieron contra el código final (controladores + modelo) con los datos precargados de `datos/` (estaciones E01–E03 y los 8 vehículos). Para simular la duración de los viajes se fijó la hora de inicio en el pasado (`Viaje.setFechaHoraInicio`), sin esperar minutos reales. Los resultados de la columna "Obtenido" son la salida real de esa corrida.
**Pruebas por interfaz (Swing):** ver la sección 9, que el grupo completa a mano.

Datos base: usuario Juan (DNI 12345678) con saldo cargado según cada caso. Saldo mínimo para retirar = costo de 30 minutos del vehículo (bicicleta común $800).

## 1. Usuarios

| ID | Caso | Entrada | Resultado esperado | Obtenido | Estado |
|---|---|---|---|---|---|
| U1 | Registrar usuario válido | Juan, 12345678, juan@mail.com | Se crea con saldo $0 y sin plan | `Juan (DNI 12345678) … Saldo: $0.0 - Plan mensual: No` | OK |
| U2 | Documento duplicado | Mismo DNI 12345678 | Se rechaza | "Ya existe un usuario con el documento 12345678" | OK |
| U3 | Nombre vacío | "", 111 | Se rechaza | "Debe ingresar un nombre válido" | OK |
| U4 | Email inválido | email sin "@" | Se rechaza | "El email no es válido" | OK |

## 2. Saldo

| ID | Caso | Entrada | Esperado | Obtenido | Estado |
|---|---|---|---|---|---|
| S1 | Agregar saldo | $5000, "Debito" | Saldo $5000 | Saldo 5000.0 | OK |
| S2 | Importe cero | $0 | Se rechaza | "El importe debe ser mayor a 0" | OK |
| S3 | Importe negativo | -$50 | Se rechaza | "El importe debe ser mayor a 0" | OK |
| S4 | Usuario inexistente | DNI 99999999 | Se rechaza | "No existe un usuario con documento 99999999" | OK |

## 3. Retiro de vehículo

| ID | Caso | Entrada | Esperado | Obtenido | Estado |
|---|---|---|---|---|---|
| R1 | Saldo menor al mínimo | Usuario con $0, BC01 en E01 | Se rechaza e informa el mínimo | "necesitás al menos $800.0 para retirar este vehículo" | OK |
| R2 | Vehículo que no está en la estación | ZZ99 en E01 | Se rechaza | "El vehículo ZZ99 no está en la estación E01" | OK |
| R3 | Estación inexistente | E99 | Se rechaza | "No existe la estación E99" | OK |
| R4 | Usuario inexistente | DNI 00000 | Se rechaza | "No existe un usuario con documento 00000" | OK |
| R5 | Retiro correcto | Juan, BC01, E01 | Se abre el viaje, BC01 sale de E01 y queda no disponible | BC01 ya no está en E01; no disponible | OK |
| R6 | Segundo retiro con viaje abierto | Juan, BC02, E01 | Se rechaza | "Ya tenés un viaje en curso. Devolvé el vehículo antes de retirar otro." | OK |

## 4. Devolución y tarifas

| ID | Caso | Entrada | Esperado | Obtenido | Estado |
|---|---|---|---|---|---|
| D1 | Primer viaje, 10 min | BC01, devolver en E02 | $0 (15 min gratis) y BC01 queda en E02 | $0.0; BC01 en E02 | OK |
| D2 | Devolver sin viaje abierto | Juan, E01 | Se rechaza | "No tenés ningún viaje en curso." | OK |
| D3 | Segundo viaje, bici común 10 min | – | 200 + 20×10 = $400 | $400.0 | OK |
| D4 | Viaje cancelado (≤ 2 min) | bici común 2 min | $0 | $0.0 | OK |
| D5 | Bici eléctrica 60 min | – | 300 + 45×60 = $3000 | $3000.0 | OK |
| D6 | Monopatín 30 min | – | 300 + 50×30 = $1800 | $1800.0 | OK |
| D7 | Monopatín 150 min (excedente ×1,5) | – | 300 + 50×120 + 75×30 = $8550 | $8550.0 | OK |
| D8 | Bici común 1500 min (> 24 h) | – | Tarifa fija $50.000 | $50000.0 | OK |
| D8b | Vehículo tras > 24 h | – | Queda fuera de servicio | No disponible | OK |

## 5. Batería

| ID | Caso | Entrada | Esperado | Obtenido | Estado |
|---|---|---|---|---|---|
| B1 | Batería inicial | BE02 | 100 | 100 | OK |
| B2 | Viaje de 180 min | BE02 | −0,5 por minuto → 10; queda con batería baja | 10; no disponible | OK |
| B3 | Retirar con batería baja | BE02 | Se rechaza | "El vehículo seleccionado no se encuentra disponible." | OK |

## 6. Plan mensual

| ID | Caso | Entrada | Esperado | Obtenido | Estado |
|---|---|---|---|---|---|
| P1 | Suscribir sin saldo | Usuario con $0 | Se rechaza | "Saldo insuficiente" | OK |
| P2 | Suscribir con saldo | saldo $31.500 | Se debitan $15.000 | 31500 → 16500 | OK |
| P3 | Suscribir con plan activo | – | Se rechaza | "Ya ténes el plan mensual." | OK |
| P4 | Viaje de 25 min con plan | bici común | $0 | $0.0 | OK |
| P5 | Viaje de 45 min con plan | bici común | Se cobran solo los 15 min excedentes: 20×15 = $300 | $300.0 | OK |
| P6 | Llegar a 20 viajes con plan | 20 viajes | Usados = 20 y el plan se desactiva | usados 20, plan activo: false | OK |
| P7 | Viaje número 21 | 10 min | Sin plan: tarifa común $400 | $400.0 | OK |
| P8 | Volver a suscribirse tras el tope | – | Permitido; contador en 0 | usados 0, plan activo | OK |

## 7. Estaciones y reportes del administrador

| ID | Caso | Entrada | Esperado | Obtenido | Estado |
|---|---|---|---|---|---|
| E1 | Registrar estación | E04, Plaza Mayor, cap. 5 | Se crea | `E04 - Plaza Mayor (capacidad 5)` | OK |
| E2 | Código duplicado | E04 | Se rechaza | "Ya existe una estación con el código E04" | OK |
| E3 | Capacidad 0 | E05, cap. 0 | Se rechaza | "La capacidad máxima debe ser mayor a 0" | OK |
| E4 | Orden por capacidad | – | De mayor a menor | E01(10) E02(8) E03(6) E04(5) | OK |
| RP1 | Reporte de viajes de un usuario | Juan | Más recientes primero (`Comparable`) | 29 viajes, orden correcto | OK |
| RP2 | Reporte de usuario inexistente | DNI 0 | Se rechaza | "No existe el usuario 0" | OK |
| RP3 | Reporte de uso de una estación | E01 | Viajes que salieron o llegaron a E01 | 28 viajes | OK |
| RP4 | Reporte de estación inexistente | E99 | Se rechaza | "No existe la estación E99" | OK |

## 8. Persistencia y limitaciones conocidas

| ID | Caso | Esperado | Obtenido | Estado |
|---|---|---|---|---|
| PE1 | Reiniciar el programa | Se recuperan usuarios, saldo, plan y viajes con plan usados | 2 usuarios; Juan saldo $800, plan activo, usados 0 | OK |
| PE2 | Reiniciar: estaciones | Se recuperan las precargadas y las agregadas | 4 estaciones (E01–E03 + E04) | OK |
| L1 | Capacidad de la estación al devolver | (limitación) | En una estación de capacidad 1 se devolvieron 2 vehículos: queda con 2 | **Limitación aceptada** |
| L2 | Historial de viajes y posición de los vehículos al reiniciar | (limitación) | No se guardan: al reiniciar los vehículos vuelven a las estaciones de `vehiculos.txt` y la batería a 100 | **Limitación aceptada** |

**L1:** se decidió no impedir la devolución cuando la estación está llena, para que el usuario nunca quede con un vehículo en la mano (la redistribución sería tarea de los empleados, que quedaron fuera del alcance). `Estacion.tieneLugarDisponible()` queda disponible para una mejora futura.
**L2:** el enunciado sólo pide persistir usuarios, cuentas, estaciones y vehículos precargados.

## 9. Pruebas unitarias (carpeta `test/`)

| Clase | Qué verifica |
|---|---|
| `usuarios/CuentaTest` | Recarga y débito de saldo |
| `usuarios/UsuarioTest` | Condiciones de retiro y acreditación de saldo |
| `viajes/ViajeTest` | Estado de los vehículos al finalizar, costo y promoción de primer viaje |
