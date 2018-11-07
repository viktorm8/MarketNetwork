import java.util.LinkedList;

class MarketEseguibile{

    public static void main(String[] args) {

        Bank b1 = new Bank(1029384);
        MarketService ms = new MarketService(1);
        ms.addBank(b1);
        System.out.print("Banca aggiunta\n");
        ms.listaNodi.forEach(m -> System.out.println(m+ "\n"));
        Trader t1 = new Trader("Giovanni");
        ms.connect(b1, t1);
        //ms.disconnect(b1, t1);

        PrivateInvestor pI1 = new PrivateInvestor("Maomao");
        ms.connect(b1, t1);
        ms.connect(t1, pI1);
        LinkedList<Member> listaOut = new LinkedList<>();
    }
    
}