public class Deposito {
    private final int[] productos;
    private int cont = 0;

    public Deposito(int capacidad) {
        this.productos = new int[capacidad];
    }
    
    public synchronized void guardar(int produ) {
        if(cont < productos.length) {
            productos[cont++] = produ;
            System.out.println("Deposito guardado: " + produ);
        }
    }
    
    public synchronized int[] lstFini() {
        int[] prodActu = new int[cont];
        for (int i = 0; i < cont; i++) {
            prodActu[i] = productos[i];
        }
        return prodActu;
    }
    
}