import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        Scanner conso = new Scanner(System.in);

        System.out.println("# prod y cali: ");
        int nGente = conso.nextInt();

        System.out.println("# productos: ");
        int nProductos = conso.nextInt();

        System.out.println("limite Buzon revision: ");
        int limBuzon = conso.nextInt();

        conso.close();

        BuzonRev buzonRevision = new BuzonRev(limBuzon);
        BuzonRev buzonReproceso = new BuzonRev(nProductos);
        Deposito deposito = new Deposito(nProductos);
        int fallosMax = (int) Math.floor(0.1 * nProductos);
        Productor[] productores = new Productor[nGente];
        EquiCalidad[] calidosos = new EquiCalidad[nGente];

        for (int i = 0; i < nGente; i++) {
            calidosos[i] = new EquiCalidad(buzonRevision, buzonReproceso, deposito, fallosMax, nProductos);
            productores[i] = new Productor(buzonRevision, buzonReproceso, nProductos);
        }

        for (int i = 0; i < nGente; i++) {
            calidosos[i].start();

        }

        for (int i = 0; i < nGente; i++) {
            productores[i].start();
            // calidosos[i].start();
        }

        for (int i = 0; i < nGente; i++) {
            try {
                productores[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            buzonRevision.guardar("FIN");
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }

        for (int i = 0; i < nGente; i++) {
            try {
                calidosos[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Prod en depo: ");
        int[] prodFini = deposito.lstFini();
        for (int i = 0; i < prodFini.length; i++) {
            System.out.println(prodFini[i]);
        }
    }
}