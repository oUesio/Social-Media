
//package socialmedia;

public class Comments extends Post{
    private int postReferenceID;

    public Comments(int accountID, String text, int postReferenceID) {
        super(accountID, text);
        this.postReferenceID = postReferenceID;
    }

    public int getPostReferenceID(){
        return postReferenceID;
    }
    
}
