package socialmedia;

import java.util.ArrayList;

public class DeletedComment extends DeletedPost{
    private static final long serialVersionUID = 6L;
    
    private int postReferenceID;

    public DeletedComment(int postID, ArrayList<Integer> commentsList, int postReferenceID) {
        super(postID, commentsList);
        this.postReferenceID = postReferenceID;
    }

    public int getPostReferenceID(){
        return postReferenceID;
    }
}
