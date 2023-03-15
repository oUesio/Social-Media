package socialmedia;

import java.util.ArrayList;

public class DeletedPost {
    private int postID; //postID of old post
    private ArrayList<Integer> commentsList; //comments of the old post
    private String message = "The original content was removed from the system and is no longer available.";

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

    public ArrayList<Integer> getCommentsList(){ //maybe not needed
        return commentsList;
    }

    public void removeCommentID(int commentID){
        for (int pos = 0; pos < commentsList.size(); pos++) {
            if (commentsList.get(pos) == commentID) {
                commentsList.remove(pos);
                break;
            }
        }
    }
}