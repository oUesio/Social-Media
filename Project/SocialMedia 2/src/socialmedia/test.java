public class test{

    public static void main(String args[]){
        Account newAccount = new Account("handle", "description");
        System.out.print(newAccount.getDescription());
        Account newAccount2 = new Account("handle2", "description2");
        System.out.println(newAccount.getAccountID());
        System.out.println(newAccount2.getAccountID());
    }
}