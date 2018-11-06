import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/*<>Implements a Market Services, 
need a data sructure to manage market graph.
I choose a HashMap, the key is the arc number,
the value is the list of the member connected. */

public class MarketService{

    int idMarket;
    HashMap<Integer,LinkedList<Member>> listaConnessioni = new HashMap<>(); // Map idArco,Lista Nodi collegati all'arco
    HashMap<Integer,LinkedList<Member>> nodiConnessi = new HashMap<>(); //Map idMember,Lista Nodi connessi al nodo con id = idMember
    LinkedList<Member> listaNodi = new LinkedList<>(); //anche non connessi

    public MarketService(int id){
        this.idMarket = id;
    }

    boolean addBank(Bank inBank){
        Random r = new Random();
        if(Bank.numberOfBanks < Bank.maxBanks ){
            inBank = new Bank(r.nextInt());
            listaNodi.add(inBank);
            return true;
        }
        else{
            return false;
        }
    }

    boolean connect(Member inMemberA, Member inMemberB){
        //true if succeded, false otherwise
        LinkedList listaMembriConnessiA = nodiConnessi.get(inMemberA.idMember);
        Iterator iterA = listaMembriConnessiA.iterator();
        while(iterA.hasNext()){
            if (iterA.next().equals(inMemberB)){
                System.out.println("NODI GIA CONNESSI!\n");
                return false;
            } 
        }

        LinkedList listaMembriConnessiB = nodiConnessi.get(inMemberA.idMember);
        Iterator iterB = listaMembriConnessiB.iterator();
        while(iterB.hasNext()){
            if (iterB.next().equals(inMemberA)){
                System.out.println("NODI GIA CONNESSI!\n");
                return false;
            } 
        }

        Random r = new Random();
        int arcID = r.nextInt();
        Iterator ite = listaNodi.iterator();
        boolean foundA = false;
        boolean foundB = false;
        while(ite.hasNext()){
            if(ite.next().equals(inMemberA)){
                foundA = true;
                System.out.println("A già presente!");
            }
            if(iter.next().equals(inMemberB)){
                foundB = true;
                System.out.println("B già presente!");
            }
        }
        boolean instantiateA, instantiateB = false;
        if((foundA) && (!foundB)){
            //instanzia B
            instantiateB = true;
            inMemberB = new Member();
            listaNodi.add(inMemberB);
        }
        else if((!foundA) && (foundB)){
            //instanzia A
            instantiateA = true;
            inMemberA = new Member();

            listaNodi.add(inMemberA);
        }
        else {
            instantiateA = true;
            instantiateB = true;
            inMemberA = new Member();
            inMemberB = new Member();

            listaNodi.add(inMemberA);

            listaNodi.add(inMemberB);
        }
        
        if( ((foundA) && (foundB) ) || ((foundA) && (instantiateB)) || ((instantiateA) && (foundB))){
            System.out.println("Nodi già presenti! Li connetto...");
            try{
                Thread.sleep(1000);
            }catch(InterruptedException ie){}
            
            //aggiorno lista connessioni
            LinkedList temp = new LinkedList<>();
            temp.add(inMemberA);
            temp.add(inMemberB);
            listaConnessioni.put(arcID,temp);
            
            //retrival list of connected nodes to node and update
            Member m;
            LinkedList<Member> listaNodiConnessiAdA= nodiConnessi.get(inMemberA.getIDMember());
            LinkedList<Member> listaNodiConnessiAdB= nodiConnessi.get(inMemberB.getIDMember());
            listaNodiConnessiAdA.add(inMemberB);
            listaNodiConnessiAdB.add(inMemberA);
            nodiConnessi.put(inMemberA.getIDMember(), listaMembriConnessiA);
            nodiConnessi.put(inMemberB.getIDMember(), listaMembriConnessiB);
            
        }

    }

    public void visit(Bank inBank, Collection<Member> outMembers){
        
        LinkedList<Member> nodiConnessiABank = nodiConnessi.get(inBank.idMember);
        Iterator ite = nodiConnessiABank.iterator();
        outMembers = new LinkedList<>();
        while(ite.hasNext()){
            Member MemberToAdd = ite.next();
            outMembers.add(MemberToAdd);
            if(MemberToAdd instanceof Trader){
                LinkedList<Member> nodiConnessiATrader = nodiConnessi.get(MemberToAdd.idMember);
                //They are PrivateInvestors
                Iterator iter = nodiConnessiATrader.iterator();
                while(iter.hasNext()){
                    PrivateInvestor pI = iter.next();
                    outMembers.add((Member)pI);
                }

            }

        }
    }

            
        

   


}