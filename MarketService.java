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
    LinkedList<Arc> listaArchi = new LinkedList<>(); 

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
        LinkedList<Member> listaMembriConnessiA; 
        if(nodiConnessi.get(inMemberA.getIDMember()) == null){
            listaMembriConnessiA = new LinkedList<>();
        }
        else{
            listaMembriConnessiA = nodiConnessi.get(inMemberA.getIDMember());
        }
        Iterator iterA = listaMembriConnessiA.iterator();
        while(iterA.hasNext()){
            if (iterA.next().equals(inMemberB)){
                System.out.println("NODI GIA CONNESSI!\n");
                return false;
            } 
        }

        LinkedList<Member> listaMembriConnessiB; 
        if(nodiConnessi.get(inMemberB.getIDMember()) == null){
            listaMembriConnessiB = new LinkedList<>();
        }
        else{
            listaMembriConnessiB = nodiConnessi.get(inMemberB.getIDMember());
        }
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
            Member m1 = (Member)ite.next();
            if(m1 == inMemberA){
                foundA = true;
                System.out.println("A già presente!");
            }
            if(m1.equals(inMemberB)){
                foundB = true;
                System.out.println("B già presente!");
            }
        }
        boolean instantiateA = false;
        boolean instantiateB = false;
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
            Arc a = new Arc(arcID);
            a.setNodes(inMemberA, inMemberB);
            listaArchi.add(a);
            
            //retrival list of connected nodes to node and update
            LinkedList<Member> listaNodiConnessiAdA= nodiConnessi.get(inMemberA.getIDMember());
            LinkedList<Member> listaNodiConnessiAdB= nodiConnessi.get(inMemberB.getIDMember());
            listaNodiConnessiAdA.add(inMemberB);
            listaNodiConnessiAdB.add(inMemberA);
            nodiConnessi.put(inMemberA.getIDMember(), listaMembriConnessiA);
            nodiConnessi.put(inMemberB.getIDMember(), listaMembriConnessiB);
            return true;
        }
        return false;

    }

    public boolean disconnect(Member inMemberA, Member inMemberB){
        Iterator i = listaArchi.iterator();
        int arcID = 0;
        Member nodeA = new Member();
        Member nodeB = new Member();
        while(i.hasNext()){
            Arc temp = (Arc)i.next();
            nodeA = temp.getNodeA();
            nodeB = temp.getNodeB();
            boolean found = false;
            if(((nodeA.equals(inMemberA)) && (nodeB.equals(inMemberB))) 
            || ((nodeB.equals(inMemberA)) && (nodeA.equals(inMemberB)))){
                found = true;
                arcID = temp.getArcID();
                i.remove();
                break;
            }

        }
        if (arcID == 0){
            System.out.println("Non sono connessi!");
            return false;
        }
        //rimuovo da listaConnessioni
        listaConnessioni.remove(arcID);

        //rimuova da nodiConnessi
        //Da NODO A a NODA B
        LinkedList<Member> tempA = nodiConnessi.get(nodeA.getIDMember());
        Iterator ite = tempA.iterator();
        while(ite.hasNext()){
            if(ite.next().equals(inMemberB)){
                ite.remove();
                break;
            }
        }
        if(tempA.isEmpty()){
            if( nodeA instanceof Bank){
                listaNodi.remove(nodeA); //rimuovo inMemberA dalla network
            }
            
        }

        //Da NODO B a NODA A
        LinkedList<Member> tempB = nodiConnessi.get(nodeB.getIDMember());
        Iterator it = tempA.iterator();
        while(it.hasNext()){
            if(it.next().equals(inMemberA)){
                it.remove();
                break;
            }
        }
        if(tempB.isEmpty()){
            if( nodeB instanceof Bank){
                listaNodi.remove(nodeB); //rimuovo inMemberB dalla network
            }
            
        }


        return true;


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