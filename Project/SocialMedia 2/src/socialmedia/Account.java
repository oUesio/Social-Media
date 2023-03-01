package socialmedia;

import java.util.ArrayList;

public class Account{
    
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

}