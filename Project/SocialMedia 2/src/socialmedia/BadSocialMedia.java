package socialmedia;

import java.io.IOException;
import java.util.ArrayList;

/**
 * BadSocialMedia is a minimally compiling, but non-functioning implementor of
 * the SocialMediaPlatform interface.
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class BadSocialMedia implements SocialMediaPlatform {
	private ArrayList<Account> accountsList = new ArrayList<Account>();

	@Override
	public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
		accountsList.add(new Account(handle));
		return accountsList.get(accountsList.size()-1).getAccountID();
	}

	@Override
	public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		accountsList.add(new Account(handle, description));
		return accountsList.get(accountsList.size()-1).getAccountID();
	}

	@Override
	public void removeAccount(int id) throws AccountIDNotRecognisedException {
		accountsList.removeIf(acc -> (acc.getAccountID() == id));
	}

	@Override
	public void removeAccount(String handle) throws HandleNotRecognisedException {
		accountsList.removeIf(acc -> (acc.getHandle() == handle));
	}

	@Override
	public void changeAccountHandle(String oldHandle, String newHandle)
			throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public String showAccount(String handle) throws HandleNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
		for (int pos = 0; pos < accountsList.size(); pos++) {
			if (accountsList.get(pos).getHandle() == handle) {
				return accountsList.get(pos).createOriginalPost(message);
			}
		}
		return -1; //temp value
	}

	@Override
	public int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
				for (int pos = 0; pos < accountsList.size(); pos++) {
					if (accountsList.get(pos).getHandle() == handle) {
						//return accountsList.get(pos).createEndorsement(id); 
					}
				}
				return -1;
			}

	@Override
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
				for (int pos = 0; pos < accountsList.size(); pos++) {
					if (accountsList.get(pos).getHandle() == handle) {
						return accountsList.get(pos).createComment(message, id);
					}
				}
				return -1;
			}

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		for (int pos = 0; pos < accountsList.size(); pos++) {
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			for (int x = 0; x < postsList.size(); x++){
				if (postsList.get(x).getPostID() == id){
					return String.format("<pre>\nID: %d\nAccount: %s\nNo. endorsements: %s | No. comments: %s\n%s\n</pre>", id, accountsList.get(pos).getHandle(), accountsList.get(pos).getEndorsements().size(), accountsList.get(pos).getComments().size(), postsList.get(x).getMessage());
				}
			}
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
			for (int x = 0; x < commentsList.size(); x++){ //for every comment in comments list
				if (commentsList.get(x).getPostID() == id){ //if the comment has the ID we are looking for
	//keeping just in case can't get other way working:
					//int totalComments = 0; //(total comment number for later)
					//for (int pos2 = 0; pos2 < accountsList.size(); pos2++) {
						//ArrayList<Comment> commentsList2 = accountsList.get(pos2).getComments();
						//for (int y = 0; y < commentsList2.size(); y++){ //check every comment for every account
							//if (commentsList2.get(y).getPostReferenceID() == commentsList.get(x).getPostID()){ //if the reference id is the same as the comment ID, increment counter by 1
								//totalComments+= 1; //just realised all of this is useless as I can just do commentsList.get(x).getCommentsList().size()!!!!!!!!!!!!!!! (slightly frustrated)
							//}
						//}
					//}
					//would have to repeat for endorsements too, creating horribly inefficient code.
					return String.format("<pre>\nID: %d\nAccount: %s\nNo. endorsements: %s | No. comments: %s\n%s\n</pre>", id, accountsList.get(pos).getHandle(), commentsList.get(x).getEndorsementsList().size(), commentsList.get(x).getCommentsList().size(), commentsList.get(x).getMessage());
				}
			}
			ArrayList<Endorsement> endorsementsList = accountsList.get(pos).getEndorsements();
			for (int x = 0; x < endorsementsList.size(); x++){
				if (endorsementsList.get(x).getPostID() == id){
					return String.format("<pre>\nID: %d\nAccount: %s\nNo. endorsements: %s N/A | No. comments: %s N/A\n%s\n</pre>", id, accountsList.get(pos).getHandle(), 0, 0, endorsementsList.get(x).getMessage());
				}
			}
		}
		return null;
	}

	@Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfAccounts() {
		return accountsList.size();
	}

	@Override
	public int getTotalOriginalPosts() { //when an account is deleted, delete it from the account so it is not counted here
		int total = 0;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			total += accountsList.get(pos).getOriginalPosts().size();
		}
		return total;
	}

	@Override
	public int getTotalEndorsmentPosts() {
		int total = 0;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			total += accountsList.get(pos).getEndorsements().size();
		}
		return total;
	}

	@Override
	public int getTotalCommentPosts() {
		int total = 0;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			total += accountsList.get(pos).getComments().size();
		}
		return total;
	}

	@Override
	public int getMostEndorsedPost() {
		int largestNumber = 0;
		int mostEndorsedPostID = -1;
		for (int pos = 0; pos < accountsList.size(); pos++) { //for all accounts, check the OG posts list
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			for (int x = 0; x < postsList.size(); x++){
				if (postsList.get(x).getEndorsementsList().size() > largestNumber){
					largestNumber = postsList.get(x).getEndorsementsList().size();
					mostEndorsedPostID = postsList.get(x).getPostID();
				}
			}
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments(); //also check the comments list
			for (int x = 0; x < commentsList.size(); x++){
				if (commentsList.get(x).getEndorsementsList().size() > largestNumber){
					largestNumber = commentsList.get(x).getEndorsementsList().size();
					mostEndorsedPostID = commentsList.get(x).getPostID();
				}
			}
		}
		return mostEndorsedPostID;
	}

	@Override
	public int getMostEndorsedAccount() {
		int maxNumberOfEndorsements = 0;
		int mostEndorsedID = -1;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			int numberOfEndorsements = 0;
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			for (int x = 0; x < postsList.size(); x++){
				numberOfEndorsements += postsList.get(x).getEndorsementsList().size();
			}
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
			for (int x = 0; x < commentsList.size(); x++){
				numberOfEndorsements += commentsList.get(x).getEndorsementsList().size();
			}
			if (numberOfEndorsements > maxNumberOfEndorsements){
				maxNumberOfEndorsements = numberOfEndorsements;
				mostEndorsedID = accountsList.get(pos).getAccountID();
			}
		}
		return mostEndorsedID;
	}

	@Override
	public void erasePlatform() {
		accountsList.clear();

	}

	@Override
	public void savePlatform(String filename) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

}
