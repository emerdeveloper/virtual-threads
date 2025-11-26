# Virtual Threads en Java: Mejora de la Escalabilidad en Aplicaciones Web

Este proyecto demuestra cómo los **Virtual Threads** introducidos en Java 21 y mejorados en Java 24 pueden transformar la forma en que se manejan las solicitudes concurrentes en aplicaciones web, especialmente en entornos como **Spring Boot**.

## 🧵 Introducción a los hilos en Java

Tradicionalmente, Java ha utilizado **Platform Threads**, que son mapeados directamente a hilos del sistema operativo. Cada solicitud HTTP en un servidor web como Tomcat consume un hilo, lo que limita la escalabilidad: si el número de solicitudes concurrentes supera el número de hilos disponibles, el servidor se bloquea o degrada su rendimiento.

![Threads](/docs/threads.png)

![Web Request](/docs/web-request.png)

Con la llegada de los **Virtual Threads** en Java 21, se introduce un modelo de concurrencia más ligero, donde cada tarea puede ejecutarse en un hilo virtual que no consume directamente recursos del sistema operativo. Esto permite manejar miles de solicitudes concurrentes sin saturar el sistema.

![Threads](/docs/virtual-threads.png)

![Web Request](/docs/web-request-v-t.png)

> ⚠️ En Java 21, los Virtual Threads aún sufrían de *pinning* cuando se ejecutaban bloques `synchronized`, lo que impedía liberar el hilo físico. Este problema fue solucionado en Java 24, permitiendo una verdadera concurrencia sin bloqueo.

## 🚀 Objetivo del proyecto

Demostrar mediante pruebas prácticas cómo los Virtual Threads mejoran la escalabilidad de aplicaciones web Java, comparando su comportamiento en Java 21 y Java 24, y analizando cómo interactúan con bloques sincronizados y operaciones bloqueantes como acceso a base de datos.

## 🏗️ Estructura del proyecto

```
virtual-threads/ 
├── deployment/ # Pruebas de rendimiento con JMeter 
├── gradle/ # Configuración de Gradle Wrapper 
├── src/ 
│ └── main/java/com/emerdeveloper/
│   ├── VirtualThreadsApplication.java # Clase principal de Spring Boot 
│   ├── controller/ 
│   │ └── OrderController.java # Endpoints REST simulando operaciones bloqueantes y sincronizadas 
│   └── service/ 
│     ├── DatabaseService.java # Simula operaciones lentas de la base de datos con Thread.sleep(100ms)
│     └── OrderService.java # Lógica de negocio simulada con delays y bloques synchronized 
├── build.gradle # Configuración del proyecto 
└── settings.gradle # Configuración de módulos
```


## 🌐 Endpoints REST

La aplicación expone endpoints funcionales que pueden ser consumidos directamente al ejecutar el proyecto:

- `@PostMapping("/{orderId}")`: Simula una operación sincronizada.
- `@GetMapping("/{orderId}")`: Simula una operación bloqueante de base de datos (3 segundos de espera).
- `@GetMapping("/api/simulate-calls/{seconds}")`: Simula una operación bloqueante parametrizando el tiempo del bloqueo `{seconds}`.
  
## 📊 Pruebas de rendimiento (carpeta `deployment/`)

Se realizaron pruebas con **JMeter** para simular distintos escenarios de concurrencia. 

**Escenario 1:** Simular 12 solicitudes bloqueantes de 3 segundos, ejecutadas secuencialmente.
* Thread Group:
    - Number of Threads (users): 1
    - Loop Count: 12
    - Ramp-Up Period: 0
    - Resultado esperado: ~36 segundos (12 × 3)

    ![Scenario](/docs/scenario_1.png)

**Escenario 2:** Simular 2 usuarios concurrentes enviando 6 solicitudes cada uno.
* Thread Group:
    - Number of Threads (users): 2
    - Loop Count: 6
    - Ramp-Up Period: 0
    - Resultado esperado: ~18 segundos (6 × 3)

  ![Scenario](/docs/scenario_2.png)

**Escenario 3:** Simular 12 usuarios concurrentes enviando 1 solicitud cada uno.
* Thread Group:
    - Number of Threads (users): 12
    - Loop Count: 1
    - Ramp-Up Period: 0
    - Resultado esperado: ~3 segundos (todas procesadas en paralelo)

  ![Scenario](/docs/scenario_3.png)

**Escenario 4:** Simular 24 usuarios concurrentes enviando 3 solicitudes cada uno.
* Thread Group:
    - Number of Threads (users): 24
    - Loop Count: 3
    - Ramp-Up Period: 0
    - Resultado esperado: ~18 segundos (procesamiento en bloques de 12 cada 3 segundos)

  ![Scenario](/docs/scenario_4.png)

A continuación se describen los más relevantes:

| Escenario | Descripción | Sin Virtual Threads (Platform Threads) | Java 21 (Virtual Threads) | Java 24 (Virtual Threads) |
|-----------|-------------|----------------------------------------|----------------------------|----------------------------|
| 5         | 1,200 usuarios × 1 solicitud | Saturación inmediata del pool de hilos. Alto tiempo de respuesta. | Excelente rendimiento. Manejo fluido de concurrencia. | Excelente rendimiento. Manejo fluido de concurrencia. |
| 6         | 1,200 usuarios × 10 solicitudes con bloque `synchronized` | Bloqueo severo. Los hilos se quedan esperando. | Afectado por *pinning*. Los hilos virtuales se comportan como físicos. | Sin *pinning*. Ejecución concurrente eficiente. |
| 7         | 1,200 usuarios × 10 solicitudes con delay de 3s (simulación de acceso a DB) | Alto consumo de hilos. Tiempo de espera acumulado. | Buen rendimiento, pero con limitaciones si se combinan con `synchronized`. | Excelente rendimiento. No hay bloqueo ni saturación. |
> Los escenarios 1–4 también muestran mejoras progresivas en concurrencia, pero los escenarios 5–7 son clave para comparar Java 21 vs Java 24.

## 📈 Resultados y gráficos

**Escenario 5:** 
* Thread Group:
    - Number of Threads (users): 1,200
    - Loop Count: 1
    - Ramp-Up Period: 4


* Resultados:
    - Resumen de la prueba
    ![Scenario](/docs/scenario_5_resume.png)
    - Usuarios activos a lo largo del tiempo 
    ![Scenario](/docs/scenario_5_active_users.png)
    - Tiempos de respuesta a lo largo del tiempo
    ![Scenario](/docs/scenario_5_response_time.png)
    - TPS
    ![Scenario](/docs/scenario_5_tps.png)
  

## ⚙️ Configuración del servidor

Para ejemplificar el comportamiento de los Platform Threads, se limitó el pool de Tomcat a **12 hilos**, en concordancia con la arquitectura de la máquina de pruebas:

- Total de núcleos: 12 (6 de rendimiento + 6 de eficiencia)

Esto permite observar cómo el servidor se bloquea al recibir más de 12 solicitudes simultáneas bajo Platform Threads, mientras que con Virtual Threads se mantiene fluido.

## 📚 Recursos recomendados

- [JEP 444: Virtual Threads (Java 21)](https://openjdk.org/jeps/444) — Introduce los hilos virtuales como una característica final del JDK. Permiten manejar miles de tareas concurrentes con una huella mínima de recursos.
- [JEP 491: Synchronize Virtual Threads without Pinning (Java 24)](https://openjdk.org/jeps/491) — Elimina el problema del *pinning* en bloques `synchronized`, permitiendo que los hilos virtuales liberen el hilo físico subyacente.
- [JEP 453: Structured Concurrency (Preview)](https://openjdk.org/jeps/453) — Propone una API para manejar tareas concurrentes como una unidad estructurada, mejorando la legibilidad y el control de errores.


## 🧪 Cómo ejecutar

1. Ejecuta con Java 21 o Java 24 según el escenario:
   ```bash
   ./gradlew bootRun
