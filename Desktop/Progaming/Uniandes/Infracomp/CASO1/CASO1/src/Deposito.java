public class Deposito {
    private int[] fini;
    private int cont = 0;

    public Deposito(int capa) {
        this.fini = new int[capa];
    }

    public synchronized void guardar(int produ) {
        System.out.println("lol" + produ);
        if (cont < fini.length) {
            for (int i = 0; i < cont; i++) {
                System.out.println("el dia" + fini[i]);
            }
            fini[cont++] = produ;
            System.out.println("guardado: " + produ);
        }
    }

    public synchronized int[] lstFini() {
        int[] prodActu = new int[cont];
        for (int i = 0; i < cont; i++) {
            prodActu[i] = fini[i];
        }
        return prodActu;
    }
}
