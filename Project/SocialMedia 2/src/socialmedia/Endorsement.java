
public class Endorsement extends Post {
    private int postReferenceID;
    private String message;

    public Endorsement(int accountID, String text, int postReferenceID, String message) {
        super(accountID, text);
        this.postReferenceID = postReferenceID;
        this.message = message;
    }

    public int getPostReferenceID(){
        return postReferenceID;
    }

    public String message(){
        return message;
    }
    
}
