package socialmedia;

public class Endorsement extends Post {
    private static final long serialVersionUID = 4L;
    
    private int postReferenceID;

    public Endorsement(int accountID, String message, int postReferenceID) {
        super(accountID, message); //pass in the message of the post we are endorsing
        this.postReferenceID = postReferenceID;
    }

    public int getPostReferenceID(){
        return postReferenceID;
    }
}
