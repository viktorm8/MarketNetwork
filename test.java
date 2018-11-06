import java.util.HashMap;

public class test{


    public static void main(String[] args) {

        HashMap<Integer,String[]> mappa = new HashMap<>();

        mappa.put(1,"Ciao Mondo!");
        mappa.put(1,"Ciao Mondo e 2!");

        String ritorno = mappa.get(1);

        System.out.println(ritorno[0]);
        
        System.out.println(ritorno[1]);
        
    }
}