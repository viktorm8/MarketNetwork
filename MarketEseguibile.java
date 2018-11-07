import java.util.LinkedList;

class MarketEseguibile{

    public static void main(String[] args) {

        Bank b1 = new Bank(1029384);
        MarketService ms = new MarketService(1);
        ms.addBank(b1);
        System.out.print("Banca aggiunta\n");
        Trader t1 = new Trader("Giovanni");
        boolean successful = ms.connect(b1, t1);
        if(successful){
            System.out.println("Sono riuscito a collegare i nodi!\n");
        }else{
            System.out.println("NON Sono riuscito a collegare i nodi!\n");
        }
        boolean successful2 = ms.disconnect(b1, t1);
        if(successful2){
            System.out.println("Sono riuscito a sconnettere i nodi!\n");
        }else{
            System.out.println("NON Sono riuscito a sconnettere i nodi\n");
        }
        
        PrivateInvestor pI1 = new PrivateInvestor("Maomao");
        ms.connect(b1, t1);
        ms.connect(t1, pI1);
        ms.listaNodi.forEach(o -> System.out.println(o + "\n") );

        LinkedList<Member> listaOut = new LinkedList<>();
        ms.visit(b1, listaOut);
        System.out.println("Lista Nodi visitati dalla banca:" + b1.toString());
        listaOut.forEach(o -> System.out.println(o + "\n") );
        
    }
    
}