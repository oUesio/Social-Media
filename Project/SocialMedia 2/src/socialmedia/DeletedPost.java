package socialmedia;

import java.io.Serializable;
import java.util.ArrayList;

public class DeletedPost implements Serializable {
    private static final long serialVersionUID = 5L;
    
    private int postID; //postID of old post
    private ArrayList<Integer> commentsList; //comments of the old post
    protected String message = "The original content was removed from the system and is no longer available.";

    public DeletedPost(int postID, ArrayList<Integer> commentsList){
        this.postID = postID;
        this.commentsList = commentsList;
    }

    public String getMessage(){
        return message;
    }

    public int getPostID(){
        return postID;
    }

    public ArrayList<Integer> getCommentsList(){
        return commentsList;
    }

    public int searchComment(int id) {
        for (int pos = 0; pos < commentsList.size(); pos++) {
            if (commentsList.get(pos) == id) {
                return pos;
            }
        }
        return -1;
    }

    public void removeCommentIDAt(int pos){
        commentsList.remove(pos);
    }
}
