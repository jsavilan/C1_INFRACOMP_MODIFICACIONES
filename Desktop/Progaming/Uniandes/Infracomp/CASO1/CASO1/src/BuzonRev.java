public class BuzonRev {
    private final String[] buzones;
    private int cont = 0;

    public BuzonRev(int capacidad) {
        this.buzones = new String[capacidad];
    }

    public synchronized void guardar(String buzon) throws InterruptedException {

        while (cont >= buzones.length) {
            wait();
        }
        buzones[cont++] = buzon;
        notifyAll();

    }

    public synchronized String quitar() throws InterruptedException {
        while (cont == 0) {
            wait();
        }
        String buzon = buzones[--cont];
        notifyAll();
        return buzon;

    }

    public boolean vacio() {
        return cont == 0;
    }

}
