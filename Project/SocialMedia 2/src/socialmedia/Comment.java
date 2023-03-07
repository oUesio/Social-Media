package socialmedia;

import java.util.ArrayList;

public class Comment extends Post{
    private int postReferenceID;
    private ArrayList<Integer> endorsementsList = new ArrayList<Integer>();
    private ArrayList<Integer> commentsList = new ArrayList<Integer>(); 

    public Comment(int accountID, String message, int postReferenceID) {
        super(accountID, message);
        this.postReferenceID = postReferenceID;
    }

    public int getPostReferenceID(){
        return postReferenceID;
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
}
