//package socialmedia;

public class Post{
    private int postID;
    protected static int numberOfPosts = 0;
    private int accountID;
    private String text;

    public Post(int accountID, String text){
        this.accountID = accountID;
        this.text = text;
        postID = numberOfPosts;
        numberOfPosts += 1;
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

    //needs to store the endorsements and comments here?
    //do we do checks here to see if account/other info is valid?
}