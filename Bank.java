class Bank extends Member{

    int bankCode;
    final static int maxBanks = 15; //default number of max number of banks
    static int numberOfBanks = 0;

    Bank(int code){
        super();  
        numberOfBanks ++;      
        this.bankCode = code;
    }

    int getCode(){
        return this.bankCode;
    }

    void investMoney(Trader t, long cash){
        //invest money
    }


}