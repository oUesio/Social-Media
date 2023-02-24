public class Account{
    
    private static int numberOfAccounts = 0;
    private int accountID;
    private String handle;
    private String description;

    public Account(string handle, string description){
        this.handle = givenHandle;
        this.description = description;
        accountID = numberOfAccounts;
        numberOfAccounts += 1;
    }

    public void setDescription(string description){
        this.description = description;
    }
    
    public void setHandle(string newHandle){
        handle = newHandle;
    }

    public string getDescription(){
        return description;
    }

    public string getHandle(){
        return handle;
    }
}
