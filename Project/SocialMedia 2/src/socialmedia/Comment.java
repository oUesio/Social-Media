package socialmedia;

public class Comment extends Post{
    private int postReferenceID;

    public Comment(int accountID, String message, int postReferenceID) {
        super(accountID, message);
        this.postReferenceID = postReferenceID;
    }

    public int getPostReferenceID(){
        return postReferenceID;
    }
}
