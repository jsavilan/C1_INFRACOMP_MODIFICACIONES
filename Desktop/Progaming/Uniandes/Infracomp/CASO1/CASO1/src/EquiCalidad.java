class EquiCalidad extends Thread { 
    private final int cantidadMaxima;
    private final BuzonRev buzonReproceso;
    private final BuzonRev buzonRevision;
    private final Deposito deposito;
    private int productosAprobados = 0;
    private final int fallosMax;
    private int fallos = 0;
    private int id;

    public EquiCalidad(BuzonRev buzonRevision, BuzonRev buzonReproceso, Deposito deposito, int fallosMax, int cantidadMaxima, int id) {
        this.cantidadMaxima = cantidadMaxima;
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.deposito = deposito;
        this.fallosMax = fallosMax;
        this.id = id;
    }
    
    @Override
    public void run() {
        boolean finGenerado = false;
        boolean terminado = false;
        
        while (!terminado) {
            try {
                // Consulta semi-activa: se utiliza el método no bloqueante para obtener un producto.
                String prod = buzonRevision.quitarNoBloqueante();
                if(prod == null) {
                    // Si no hay producto y ya se generó FIN, se finaliza la ejecución
                    if(finGenerado && buzonRevision.vacio()) {
                        terminado = true;
                    } else {
                        // Espera breve antes de volver a consultar
                        Thread.sleep(100);
                    }
                    continue;
                }
                // Genera un número aleatorio en el rango [1, 100]
                int randy = (int)(Math.random() * 100) + 1;
                if("FIN".equals(prod)) {
                    // Si se recibe FIN, se reenvía para que otros hilos lo detecten y se marca la señal.
                    buzonRevision.guardar("FIN");
                    finGenerado = true;
                } else {
                    if(randy % 7 == 0 && fallos < fallosMax) {
                        fallos++;
                        buzonReproceso.guardar(prod);
                        System.out.println("Calidad"+id+ " rechaza: " + prod);
                    } else {
                        productosAprobados++;
                        deposito.guardar(productosAprobados);
                        System.out.println("Calidad "+id+ " aprueba: " + prod);
                    }
                    // Cuando se alcanza o supera la meta, se envía el mensaje FIN.
                    if(productosAprobados >= cantidadMaxima && !finGenerado) {
                        buzonReproceso.guardar("FIN");
                        finGenerado = true;
                    }
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                terminado = true;
            }
        }
        System.out.println("Calidad "+ id+" termina su ejecución.");
    }
}    