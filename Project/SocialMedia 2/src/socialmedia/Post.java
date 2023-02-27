//package socialmedia;
import java.util.ArrayList;

public class Post{
    private int postID;
    protected static int numberOfPosts = 0;
    private int accountID;
    private String text;
    private ArrayList<Integer> endorsementsList = new ArrayList<Integer>();
    private ArrayList<Integer> commentsList = new ArrayList<Integer>(); 

    public Post(int accountID, String text){
        this.accountID = accountID;
        this.text = text;
        postID = numberOfPosts;
        numberOfPosts += 1;
    }

    public void addEndorsementID(int endorsementID){ //to add endorsement ID to list of ID's as required (for the tree of posts)
        Integer iInteger = Integer.valueOf(endorsementID);
        endorsementsList.add(iInteger);
    }

    public void addCommentID(int commentID){ //to add comment ID to list of ID's as required (for the tree of posts)
        Integer iInteger = Integer.valueOf(commentID);
        commentsList.add(iInteger);
    }

    public ArrayList<Integer> getEndorsementsList(){
        return endorsementsList;
    }

    public ArrayList<Integer> getCommentsList(){
        return commentsList;
    }

    public void removeEndorsementID(int givenEndorsementID){
        //remove ID from list (throw error if not in list?)
    }

    public void removeCommentID(int givenEndorsementID){
        //remove ID from list (throw error if not in list?)
    }


    public String getText(){
        return text;
    }

    public int getPostID(){
        return postID;
    }

    public int getAccountID(){
        return accountID;
    }

    //needs to store the endorsements and comments here? If so, maybe have array in here that we add to every time a new post is made, for example newEndorsement(EndorsementPostID:0,...) new endorsement has ID of 1 for example, updateArrayOf(postID:0,newPostID:1)
    //do we do checks here to see if account/other info is valid?
}
