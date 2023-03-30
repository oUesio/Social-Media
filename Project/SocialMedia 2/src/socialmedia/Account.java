package socialmedia;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static int numberOfAccounts = 0;
    private int accountID;
    private String handle;
    private String description;
    private ArrayList<Post> originalPostList = new ArrayList<Post>(); // account's posts
    private ArrayList<Endorsement> endorsementsList = new ArrayList<Endorsement>(); // account's reposts of other's
    private ArrayList<Comment> commentsList = new ArrayList<Comment>(); // account's comments

    public Account(String handle, String description){
        this.handle = handle;
        this.description = description;
        accountID = numberOfAccounts;
        numberOfAccounts += 1;
    }

    public Account(String handle){
        this.handle = handle;
        this.description = "";
        accountID = numberOfAccounts;
        numberOfAccounts += 1;
    }

    public void setDescription(String description){
        this.description = description;
    }
    
    public void setHandle(String handle){
        this.handle = handle;
    }

    public String getDescription(){
        return description;
    }

    public String getHandle(){
        return handle;
    }

    public int getAccountID(){
        return accountID;
    }

    public ArrayList<Post> getOriginalPosts() {
        return originalPostList;
    }

    public ArrayList<Endorsement> getEndorsements() {
        return endorsementsList;
    }

    public ArrayList<Comment> getComments() {
        return commentsList;
    }

    public int createOriginalPost(String message) {
        originalPostList.add(new Post(accountID, message));
        return originalPostList.get(originalPostList.size()-1).getPostID();
    }

    public int createEndorsement(String message, int postReferenceID) {
        endorsementsList.add(new Endorsement(accountID, message, postReferenceID));
        return endorsementsList.get(endorsementsList.size()-1).getPostID();
    }

    public int createComment(String message, int postReferenceID) {
        commentsList.add(new Comment(accountID, message, postReferenceID));
        return commentsList.get(commentsList.size()-1).getPostID();
    }
    
    public void removePostAt(int pos){
        originalPostList.remove(pos);
    }

    public void removeEndorsementAt(int pos){
        endorsementsList.remove(pos);
    }

    public void removeCommentAt(int pos){
        commentsList.remove(pos);
    }
    
    public int searchPost(int id) {
        for (int pos = 0; pos < originalPostList.size(); pos++) {
            if (originalPostList.get(pos).getPostID() == id) {
                return pos;
            }
        }
        return -1;
    }

    public int searchEndorsement(int id) {
        for (int pos = 0; pos < endorsementsList.size(); pos++) {
            if (endorsementsList.get(pos).getPostID() == id) {
                return pos;
            }
        }
        return -1;
    }

    public int searchComment(int id) {
        for (int pos = 0; pos < commentsList.size(); pos++) {
            if (commentsList.get(pos).getPostID() == id) {
                return pos;
            }
        }
        return -1;
    }

    public void addCommentIDtoPostAt(int pos, int commentID) {
        originalPostList.get(pos).addCommentID(commentID);
    }

    public void addEndorseIDtoPostAt(int pos, int endorseID) {
        originalPostList.get(pos).addEndorsementID(endorseID);
    }

    public void addCommentIDtoCommentAt(int pos, int commentID) {
        commentsList.get(pos).addCommentID(commentID);
    }

    public void addEndorseIDtoCommentAt(int pos, int endorseID) {
        commentsList.get(pos).addEndorsementID(endorseID);
    }
    
    public void removeCommentIDinPostAt(int pos, int commentID) {
        originalPostList.get(pos).removeCommentID(commentID);;
    }

    public void removeEndorseIDinPostAt(int pos, int endorseID) {
        originalPostList.get(pos).removeEndorsementID(endorseID);
    }

    public void removeCommentIDinCommentAt(int pos, int commentID) {
        commentsList.get(pos).removeCommentID(commentID);
    }

    public void removeEndorseIDinCommentAt(int pos, int endorseID) {
        commentsList.get(pos).removeEndorsementID(endorseID);;
    }
}
