class Productor extends Thread {
    private final BuzonRev buzonRevision;
    private final BuzonRev buzonReproceso;
    private static int cont = 0;
    private final int maxProdu;
    private int id;

    public Productor(BuzonRev buzonRev, BuzonRev buzonReproceso, int maxProdu,int id) {
        this.maxProdu = maxProdu;
        this.buzonRevision = buzonRev;
        this.buzonReproceso = buzonReproceso;
        this.id = id;
    }

    @Override
    public void run() {
        boolean terminado = false;
        while (!terminado) {
            try {
                String produ = null;
                if (!buzonReproceso.vacio()) {
                    produ = buzonReproceso.quitar();
                    if ("FIN".equals(produ)) {
                        terminado = true;
                    }
                } else {
                    synchronized (Productor.class) {
                        if (cont < maxProdu) {
                            cont++;
                            produ = "producto #" + cont;
                        } else {
                            terminado = true;
                        }
                    }
                }
                if (!terminado && produ != null) {
                    buzonRevision.guardar(produ);
                    System.out.println("Productor "+id + " genera: " + produ);
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                terminado = true;
            }
        }
        System.out.println("Productor "+ id + " termina su ejecución.");
    }

}