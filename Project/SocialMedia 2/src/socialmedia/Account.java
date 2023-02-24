public class Account{
    
    private static int numberOfAccounts = 0;
    private int accountID;
    private String handle;
    private String description;

    public Account(String handle, String description){
        this.handle = handle;
        this.description = description;
        accountID = numberOfAccounts;
        numberOfAccounts += 1;
    }

    public void setDescription(String description){
        this.description = description;
    }
    
    public void setHandle(String handle){
        this.handle = handle;
    }

    public String getDescription(){
        return description;
    }

    public String getHandle(){
        return handle;
    }
}
