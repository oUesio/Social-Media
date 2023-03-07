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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalCommentPosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMostEndorsedPost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMostEndorsedAccount() {
		// TODO Auto-generated method stub
		return 0;
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
