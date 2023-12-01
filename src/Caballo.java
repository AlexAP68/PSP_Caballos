import java.time.LocalTime;
import java.util.Random;

public class Caballo extends Thread
{
    private int velocidad;
    private double distanciaRecorrida;
    private int posicion;
    private LocalTime timeStart;
    private int timeRaceDone;
    public final Object lock = new Object();
    private Carrera carrera;
    private int estado;

    //Constructor de caballo
    public Caballo(int velocidad, double distance, int posicion, LocalTime timeStart, int timeRaceDone, Carrera carrera, int estado) {
        this.velocidad = velocidad;
        this.distanciaRecorrida = distance;
        this.posicion = posicion;
        this.timeStart = timeStart;
        this.timeRaceDone = timeRaceDone;
        this.carrera = carrera;
        this.estado = estado;

    }

    //Metodo run
    @Override
    public void run() {
        timeStart = LocalTime.now();
        while (!carrera.isAcabada() && estado != 3) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.getMessage();
                return;
            }

            if (!carrera.isPausado()) {
                //Calcular la distancia recorrida
                calculateDistanceCovered();
                if (distanciaRecorrida >= carrera.distanciacarrera) {
                    carrera.cruzarMeta(this);
                    break;
                }
                //Inicia variante para ver si va ha variar el estado del caballo
                variante();
            }
        }
    }


    //Metodo estado para cambiar el grafico de la carrera del caballo
    public void estado() {
        //Calcula lo que ha recorrido
        int liniaRecorrida = (int) ((getMetersCovered() * 100 / carrera.getMetroscarrera()) );

        //Si ha finalizado la carrera pinta lo siguiente
        if (distanciaRecorrida >= carrera.distanciacarrera) {
            System.out.println( "-".repeat(102) + "🐎" +     "|" + getName() + "|Estado: Finalizado " + "Tiempo:" + getTimeRaceDone() + "segundos");
            System.out.println("  ");
            //Si el caballo no esta muerto o ha acabado la carrera
        } else if (estado != 3) {
            caballocorre(liniaRecorrida);
            //Si el estado es 3 el caballo esta muerto
        }if (estado == 3){
            System.out.printf("%s ---> Caballo destruido por la AVT.\n", getName());
        }
    }

    //Segun el estado que tenga el caballo lo dibujara de una forma u otra
    private void caballocorre(int liniaRecorrida) {

        //Estado normal
        if (estado == 0){
            System.out.println( "-".repeat(liniaRecorrida) + "🐎" +  "-".repeat(102 - liniaRecorrida) + "|" + getName() + " Velocidad:" + getVelocidad());
            System.out.println("  ");
            //Estado oveja
        } else if (estado == 1) {
            System.out.println( "-".repeat(liniaRecorrida) + "\uD83D\uDC11" +   "-".repeat(102 - liniaRecorrida) + "|" + getName() + " Velocidad:" + getVelocidad() + "|Estado: Convertido en oveja");
            System.out.println("  ");

            estado = 0;
            this.velocidad = 15;
            //Estado viaje temporal
        }else {
            System.out.println( "-".repeat(liniaRecorrida) + "🐎\uD83C\uDF00" +   "-".repeat(100 - liniaRecorrida) + "|" + getName() + " Velocidad:" + getVelocidad() + "|Estado: Ha saltado en el tiempo");
            System.out.println("  ");
            estado = 0;
        }

    }

    private static int contadorMuerto = 0; // Contador de muertes

    //Ver si el caballo va ha cambiar de estado
    private synchronized void variante() {
        Random rnd = new Random();
        int randomNum = rnd.nextInt(40) + 1;

        if (randomNum >= 10 && randomNum <= 15) {
            // Oveja
            this.estado = 1;
            this.velocidad = 0;
        } else if (randomNum == 39 && contadorMuerto < (carrera.getHorses().size()/2) ) {
            // ¡Hilo muere!
            Thread.currentThread().interrupt();
            incrementDeathCounter();
            setDead(3);

        } else if (randomNum >= 20 && randomNum <= 23) {
            // Teletransportacion de posicion y cambio de distancia
            double distanceChange = rnd.nextDouble() * carrera.distanciacarrera;

            // Decide si el caballo avanza o retrocede
            boolean moveForward = rnd.nextBoolean();

            if (!moveForward) {
                // Retroceder, pero asegurarse de que no vaya más alla del punto de partida
                double newDistance = Math.max(0, getDistanciaRecorrida() - distanceChange);
                setDistanciaRecorrida(newDistance);
            } else {
                // Avanzar
                setDistanciaRecorrida(getDistanciaRecorrida() + distanceChange);
            }
            estado =2;
        }

        else {
            // Mantener velocidad normal
            int speedVariation = rnd.nextInt(56) + 15;
            setVelocidad(speedVariation);
            estado = 0;
        }
    }

    private synchronized void incrementDeathCounter() {
            contadorMuerto++;

    }

    private void calculateDistanceCovered() {
        setDistanciaRecorrida(getDistanciaRecorrida() +  ((double) velocidad / 3600));
    }

    private int getMetersCovered() {
        int metersCovered = (int) (getDistanciaRecorrida() * 1000);

        return metersCovered;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int newSpeed) {
        this.velocidad = newSpeed;
    }

    public double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(double distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public int getTimeRaceDone() {
        return timeRaceDone;
    }

    public void setTimeRaceDone(int timeRaceDone) {
        this.timeRaceDone = timeRaceDone;
    }



    public void setDead(int muerto) {
        this.estado = muerto;

        if (estado == 3) {
            // Si el caballo está muerto, quitarlo de la lista
            carrera.getHorses().remove(this);
            carrera.getdeadHorses().add(this);
        }
    }
}
