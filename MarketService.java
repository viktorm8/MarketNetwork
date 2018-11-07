import java.time.temporal.TemporalAccessor;
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
        if(Bank.numberOfBanks < Bank.maxBanks ){
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
            if (iterA.next() == inMemberB){
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
            if (iterB.next() == inMemberA){
                System.out.println("NODI GIA CONNESSI!\n");
                return false;
            } 
        }

        Random r = new Random();
        int arcID = r.nextInt();
        listaNodi.forEach( nodo -> System.out.println(nodo));
        Iterator ite = listaNodi.iterator();
        boolean foundA = false;
        boolean foundB = false;
        while(ite.hasNext()){
            Member m1 = (Member)ite.next();
            if(m1 == inMemberA){
                foundA = true;
                System.out.println("A già presente!");
            }
            if(m1 == inMemberB){
                foundB = true;
                System.out.println("B già presente!");
            }
        }
        boolean addA = false;
        boolean addB = false;
        if((foundA) && (!foundB)){
            //instanzia B
            addB = true;
            listaNodi.add(inMemberB);
        }
        else if((!foundA) && (foundB)){
            //instanzia A
            addA = true;
            listaNodi.add(inMemberA);
        }
        else {
            //do nothing
        }
        
        if( ((foundA) && (foundB) ) || ((foundA) && (addB)) || ((addA) && (foundB))){
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
            LinkedList<Member> listaNodiConnessiAdA;
            if(nodiConnessi.get(inMemberA.getIDMember()) == null){
                listaNodiConnessiAdA = new LinkedList<>();
            }else{
                listaNodiConnessiAdA = nodiConnessi.get(inMemberA.getIDMember());
            }

            LinkedList<Member> listaNodiConnessiAdB;
            if(nodiConnessi.get(inMemberB.getIDMember()) == null){
                listaNodiConnessiAdB = new LinkedList<>();
            }else{
                listaNodiConnessiAdB = nodiConnessi.get(inMemberB.getIDMember());
            }

            listaNodiConnessiAdA.add(inMemberB);
            listaNodiConnessiAdB.add(inMemberA);
            nodiConnessi.remove(inMemberA.getIDMember());
            nodiConnessi.remove(inMemberB.getIDMember());
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
            if(((nodeA == inMemberA) && (nodeB == inMemberB)) 
            || ((nodeB == inMemberA) && (nodeA == inMemberB))){
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
            if(ite.next() == inMemberB){
                ite.remove();
                break;
            }
        }
        nodiConnessi.remove(nodeA.getIDMember());

        if(tempA.isEmpty()){
            if( nodeA instanceof Bank){
                listaNodi.remove(nodeA); //rimuovo la banca dalla network
            }
            
        }else{
            nodiConnessi.put(inMemberA.getIDMember(), tempA);
        }

        //Da NODO B a NODA A
        LinkedList<Member> tempB = nodiConnessi.get(nodeB.getIDMember());
        Iterator it = tempA.iterator();
        while(it.hasNext()){
            if(it.next() == inMemberA){
                it.remove();
                break;
            }
        }

        nodiConnessi.remove(nodeB.getIDMember());

        if(tempB.isEmpty()){
            if( nodeB instanceof Bank){
                listaNodi.remove(nodeB); //rimuovo inMemberB dalla network
            }
            
        }else{
            nodiConnessi.put(inMemberB.getIDMember(), tempA);
        }


        return true;


    }

    public void visit(Bank inBank, Collection<Member> outMembers){
        
        LinkedList<Member> nodiConnessiABank = nodiConnessi.get(inBank.idMember);
        Iterator ite = nodiConnessiABank.iterator();
        outMembers = new LinkedList<>();
        while(ite.hasNext()){
            Member MemberToAdd = (Trader)ite.next();
            outMembers.add(MemberToAdd);
            if(MemberToAdd instanceof Trader){
                LinkedList<Member> nodiConnessiATrader = nodiConnessi.get(MemberToAdd.idMember);
                //They are PrivateInvestors
                Iterator iter = nodiConnessiATrader.iterator();
                while(iter.hasNext()){
                    PrivateInvestor pI = (PrivateInvestor) iter.next();
                    outMembers.add((Member)pI);
                }

            }

        }
    }

            
        

   


}