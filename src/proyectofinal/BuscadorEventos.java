import java.io.*;                        // Clases para leer y escribir archivos (File, BufferedReader, etc.)
import java.nio.charset.StandardCharsets; // Para indicar que usamos UTF-8 (soporta tildes y ñ)
import java.util.ArrayList;              // Clase para crear listas dinamicas (pueden crecer o achicarse)
import java.util.List;                   // Interfaz que representa una lista (la usamos como tipo de dato)
import java.util.Scanner;                // Clase para leer lo que escribe el usuario por teclado

// ============================================================
// JAVADOC DE LA CLASE PRINCIPAL
// El bloque /** ... */ es un comentario especial llamado
// "Javadoc". Documenta la clase para que otros programadores
// entiendan para que sirve sin leer todo el codigo.
// ============================================================

/**
 * Trabajo Final de Java 2026 - CILSA
 *
 * Descripcion:
 *   Programa que lee un archivo CSV con informacion de eventos
 *   culturales y permite al usuario buscarlos por dos criterios:
 *   Localidad y Tipo de Evento. Tambien puede guardar los
 *   resultados en un nuevo archivo CSV.
 *
 * Archivo de datos: eventos.csv
 *
 * Requisitos cubiertos:
 *   [OBLIGATORIO] 1 - Lectura desde archivo CSV
 *   [OBLIGATORIO] 2 - Menu con 4 opciones (2 busquedas + guardar + salir)
 *   [OPCIONAL]    3 - Guardar resultados en nuevo archivo CSV
 *
 * Autor: Trabajo Grupal - CILSA 2026
 */
public class BuscadorEventos {

    // ==========================================================
    // CLASE INTERNA: Evento
    //
    // Una "clase interna" es una clase definida DENTRO de otra.
    // La usamos aqui para representar cada fila del archivo CSV.
    //
    // La palabra "static" significa que no necesita una instancia
    // de BuscadorEventos para poder usarse.
    // ==========================================================
    static class Evento {

        // ----------------------------------------------------------
        // ATRIBUTOS (campos de datos)
        // Cada variable guarda un dato de la columna correspondiente
        // del archivo eventos.csv.
        //
        // Columnas del CSV:
        //   Localidad | Tipo de evento | Organizador | Contacto | Telefono
        // ----------------------------------------------------------
        String localidad;    // Ej: "Campana", "Pilar", "Lujan"
        String tipoEvento;   // Ej: "Concierto", "Taller", "Feria"
        String organizador;  // Ej: "MusicaCampana", "ArtePilar"
        String contacto;     // Ej: "Laura", "Juan", "Ines"
        String telefono;     // Ej: "11112222", "22221111"

        // ----------------------------------------------------------
        // CONSTRUCTOR
        // Un constructor es un metodo especial que se llama cuando
        // creamos un nuevo objeto con "new Evento(...)".
        // Recibe los 5 valores y los guarda en los atributos.
        //
        // .trim() elimina espacios en blanco al inicio y al final
        // de cada texto (por si el CSV tiene espacios de mas).
        // ----------------------------------------------------------
        public Evento(String localidad, String tipoEvento,
                      String organizador, String contacto, String telefono) {
            this.localidad   = localidad.trim();   // "this." diferencia el atributo del parametro
            this.tipoEvento  = tipoEvento.trim();
            this.organizador = organizador.trim();
            this.contacto    = contacto.trim();
            this.telefono    = telefono.trim();
        }

        // ----------------------------------------------------------
        // METODO: toCsv()
        // Convierte el objeto Evento en una linea de texto CSV.
        // Los campos se unen con punto y coma (;) igual que el
        // archivo original. Se usa al guardar resultados.
        //
        // Ejemplo de salida:
        //   Campana;Concierto;MusicaCampana;Laura;11112222
        // ----------------------------------------------------------
        public String toCsv() {
            return localidad + ";" + tipoEvento + ";" +
                   organizador + ";" + contacto + ";" + telefono;
        }

        // ----------------------------------------------------------
        // METODO: toString()
        // @Override indica que estamos redefiniendo un metodo que
        // ya existe en la clase Object (la clase base de todo en Java).
        // toString() se llama automaticamente cuando hacemos
        // System.out.println(unEvento).
        //
        // String.format() crea texto con formato fijo:
        //   %-20s = texto alineado a la izquierda en 20 caracteres
        // Esto hace que todas las filas queden alineadas en columnas.
        //
        // Ejemplo de salida:
        //   Localidad: Campana              | Tipo: Concierto    | ...
        // ----------------------------------------------------------
        @Override
        public String toString() {
            return String.format(
                "  Localidad: %-20s | Tipo: %-15s | Organizador: %-25s | Contacto: %-10s | Tel: %s",
                localidad, tipoEvento, organizador, contacto, telefono
            );
        }

    } // fin de la clase interna Evento


    // ==========================================================
    // METODO: leerCSV
    //
    // Se encarga de abrir el archivo CSV, leerlo linea por linea
    // y transformar cada linea en un objeto Evento.
    // Devuelve una List<Evento> con todos los eventos del archivo.
    //
    // Parametro:
    //   rutaArchivo - ruta o nombre del archivo a leer
    //
    // Retorno:
    //   List<Evento> - lista con todos los eventos leidos
    // ==========================================================
    static List<Evento> leerCSV(String rutaArchivo) {

        // Creamos una lista vacia donde vamos a guardar los eventos
        // ArrayList es una implementacion de List que puede crecer dinamicamente
        List<Evento> eventos = new ArrayList<>();

        // ----------------------------------------------------------
        // try-with-resources
        // La sintaxis "try (recurso)" garantiza que el archivo se
        // cierra automaticamente al terminar, aunque haya un error.
        // Es la forma correcta y segura de trabajar con archivos en Java.
        //
        // BufferedReader     = lector que carga el archivo en memoria (mas rapido)
        // InputStreamReader  = convierte los bytes del archivo en caracteres
        // FileInputStream    = abre el archivo desde el disco
        // StandardCharsets.UTF_8 = indica que el archivo usa codificacion UTF-8
        //                     (necesario para que las tildes y la n con tilde funcionen)
        // ----------------------------------------------------------
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(rutaArchivo),
                    StandardCharsets.UTF_8))) {

            String linea;             // Variable que guardara cada linea leida
            boolean primeraLinea = true; // Bandera para saber si estamos en la primer linea (encabezado)

            // Leer el archivo linea por linea hasta que no haya mas (readLine devuelve null)
            while ((linea = br.readLine()) != null) {

                if (primeraLinea) {
                    // BOM (Byte Order Mark): algunos archivos CSV guardados en Windows
                    // tienen un caracter invisible "\uFEFF" al principio.
                    // Lo eliminamos para no contaminar los datos.
                    linea = linea.replace("\uFEFF", "");

                    primeraLinea = false; // Ya procesamos la primera linea

                    // "continue" salta al siguiente ciclo del while sin ejecutar
                    // el codigo de abajo. Asi ignoramos la linea de encabezados.
                    continue;
                }

                // split(";") divide la linea en un arreglo usando ";" como separador
                // Ejemplo: "Campana;Concierto;Musica;Laura;1111" -> ["Campana","Concierto","Musica","Laura","1111"]
                String[] campos = linea.split(";");

                // Solo procesamos la linea si tiene exactamente 5 columnas
                // (proteccion contra filas vacias o mal formadas en el CSV)
                if (campos.length == 5) {

                    // Creamos un nuevo objeto Evento con los 5 campos
                    // campos[0] = Localidad, campos[1] = Tipo, campos[2] = Organizador, etc.
                    Evento e = new Evento(
                        campos[0],  // Localidad
                        campos[1],  // Tipo de evento
                        campos[2],  // Organizador
                        campos[3],  // Nombre del contacto
                        campos[4]   // Telefono
                    );

                    // Agregamos el evento a la lista
                    eventos.add(e);
                }
            }

        // ----------------------------------------------------------
        // MANEJO DE EXCEPCIONES (errores posibles)
        // "catch" captura el error y muestra un mensaje al usuario
        // en vez de que el programa se cierre abruptamente.
        // ----------------------------------------------------------
        } catch (FileNotFoundException e) {
            // Se lanza cuando el archivo no existe en la ruta indicada
            System.out.println("\n[ERROR] No se encontro el archivo: " + rutaArchivo);
            System.out.println("  Asegurate de que eventos.csv este en la carpeta raiz del proyecto.\n");

        } catch (IOException e) {
            // Se lanza por cualquier otro error de lectura (disco lleno, permisos, etc.)
            System.out.println("\n[ERROR] Problema al leer el archivo: " + e.getMessage());
        }

        // Devolvemos la lista (puede estar vacia si hubo un error)
        return eventos;
    }


    // ==========================================================
    // METODO: buscarPorLocalidad
    //
    // Recorre toda la lista de eventos y selecciona solo los que
    // coinciden con la localidad ingresada por el usuario.
    //
    // Parametros:
    //   eventos   - la lista completa de todos los eventos
    //   localidad - el texto que ingreso el usuario
    //
    // Retorno:
    //   List<Evento> - lista filtrada con los eventos que coinciden
    //
    // Nota sobre equalsIgnoreCase():
    //   Compara textos SIN distinguir mayusculas de minusculas.
    //   "campana" == "Campana" == "CAMPANA" -> todos son iguales.
    //   Esto hace la busqueda mas comoda para el usuario.
    // ==========================================================
    static List<Evento> buscarPorLocalidad(List<Evento> eventos, String localidad) {

        List<Evento> resultados = new ArrayList<>(); // Lista para guardar los eventos que coincidan

        // Recorremos cada evento de la lista con un "for-each"
        // La variable "e" toma el valor de cada Evento en cada vuelta
        for (Evento e : eventos) {

            // Comparamos la localidad del evento con lo que busca el usuario
            if (e.localidad.equalsIgnoreCase(localidad)) {
                resultados.add(e); // Si coincide, lo agregamos a los resultados
            }
        }

        return resultados; // Devolvemos la lista filtrada (puede estar vacia)
    }


    // ==========================================================
    // METODO: buscarPorTipo
    //
    // Igual que buscarPorLocalidad pero filtra por Tipo de Evento.
    //
    // Parametros:
    //   eventos - la lista completa de todos los eventos
    //   tipo    - el tipo de evento que ingreso el usuario
    //             Ej: "Concierto", "Taller", "Feria"
    //
    // Retorno:
    //   List<Evento> - lista filtrada con los eventos que coinciden
    // ==========================================================
    static List<Evento> buscarPorTipo(List<Evento> eventos, String tipo) {

        List<Evento> resultados = new ArrayList<>();

        for (Evento e : eventos) {
            // equalsIgnoreCase: "concierto" == "Concierto" == "CONCIERTO"
            if (e.tipoEvento.equalsIgnoreCase(tipo)) {
                resultados.add(e);
            }
        }

        return resultados;
    }


    // ==========================================================
    // METODO: mostrarResultados
    //
    // Imprime en la consola los resultados de una busqueda
    // con un formato visual claro y ordenado.
    //
    // Parametros:
    //   resultados - lista de eventos encontrados
    //   criterio   - nombre del filtro usado (ej: "Localidad")
    //   valor      - valor buscado (ej: "Campana")
    // ==========================================================
    static void mostrarResultados(List<Evento> resultados, String criterio, String valor) {

        // Imprimimos el encabezado de la seccion de resultados
        System.out.println("\n==========================================");
        System.out.println(" Resultados para " + criterio + ": \"" + valor + "\"");
        System.out.println("==========================================");

        if (resultados.isEmpty()) {
            // isEmpty() devuelve true si la lista no tiene ningun elemento
            System.out.println("  No se encontraron eventos con ese criterio.");
        } else {
            // .size() devuelve la cantidad de elementos de la lista
            System.out.println("  Se encontraron " + resultados.size() + " evento(s):\n");

            // Recorremos e imprimimos cada evento
            // Al llamar println(e), Java llama automaticamente e.toString()
            for (Evento e : resultados) {
                System.out.println(e);
            }
        }

        System.out.println("==========================================\n");
    }


    // ==========================================================
    // METODO: guardarResultados
    //
    // Crea un nuevo archivo CSV con los resultados de la ultima
    // busqueda realizada. El archivo se guarda en la carpeta
    // raiz del proyecto (misma ubicacion que eventos.csv).
    //
    // Parametros:
    //   resultados    - lista de eventos a guardar
    //   nombreArchivo - nombre del archivo de salida (con extension .csv)
    // ==========================================================
    static void guardarResultados(List<Evento> resultados, String nombreArchivo) {

        // Si la lista esta vacia no tiene sentido crear el archivo
        if (resultados.isEmpty()) {
            System.out.println("  No hay resultados para guardar.");
            return; // "return" sin valor sale del metodo inmediatamente
        }

        // try-with-resources para escritura de archivo
        // PrintWriter      = permite escribir lineas de texto en un archivo
        // OutputStreamWriter = convierte texto a bytes con la codificacion indicada
        // FileOutputStream   = crea/abre el archivo en el disco para escritura
        // StandardCharsets.UTF_8 = guardamos con UTF-8 para soportar tildes
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(
                    new FileOutputStream(nombreArchivo),
                    StandardCharsets.UTF_8))) {

            // Escribimos la linea de encabezado (cabecera del CSV)
            pw.println("Localidad;Tipo de evento;Organizador;Nombre del contacto;Telefono");

            // Escribimos cada evento como una linea CSV usando el metodo toCsv()
            for (Evento e : resultados) {
                pw.println(e.toCsv());
            }

            System.out.println("\n  Resultados guardados correctamente en: " + nombreArchivo);

        } catch (IOException e) {
            // Error al escribir: disco lleno, sin permisos, nombre invalido, etc.
            System.out.println("\n  [ERROR] No se pudo guardar el archivo: " + e.getMessage());
        }
    }


    // ==========================================================
    // METODO: mostrarMenu
    //
    // Dibuja el menu de opciones en la consola.
    // Se llama al inicio de cada vuelta del bucle principal.
    //
    // Los caracteres ╔ ╗ ║ ╚ ╝ ═ ╣ son caracteres Unicode
    // especiales que forman un recuadro visual en la consola.
    // ==========================================================
    static void mostrarMenu() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     BUSCADOR DE EVENTOS - CILSA      ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Buscar por Localidad             ║");
        System.out.println("║  2. Buscar por Tipo de Evento        ║");
        System.out.println("║  3. Guardar ultimo resultado en CSV  ║");
        System.out.println("║  4. Salir                            ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("  Ingrese una opcion: "); // print sin "ln" para que el cursor quede en la misma linea
    }


    // ==========================================================
    // METODO PRINCIPAL: main
    //
    // Es el punto de entrada del programa. Java siempre busca
    // este metodo exacto para saber por donde empezar.
    //
    // La firma "public static void main(String[] args)" es fija:
    //   public  = accesible desde cualquier lugar
    //   static  = no necesita crear un objeto para llamarlo
    //   void    = no devuelve ningun valor
    //   String[] args = parametros que se pueden pasar desde la
    //                   linea de comandos (no los usamos aqui)
    // ==========================================================
    public static void main(String[] args) {

        // Scanner es el objeto que nos permite leer lo que escribe
        // el usuario en la consola. System.in es la entrada estandar (teclado).
        Scanner scanner = new Scanner(System.in);

        // Nombre del archivo de datos.
        // Al no incluir ruta completa, Java lo busca en la carpeta
        // raiz del proyecto (donde esta el pom.xml o nbproject/).
        String rutaArchivo = "eventos.csv";

        // Cargamos todos los eventos al iniciar el programa.
        // Solo leemos el archivo UNA vez, y trabajamos con la lista en memoria.
        System.out.println("\nCargando datos desde " + rutaArchivo + "...");
        List<Evento> todosLosEventos = leerCSV(rutaArchivo);

        // Si la lista quedo vacia (error al leer), no tiene sentido continuar
        if (todosLosEventos.isEmpty()) {
            System.out.println("No se pudieron cargar los eventos. Finalizando programa.");
            return; // Termina el metodo main y por ende el programa
        }

        System.out.println("  " + todosLosEventos.size() + " eventos cargados correctamente.\n");

        // Esta lista guarda los resultados de la ULTIMA busqueda realizada.
        // Se actualiza cada vez que el usuario hace una nueva busqueda.
        // La opcion 3 (guardar) usa esta lista.
        List<Evento> ultimoResultado = new ArrayList<>();

        // Variable booleana que controla el bucle principal.
        // Mientras sea "true" el menu se sigue mostrando.
        // Cuando el usuario elige "Salir" se pone en "false".
        boolean continuar = true;

        // --------------------------------------------------------
        // BUCLE PRINCIPAL DEL PROGRAMA
        // Se repite hasta que el usuario elija la opcion 4 (Salir).
        // Cada vuelta muestra el menu y procesa la opcion elegida.
        // --------------------------------------------------------
        while (continuar) {

            mostrarMenu(); // Mostramos el menu antes de cada decision

            // nextLine() lee todo lo que escribio el usuario hasta que presiona Enter
            // .trim() elimina espacios accidentales al inicio o al final
            String opcionStr = scanner.nextLine().trim();

            // switch evalua el valor de "opcionStr" y ejecuta el "case" que coincida
            switch (opcionStr) {

                // ------------------------------------------------
                // OPCION 1: Buscar por Localidad
                // Pide una localidad al usuario y muestra los
                // eventos que pertenecen a esa localidad.
                // ------------------------------------------------
                case "1":
                    System.out.print("\n  Ingrese la localidad a buscar: ");
                    String localidad = scanner.nextLine().trim(); // Leemos la localidad

                    // Llamamos al metodo de busqueda y guardamos los resultados
                    ultimoResultado = buscarPorLocalidad(todosLosEventos, localidad);

                    // Mostramos los resultados en pantalla
                    mostrarResultados(ultimoResultado, "Localidad", localidad);

                    break; // "break" evita que se ejecute el siguiente case

                // ------------------------------------------------
                // OPCION 2: Buscar por Tipo de Evento
                // Pide un tipo de evento y muestra los que coinciden.
                // ------------------------------------------------
                case "2":
                    // Mostramos los tipos disponibles como ayuda al usuario
                    System.out.println("\n  Tipos disponibles: Concierto, Taller, Conferencia,");
                    System.out.println("                     Exposicion, Feria, Festival");
                    System.out.print("  Ingrese el tipo de evento a buscar: ");
                    String tipo = scanner.nextLine().trim(); // Leemos el tipo

                    ultimoResultado = buscarPorTipo(todosLosEventos, tipo);
                    mostrarResultados(ultimoResultado, "Tipo de Evento", tipo);

                    break;

                // ------------------------------------------------
                // OPCION 3: Guardar ultimo resultado en CSV
                // Guarda en un archivo CSV los resultados de la
                // ultima busqueda realizada (opcion 1 o 2).
                // ------------------------------------------------
                case "3":
                    if (ultimoResultado.isEmpty()) {
                        // Si aun no hizo ninguna busqueda, no hay nada que guardar
                        System.out.println("\n  Primero realiza una busqueda (opcion 1 o 2).\n");
                    } else {
                        System.out.print("\n  Nombre del archivo a guardar (sin extension): ");
                        String nombre = scanner.nextLine().trim();

                        // Si el usuario no escribe nada, usamos "resultados" como nombre
                        if (nombre.isEmpty()) {
                            nombre = "resultados";
                        }

                        // Llamamos al metodo que escribe el archivo, agregando la extension
                        guardarResultados(ultimoResultado, nombre + ".csv");
                        System.out.println(); // Linea en blanco para separacion visual
                    }
                    break;

                // ------------------------------------------------
                // OPCION 4: Salir del programa
                // Cambia la variable de control a false para que
                // el while no vuelva a ejecutarse.
                // ------------------------------------------------
                case "4":
                    System.out.println("\n  Gracias por usar el Buscador de Eventos. ¡Hasta luego!\n");
                    continuar = false; // Esto termina el bucle while en la proxima evaluacion
                    break;

                // ------------------------------------------------
                // DEFAULT: Opcion no valida
                // Se ejecuta cuando el usuario escribe algo que
                // no es "1", "2", "3" ni "4".
                // ------------------------------------------------
                default:
                    System.out.println("\n  Opcion no valida. Ingrese 1, 2, 3 o 4.\n");
                    break;
            }

        } // fin del while

        // Cerramos el Scanner para liberar los recursos del sistema.
        // Es buena practica cerrar siempre los recursos que se abrieron.
        scanner.close();

    } // fin del metodo main

} // fin de la clase BuscadorEventos