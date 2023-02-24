//package socialmedia;

public class Post{
    private int postID;
    protected int numberOfPosts = 0;
    private int accountID;
    private String text;

    public Post(int accountID, String text){
        postID = numberOfPosts;
        numberOfPosts += 1;
        this.accountID = accountID;
        this.text = text;
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

}