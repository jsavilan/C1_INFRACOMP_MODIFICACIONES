import java.util.LinkedList;
import java.util.Queue;

class BuzonRev {
    private final Queue<String> buzones;
    private final int capacidad;

    public BuzonRev(int capacidad) {
        this.capacidad = capacidad;
        this.buzones = new LinkedList<>();
    }
    
    public synchronized void guardar(String mensaje) throws InterruptedException {
        while(buzones.size() >= capacidad) {
            wait();
        }
        buzones.add(mensaje);
        notifyAll();
    }
    
    public synchronized String quitar() throws InterruptedException {
        while(buzones.isEmpty()) {
            wait();
        }
        String mensaje = buzones.poll();
        notifyAll();
        return mensaje;
    }
    
    // Método de extracción no bloqueante para polling semi-activo.
    public synchronized String quitarNoBloqueante() {
        if(buzones.isEmpty()){
            return null;
        }
        String mensaje = buzones.poll();
        notifyAll();
        return mensaje;
    }
    
    public synchronized boolean vacio() {
        return buzones.isEmpty();
    }

}