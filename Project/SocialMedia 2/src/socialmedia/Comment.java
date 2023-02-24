
//package socialmedia;

public class Comment extends Post{
    private int postReferenceID;

    public Comment(int accountID, String message, int postReferenceID) {
        super(accountID, message);
        this.postReferenceID = postReferenceID;
    }

    public int getPostReferenceID(){
        return postReferenceID;
    }
    
    //what is different to the endorsement?
    //do we do checks here to see if account/other info is valid?
}
