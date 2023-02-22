public class Account{
    
    private static int numberOfAccounts = 0;
    private int accountID;
    private string handle;
    private string description;
    //private int[] posts;

    public Account(string givenHandle, string givenDescription){
        accountID = numberOfAccounts;
        numberOfAccounts += 1;
        handle = givenHandle;
        description = givenDescription;
    }

    public void setDescription(string newDescription){
        givenDescription = newDescription;
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

    public int createPost(string givenMessage){

    } 

    //add/remove to post array methods
}