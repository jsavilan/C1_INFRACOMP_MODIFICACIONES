import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando programa");
        Scanner conso = new Scanner(System.in);

        System.out.println("Número de productores y calificadores: ");
        int nGente = conso.nextInt();

        System.out.println("Número de productos: ");
        int nProductos = conso.nextInt();

        System.out.println("Límite del buzón de revisión: ");
        int limBuzon = conso.nextInt();

        conso.close();

        BuzonRev buzonRevision = new BuzonRev(limBuzon);
        // Se asume que el buzón de reproceso tiene capacidad suficiente (usamos
        // nProductos)
        BuzonRev buzonReproceso = new BuzonRev(nProductos);
        Deposito deposito = new Deposito(nProductos);
        int fallosMax = (int) Math.floor(0.1 * nProductos);

        Productor[] productores = new Productor[nGente];
        EquiCalidad[] verificadores = new EquiCalidad[nGente];

        for (int i = 0; i < nGente; i++) {
            verificadores[i] = new EquiCalidad(buzonRevision, buzonReproceso, deposito, fallosMax, nProductos, i);
            productores[i] = new Productor(buzonRevision, buzonReproceso, nProductos, i);
        }

        for (int i = 0; i < nGente; i++) {
            verificadores[i].start();
        }

        for (int i = 0; i < nGente; i++) {
            productores[i].start();
        }

        for (int i = 0; i < nGente; i++) {
            try {
                productores[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Se notifica a los operarios de calidad en caso de finalización
        try {
            buzonRevision.guardar("FIN");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (int i = 0; i < nGente; i++) {
            try {
                verificadores[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Productos en depósito:");
        int[] prodFini = deposito.lstFini();
        for (int i = 0; i < prodFini.length; i++) {
            System.out.println(prodFini[i]);
        }

        System.out.println("Programa finalizado.");
    }
}