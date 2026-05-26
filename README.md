# 📋 Buscador de Eventos — Trabajo Final Java 2026

**Institución:** CILSA — Programa de Oportunidades de Formación  
**Materia:** Java 2026  
**Modalidad:** Grupal (entrega individual)  

---

## 📌 Descripción

Programa de consola escrito en **Java** que lee un archivo `eventos.csv` con datos de eventos culturales y permite al usuario **buscar y filtrar** esa información mediante un menú interactivo.

---

## ✅ Requisitos del trabajo y cómo se cumplen

| Requisito | Estado | Detalle |
|-----------|--------|---------|
| Lectura de archivo `.csv` | ✅ Obligatorio | Lee `eventos.csv` con `BufferedReader` |
| Búsqueda por criterio 1 | ✅ Obligatorio | Opción 1 → filtro por **Localidad** |
| Búsqueda por criterio 2 | ✅ Obligatorio | Opción 2 → filtro por **Tipo de Evento** |
| Menú con al menos 3 opciones | ✅ Obligatorio | Menú con 4 opciones |
| Guardar resultados en CSV | ✅ Opcional | Opción 3 → exporta a un nuevo `.csv` |

---

## 📁 Estructura del proyecto

```
BuscadorEventos/
│
├── src/
│   └── BuscadorEventos.java    ← Código fuente principal
│
├── eventos.csv                 ← Archivo de datos (va en la RAÍZ)
│
├── README.md                   ← Este archivo
│
└── [resultado].csv             ← Se genera al usar la opción 3
```

> ⚠️ **Importante:** `eventos.csv` debe estar en la **carpeta raíz** del proyecto,  
> **NO** dentro de la carpeta `src/`.

---

## 🗂️ Estructura del archivo `eventos.csv`

El archivo usa **punto y coma** (`;`) como separador y tiene 5 columnas:

```
Localidad;Tipo de evento;Organizador;Nombre del contacto;Teléfono
Campana;Concierto;MúsicaCampana;Laura;11112222
Pilar;Taller;ArtePilar;Juan;22221111
Lujan;Conferencia;SaberesLujan;Inés;33331111
...
```

### Localidades disponibles
`Campana` · `Pilar` · `Lujan` · `Zárate` · `Tigre` · `San Fernando` · `Moron` · `Quilmes` · `Lanus` · `Tres de Febrero`

### Tipos de evento disponibles
`Concierto` · `Taller` · `Conferencia` · `Exposición` · `Feria` · `Festival`

---

## 🖥️ Cómo ejecutar en NetBeans — Paso a paso

### Paso 1 — Crear el proyecto

1. Abrir **NetBeans**
2. Ir a **Archivo → Nuevo Proyecto** (`File → New Project`)
3. Categoría: **Java** → Tipo: **Java Application**
4. Clic en **Siguiente**
5. Nombre del proyecto: `BuscadorEventos`
6. ❌ **Desmarcar** la casilla *"Crear clase principal"*
7. Clic en **Finalizar**

---

### Paso 2 — Agregar el archivo Java

1. En el panel **Projects** (izquierda), expandir el proyecto
2. Clic derecho sobre **Source Packages**
3. Seleccionar **Nuevo → Clase Java**
4. Nombre de la clase: `BuscadorEventos`
5. Dejar el campo **Paquete** completamente **vacío**
6. Clic en **Finalizar**
7. **Borrar** todo el código que NetBeans genera automáticamente
8. **Pegar** todo el contenido de `BuscadorEventos.java`

---

### Paso 3 — Ubicar el archivo CSV

1. Con el proyecto abierto, clic derecho sobre el **nombre del proyecto**
2. Seleccionar **Propiedades** → copiar el valor de **"Project Folder"**  
   Ejemplo: `C:\Users\TuNombre\Documents\NetBeansProjects\BuscadorEventos`
3. Abrir esa carpeta en el **Explorador de Windows**
4. **Copiar** `eventos.csv` directamente en esa carpeta (no en `src/`)

```
✅ Correcto:   BuscadorEventos/eventos.csv
❌ Incorrecto: BuscadorEventos/src/eventos.csv
```

---

### Paso 4 — Ejecutar

1. Clic derecho sobre `BuscadorEventos.java` en el panel de proyectos
2. Seleccionar **"Ejecutar archivo"** (o presionar `Shift + F6`)
3. La consola aparece en la parte inferior (**panel Output**)
4. **Hacer clic dentro de la consola** para poder escribir

---

## 🔄 Flujo del programa

```
┌─────────────────────────────────────────┐
│   Inicio: carga eventos.csv en memoria  │
└────────────────────┬────────────────────┘
                     │
                     ▼
        ┌────────────────────────┐
        │   Mostrar menú         │◄──────────────┐
        └────────────┬───────────┘               │
                     │                           │
          ┌──────────┼──────────┐                │
          ▼          ▼          ▼                │
       [1] Local  [2] Tipo   [3] Guardar         │
          │          │          │                │
          ▼          ▼          ▼                │
       Pide texto  Pide tipo  ¿Hay resultado?    │
          │          │          │                │
          ▼          ▼        Sí│  No→ Avisa     │
       Filtra     Filtra        ▼                │
       lista      lista    Pide nombre           │
          │          │      de archivo           │
          └────┬─────┘          │                │
               ▼                ▼                │
          Muestra          Crea .csv             │
          resultados                             │
               └────────────────────────────────┘
                                            [4] Salir → Fin
```

---

## 💻 Menú del programa

```
╔══════════════════════════════════════╗
║     BUSCADOR DE EVENTOS - CILSA      ║
╠══════════════════════════════════════╣
║  1. Buscar por Localidad             ║
║  2. Buscar por Tipo de Evento        ║
║  3. Guardar ultimo resultado en CSV  ║
║  4. Salir                            ║
╚══════════════════════════════════════╝
  Ingrese una opcion:
```

---

## 📖 Explicación del código — Conceptos usados

### `import` — Importaciones

```java
import java.io.*;                         // Leer y escribir archivos
import java.nio.charset.StandardCharsets; // Soporte de UTF-8 (tildes, ñ)
import java.util.ArrayList;              // Lista dinámica
import java.util.List;                   // Interfaz de lista
import java.util.Scanner;                // Leer teclado
```

Las importaciones le dicen a Java qué herramientas del lenguaje vamos a usar.  
Sin ellas, el compilador no encuentra las clases `Scanner`, `ArrayList`, etc.

---

### Clase interna `Evento` — Modelo de datos

```java
static class Evento {
    String localidad;
    String tipoEvento;
    String organizador;
    String contacto;
    String telefono;
    ...
}
```

Cada objeto `Evento` representa **una fila del CSV**.  
En vez de trabajar con arreglos de texto sueltos, agrupamos los datos en una clase con nombre claro. Esto hace el código más legible y organizado.

---

### `leerCSV()` — Lectura del archivo

```java
BufferedReader br = new BufferedReader(
    new InputStreamReader(
        new FileInputStream(rutaArchivo),
        StandardCharsets.UTF_8));
```

| Clase | Qué hace |
|-------|----------|
| `FileInputStream` | Abre el archivo y lee sus bytes crudos |
| `InputStreamReader` | Convierte esos bytes en caracteres usando UTF-8 |
| `BufferedReader` | Carga el archivo en memoria para leer línea por línea más rápido |

El **BOM** (`\uFEFF`) es un carácter invisible que agregan algunos programas (como Excel) al inicio de archivos CSV. Lo eliminamos para no corromper el primer dato.

```java
linea = linea.replace("\uFEFF", ""); // Eliminar BOM si existe
```

---

### `buscarPorLocalidad()` y `buscarPorTipo()` — Filtrado

```java
for (Evento e : eventos) {
    if (e.localidad.equalsIgnoreCase(localidad)) {
        resultados.add(e);
    }
}
```

- El **for-each** recorre cada elemento de la lista sin necesitar índices.
- `equalsIgnoreCase()` compara texto **sin importar mayúsculas/minúsculas**,  
  así `"campana"`, `"Campana"` y `"CAMPANA"` dan el mismo resultado.

---

### `guardarResultados()` — Escritura de archivo

```java
PrintWriter pw = new PrintWriter(
    new OutputStreamWriter(
        new FileOutputStream(nombreArchivo),
        StandardCharsets.UTF_8));
pw.println("encabezado...");
for (Evento e : resultados) {
    pw.println(e.toCsv());
}
```

Crea un nuevo archivo CSV con los eventos filtrados.  
El método `toCsv()` de cada `Evento` devuelve la fila en formato `campo1;campo2;...`.

---

### `switch` — Menú de opciones

```java
switch (opcionStr) {
    case "1": /* buscar por localidad */ break;
    case "2": /* buscar por tipo */      break;
    case "3": /* guardar */              break;
    case "4": continuar = false;         break;
    default:  /* opción inválida */      break;
}
```

El `switch` evalúa el texto ingresado y ejecuta el bloque correspondiente.  
`break` es obligatorio para que no "caiga" al siguiente `case`.  
`default` maneja cualquier entrada inesperada.

---

### `try-with-resources` — Manejo seguro de archivos

```java
try (BufferedReader br = new BufferedReader(...)) {
    // usar br
} catch (FileNotFoundException e) {
    // archivo no encontrado
} catch (IOException e) {
    // otro error de lectura
}
```

El `try-with-resources` **cierra automáticamente** el archivo al terminar el bloque,  
aunque ocurra un error. Es la forma correcta de trabajar con archivos en Java  
para evitar dejar recursos abiertos en memoria.

---

## ❓ Solución de problemas frecuentes

| Problema | Causa | Solución |
|----------|-------|----------|
| `[ERROR] No se encontro el archivo` | `eventos.csv` no está en la carpeta raíz | Mover el CSV a `BuscadorEventos/eventos.csv` |
| No puedo escribir en la consola | No está seleccionada | Hacer clic dentro del panel **Output** |
| Aparecen `?` en lugar de tildes | Encoding incorrecto en NetBeans | Ir a **Herramientas → Opciones → Editor → General → Encoding: UTF-8** |
| El programa no arranca | Clase principal no configurada | Clic derecho en el proyecto → **Propiedades → Run → Main Class → BuscadorEventos** |

---

## 🧪 Ejemplo de ejecución

```
Cargando datos desde eventos.csv...
  10 eventos cargados correctamente.

╔══════════════════════════════════════╗
║     BUSCADOR DE EVENTOS - CILSA      ║
╠══════════════════════════════════════╣
║  1. Buscar por Localidad             ║
║  2. Buscar por Tipo de Evento        ║
║  3. Guardar ultimo resultado en CSV  ║
║  4. Salir                            ║
╚══════════════════════════════════════╝
  Ingrese una opcion: 1

  Ingrese la localidad a buscar: Campana

==========================================
 Resultados para Localidad: "Campana"
==========================================
  Se encontraron 1 evento(s):

  Localidad: Campana               | Tipo: Concierto        | Organizador: MúsicaCampana              | Contacto: Laura      | Tel: 11112222
==========================================

  Ingrese una opcion: 3

  Nombre del archivo a guardar (sin extension): mis_resultados

  Resultados guardados correctamente en: mis_resultados.csv
```

---

*Trabajo Final — Java 2026 — CILSA*
