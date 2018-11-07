class Arc{

    public int idArc;
    private Member nodeA, nodeB;

    public Arc(int id){
        this.idArc = id;
    }

    public void setNodes(Member nA, Member nB){
        this.nodeA = nA;
        this.nodeB = nB;
    }

    public void setNodeA(Member a){
        this.nodeA = a;
    }

    public void setNodeB(Member b){
        this.nodeB = b;
    }
    
    public Member getNodeA(){
        return this.nodeA;
    }

    public Member getNodeB(){
        return this.nodeB;
    }

    public int getArcID(){
        return idArc;
    }
}