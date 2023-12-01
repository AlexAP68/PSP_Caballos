import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Carrera
{
    public double distanciacarrera = 0;
    private ArrayList<Caballo> caballo;
    private ArrayList<Caballo> caballoderrotado = new ArrayList<>();
    private ArrayList<Caballo> podio = new ArrayList<>();
    private int contador = 0;
    private volatile boolean acabada = false, pausado = false, mostrarpodio = false;
    private int metroscarrera = 0;
    public final Object lockRace = new Object();

    //Carrera constructor
    public Carrera(double distance, ArrayList<Caballo> caballo)
    {
        this.distanciacarrera = distance;
        this.caballo = caballo;
    }

    //Los caballos creados en Main se iniciaran
    public void startCarrera() {
        for (Caballo caballo : caballo) {
            caballo.start();
        }
//Muestra la carrera
        showCarrera();
    }

    //Muestra la carrera
    private void showCarrera() {
        LocalTime startTime = LocalTime.now();

        //Si la carrera no ha acabado sigue
        while (!isAcabada()) {
            //Si la carrera no se ha pausado sigue
            if (!isPausado()) {
                LocalTime actualTime = LocalTime.now();
                Duration elapsedTime = Duration.between(startTime, actualTime);

                System.out.println();
                System.out.println("*************************************************************");
                System.out.printf("Tiempo de carrera: %02d:%02d:%02d%n",
                        elapsedTime.toHoursPart(), elapsedTime.toMinutesPart(), elapsedTime.toSecondsPart());

                // Mostrar caballos vivos
                for (Caballo caballo : caballo) {
                    caballo.estado();
                }

                if (caballoderrotado != null) {
                    System.out.println("*************************************************************");
                    // Mostrar caballos muertos
                    for (Caballo caballo : caballoderrotado) {
                        caballo.estado();
                    }
                }
                //retardo
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                //que solo se ejecute una
                synchronized (lockRace) {
                    try {
                        //espera
                        lockRace.wait();
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
                }
            }
        }
    }

    //Metodo por cruzar linea
    public synchronized void cruzarMeta(Caballo caballo) {
            LocalTime endTime = LocalTime.now();
            Duration elapsedTime = Duration.between(caballo.getTimeStart(), endTime);
            caballo.setTimeRaceDone((int) elapsedTime.getSeconds());
            //Si el contador es más pequeño añade el caballo al podio
            if (contador < 3) {
                switch (contador) {
                    case 0 -> caballo.setPosicion(1);
                    case 1 -> caballo.setPosicion(2);
                    case 2 -> caballo.setPosicion(3);
                }
                podio.add(caballo);
            }
            contador++;
        //si hay tres que han cruzado la linea pregunta si quieres parar o continuar
        if (contador == 3) {
            setPausado(true);
            System.out.println("**********************************************************");
            System.out.println("Carrera pausada");
            System.out.println("¿Quieres continuar con la carrera? (si | no)");

            boolean respuestaValida = false;
            Scanner sc = new Scanner(System.in);

            do {
                String answerUser = sc.nextLine();
                if (answerUser.equalsIgnoreCase("si")) {
                    setPausado(false);
                    notificar();
                    respuestaValida = true;
                } else if (answerUser.equalsIgnoreCase("no")) {
                    setAcabada(true);
                    notificar();
                    System.out.println();
                    System.out.println("¡Se ha acabado el tiempo!");
                    showPodio();
                    respuestaValida = true;
                } else {
                    System.out.println("Respuesta no válida. Introduce 'si' o 'no'.");
                }
            } while (!respuestaValida);
        }


        if (contador == this.caballo.size() - 1) {
                setAcabada(true);
                notificar();
                System.out.println();
                System.out.println("¡Se ha acabado el tiempo!");
                System.out.println("Se han definido las posiciones");
                showPodio();
            }
        }


    private void notificar() {
        for (Caballo h : caballo) {
            //despierta a los caballos
            synchronized (h.lock) {
                h.lock.notifyAll();
            }
        }
        synchronized (lockRace) {
            lockRace.notifyAll();
        }
    }


    //Printea el podio
    private void showPodio() {
        System.out.println("********************");
        for (Caballo caballo : podio) {
            if (caballo.getPosicion() == 1) {
                System.out.printf("%s es el nuevo/a Guardian del Tiempo.\n", caballo.getName());
                System.out.printf("Posición: %d, Tiempo: %d segundos.\n",  caballo.getPosicion(), caballo.getTimeRaceDone());
            }
            if (caballo.getPosicion() == 2) {
                System.out.printf("%s es la nueva Señorita Minutos.\n", caballo.getName());
                System.out.printf("Posición: %d, Tiempo: %d segundos.\n",  caballo.getPosicion(), caballo.getTimeRaceDone());
            }
            if (caballo.getPosicion() == 3) {
                System.out.printf("%s es el nuevo jefe/a de la AVT.\n", caballo.getName());
                System.out.printf("Posición: %d, Tiempo: %d segundos.\n",  caballo.getPosicion(), caballo.getTimeRaceDone());
            }
        }

        System.out.println("********************");
        System.out.println("Los demas caballos han sido eliminados por la AVT");
        setMostrarpodio(true);
    }

    /* GETTERS AND SETTERS*/

    public ArrayList<Caballo> getHorses() {
        return caballo;
    }

    public  ArrayList<Caballo> getdeadHorses() {
        return caballoderrotado;
    }

    public void setHorses(ArrayList<Caballo> hors) {
        this.caballo = hors;
    }

    public void setdeadHorses(ArrayList<Caballo> deadhorse) {
        this.caballoderrotado = deadhorse;
    }

    public double getDistanciacarrera() {
        return distanciacarrera;
    }

    public void setDistanciacarrera(double distanciacarrera) {
        this.distanciacarrera = distanciacarrera;
    }

    public boolean isAcabada() {
        return acabada;
    }

    public void setAcabada(boolean acabada) {
        this.acabada = acabada;
    }

    public boolean isPausado() {
        return pausado;
    }

    public void setPausado(boolean pausado) {
        this.pausado = pausado;
    }

    public boolean isMostrarpodio() {
        return mostrarpodio;
    }

    public void setMostrarpodio(boolean mostrarpodio) {
        this.mostrarpodio = mostrarpodio;
    }

    public int getMetroscarrera() {
        this.metroscarrera = (int) (distanciacarrera * 1000);

        return metroscarrera;
    }
}
