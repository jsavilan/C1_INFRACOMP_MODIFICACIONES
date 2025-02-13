public class Productor extends Thread {
    // private String produ;
    private final BuzonRev buzonRevision;
    private final BuzonRev buzonReproceso;
    private static int cont = 0;
    private final int maxProdu;
    // private boolean finali = false;
    // private String produ;

    public Productor(BuzonRev buzonRev, BuzonRev buzonReproceso, int maxProdu) {
        this.maxProdu = maxProdu;
        this.buzonRevision = buzonRev;
        this.buzonReproceso = buzonReproceso;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String produ = null;
                if (!buzonReproceso.vacio()) {
                    produ = buzonReproceso.quitar();
                    if ("FIN".equals(produ)) {
                        break;
                    }
                } else {
                    synchronized (Productor.class) {
                        if (cont < maxProdu) {
                            cont++;
                            produ = "producto #" + cont;
                        } else {
                            break;
                        }
                    }
                }

                /*
                 * String produ = buzonReproceso.quitar();
                 * if (produ.equals("FIN")) {
                 * finali = true;
                 * break;
                 * }
                 * try {
                 * buzonRevision.guardar(produ);
                 * } catch (InterruptedException e) {
                 * e.printStackTrace();
                 * }
                 * System.out.println("mbappe");
                 * 
                 * 
                 * if (cont > 0) {
                 * 
                 * produ = buzonReproceso.quitar();
                 * 
                 * } else {
                 * cont++;
                 * produ = "Producto #" + cont;
                 * }
                 * if (produ.equals("FIN")) {
                 * finali = true;
                 * break;
                 * }
                 */
                buzonRevision.guardar(produ);

                System.out.println("Hicieron: " + produ);

                Thread.sleep(500);

            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
