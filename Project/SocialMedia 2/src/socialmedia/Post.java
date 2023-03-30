package socialmedia;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable {
    private static final long serialVersionUID = 2L;
    
    private int postID;
    protected static int numberOfPosts = 0;
    private int accountID;
    private String message;
    private ArrayList<Integer> endorsementsList = new ArrayList<Integer>();
    private ArrayList<Integer> commentsList = new ArrayList<Integer>(); 

    public Post(int accountID, String message){
        this.accountID = accountID;
        this.message = message;
        postID = numberOfPosts;
        numberOfPosts += 1;
    }

    public String getMessage(){
        return message;
    }

    public int getPostID(){
        return postID;
    }

    public int getAccountID(){
        return accountID;
    }

    public void addEndorsementID(int endorsementID){ 
        endorsementsList.add(Integer.valueOf(endorsementID));
    }

    public void addCommentID(int commentID){ 
        commentsList.add(Integer.valueOf(commentID));
    }

    public ArrayList<Integer> getEndorsementsList(){
        return endorsementsList;
    }

    public ArrayList<Integer> getCommentsList(){
        return commentsList;
    }

    public void removeEndorsementID(int endorsementID){
        for (int pos = 0; pos < endorsementsList.size(); pos++) {
            if (endorsementsList.get(pos) == endorsementID) {
                endorsementsList.remove(pos);
                break;
            }
        }
    }

    public void removeCommentID(int commentID){
        for (int pos = 0; pos < commentsList.size(); pos++) {
            if (commentsList.get(pos) == commentID) {
                commentsList.remove(pos);
                break;
            }
        }
    }
    
    public void resetNumberOfPosts() {
        numberOfPosts = 0;
    }
}
