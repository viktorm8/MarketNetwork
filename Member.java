class Member{

    int idMember;

    static int countID = 0;

    public Member(){
        this.idMember = countID;
        countID ++;
    }

    public int getIDMember(){
        return this.idMember;
    }
}