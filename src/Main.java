import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main
{
    private Scanner sc = new Scanner(System.in);
    private int numeroCaballos = 0;
    private double distancia = 0;
    private ArrayList<Caballo> caballo = new ArrayList<>();
    private Carrera carrera;

    public static void main(String[] args)
    {
        Main main = new Main();
        main.startPrograma();
    }

    //Iniciamos el programa
    private void startPrograma() {
        System.out.println("L     OOO  K   K  I    H   H  OOO  RRRR   SSS   EEEE   RRRR      AA    CCCC  EEEE");
        System.out.println("L    O   O K  K   I    H   H O   O R   R S      E      R   R    A  A  C      E   ");
        System.out.println("L    O   O K K    I    HHHHH O   O RRRR   SSS   EEEE   RRRR     AAAA  C      EEEE ");
        System.out.println("L    O   O KK     I    H   H O   O R  R       S E      R   R    A  A  C      E    ");
        System.out.println("LLLL  OOO  K  K   I    H   H  OOO  R   RR SSSS  EEEE   R   RR  A    A  CCCC  EEEE ");
        System.out.println(" ");

        Scanner scanner = new Scanner(System.in);

        String eleccion;
        do {
            Menu();
            System.out.print("Ingrese su opcion: ");
            eleccion = scanner.next();

            switch (eleccion.toLowerCase()) {
                case "1":
                    iniciarCarrerasolitaria();
                    break;
                case "2":
                    informacion();
                    break;
                case "3":
                    System.out.println("¡Hasta luego!");
                    System.out.println("Todo el tiempo. Siempre.");
                    break;
                default:
                    System.out.println("Opcion no valida. Intente de nuevo.");
            }

        } while (!eleccion.equalsIgnoreCase("3"));

        scanner.close();

    }

    //Las opciones del Menu
    private void Menu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Carrera");
        System.out.println("2. Informacion");
        System.out.println("3. Salir");
    }

//Preguntamos los datos para crear la carrera
    private void iniciarCarrerasolitaria(){
        boolean eleccion = true;
        while (eleccion) {
            System.out.println("Introduce el numero de caballos que participaran en la carrera (entre 10 y 20): ");

            try {
                numeroCaballos = sc.nextInt();

                if (numeroCaballos < 10 || numeroCaballos > 20) {
                    System.out.println("El numero de caballos debe estar entre 10 y 20. Intente de nuevo.");
                    continue;
                }

                sc.nextLine();  // Consumir el carácter de nueva línea
                eleccion = false;
            } catch (InputMismatchException e) {
                e.getMessage();
                System.out.println("Se necesita un numero entero.");
                sc.next();
            }
        }
        eleccion = true;
        while (eleccion) {
            System.out.println("Ahora introduce la distancia de la carrera (entre 0,4Km y 4 Km):");

            try {
                distancia = sc.nextDouble();

                if (distancia < 0.4 || distancia > 4) {
                    System.out.println("La distancia de la carrera debe estar entre 0,4Km y 4 Km. Intente de nuevo.");
                    continue;
                }

                sc.nextLine();  // Consumir el carácter de nueva línea
                eleccion = false;
            } catch (InputMismatchException e) {
                e.getMessage();
                System.out.println("Se requiere un numero. (Utiliza como separador la coma)");
                sc.next();
            }
        }

        carrera = new Carrera(distancia, caballo);
//Creamos los caballos para usar en la carrera
            crearCaballos();

//Iniciamos la carrera
        carrera.startCarrera();

        //Nos quedamos esperando hasta que acabe la carrera
        while (!carrera.isMostrarpodio()) {
        }


        nuevaCarrera();
    }

    //Pregunta si quieres iniciar una nueva carrera
    private void nuevaCarrera() {
        System.out.println("¿Deseas realizar otra carrera? (si | no)");

        boolean respuestaValida = false;

        do {
            String respuesta = sc.nextLine().toLowerCase();

            if (respuesta.equals("si")) {
                caballo.clear();
                iniciarCarrerasolitaria();
                respuestaValida = true;
            } else if (respuesta.equals("no")) {
                sc.close();
                System.out.println("Programa finalizado.");
                System.out.println("Todo el tiempo. Siempre.");
                System.exit(0);
            } else {
                System.out.println("Respuesta no valida. Introduce 'si' o 'no'.");
            }
        } while (!respuestaValida);
    }

    //Creamos los caballos necesarios para la carrera
    private ArrayList<Caballo> crearCaballos() {
        //Guardamos en una lista los nombres del archivo txt
        List<String> nombres = leerNombresDesdeArchivo();

        for (int i = 0; i < numeroCaballos; i++) {
            String nombre = nombres.get(i % nombres.size());  // Ciclar por la lista de nombres
            Caballo caballo = new Caballo(50, 0, 0, null, 0, carrera, 0);
            caballo.setName("Caballo " + nombre);
            this.caballo.add(caballo);
        }

        return caballo;
    }

    //Leemos el archivo con los nombres
    private List<String> leerNombresDesdeArchivo() {
        List<String> nombres = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("nombres.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                nombres.add(linea.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nombres;
    }
    //Informacion de la carrera
    private void  informacion(){
        System.out.println("Los caballos tendran que llegar desde el principio del tiempo hacia el final del tiempo para encontrar al que permanece");
        System.out.println("El unico inconveniente es que la AVT no se lo pondra tan facil");
        System.out.println("\uD83D\uDC11: La AVT podra convertir a los caballos  en una variante en forma de oveja");
        System.out.println("🐎\uD83C\uDF00:Los caballos de vez en cuando podran saltar en el tiempo, lo malo, tambien podran volver hacia atras");
        System.out.println("Si la AVT pilla a un caballo, lo destruira");
        System.out.println("Nota: Cuidado con la Ramificacion temporal. Si eso pasa se tendra que reiniciar la sagrada linia temporal. Buena suerte ");
    }
}