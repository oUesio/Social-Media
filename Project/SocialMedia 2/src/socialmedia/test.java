public class test{

    public static void main(String args[]){
        Account newAccount = new Account("handle", "description");
        Account newAccount2 = new Account("handle2", "description2");
        System.out.println("Account 1 has ID:" + newAccount.getAccountID());
        System.out.println("Account 2 has ID:" + newAccount2.getAccountID());
        Post newPost = new Post(0, "this is a post, my first post");
        System.out.println("Post 1 has ID:" + newPost.getPostID());
        System.out.println("Post 1 has Text:" + newPost.getText());
        Post newPost2 = new Post(0, "this is another post, another post");
        System.out.println("Post 2 has ID:" + newPost.getPostID());
        System.out.println("Post 2 has Text:" + newPost2.getText());
    }
}