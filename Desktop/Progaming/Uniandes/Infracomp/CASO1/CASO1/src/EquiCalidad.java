public class EquiCalidad extends Thread {
    private final int cantidadMaxima;
    private final BuzonRev buzonReproceso;
    private final BuzonRev buzonRevision;
    private final Deposito deposito;
    private int productosAprobados = 0;
    private final int fallosMax;
    private int fallos = 0;
    // (int) (Math.random() * 101)

    public EquiCalidad(BuzonRev buzonRevision, BuzonRev buzonReproceso, Deposito deposito, int fallosMax,
            int cantidadMaxima) {
        this.cantidadMaxima = cantidadMaxima;
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.deposito = deposito;
        this.fallosMax = fallosMax;

    }

    @Override
    public void run() {
        try {

            while (true) {
                String prod = buzonRevision.quitar();
                int randy = (int) (Math.random() * 101);
                if ("FIN".equals(prod)) {
                    buzonRevision.guardar("FIN");
                    break;
                }
                if (randy % 7 == 0 && fallos < fallosMax) {
                    // yield
                    fallos++;
                    buzonReproceso.guardar(prod);
                    System.out.println("Rechazo:" + prod);

                } else {
                    productosAprobados++;
                    deposito.guardar(productosAprobados);
                    System.out.println("Calidad aprobo: " + prod);
                }
                if (productosAprobados >= cantidadMaxima) {
                    buzonReproceso.guardar("Fin");
                    break;
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

}
