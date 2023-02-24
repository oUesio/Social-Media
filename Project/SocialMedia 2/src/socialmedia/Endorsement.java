
public class Endorsement extends Post {
    private int postReferenceID;

    public Endorsement(int accountID, String message, int postReferenceID) {
        super(accountID, message);
        this.postReferenceID = postReferenceID;
    }

    public int getPostReferenceID(){
        return postReferenceID;
    }
    //do we do checks here to see if account/other info is valid?
}