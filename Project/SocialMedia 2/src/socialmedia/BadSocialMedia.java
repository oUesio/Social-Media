//package socialmedia;

import java.io.IOException;
import java.util.ArrayList;

import javax.print.attribute.standard.NumberOfDocuments;

/**
 * BadSocialMedia is a minimally compiling, but non-functioning implementor of
 * the SocialMediaPlatform interface.
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class BadSocialMedia implements SocialMediaPlatform {
	private ArrayList<Account> accountsList = new ArrayList<Account>();
	private ArrayList<DeletedPost> deletedPostsList = new ArrayList<DeletedPost>();

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
						int endorseID = accountsList.get(pos).createEndorsement(accountsList.get(pos).getDescription(), id); //id of endorsement
						for (Account acc : accountsList) {
							int foundPostIDPos = acc.searchPost(id);
							if (foundPostIDPos != -1) {
								acc.addEndorseIDtoPostAt(foundPostIDPos, endorseID);
								break;
							}
							int foundCommentIDPos = acc.searchComment(id);
							if (foundCommentIDPos != -1) {
								acc.addEndorseIDtoCommentAt(foundCommentIDPos, endorseID);
								break;
							}
						}
						return endorseID;
					}
				}
				return -1;
			}

	@Override
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
				for (int pos = 0; pos < accountsList.size(); pos++) {
					if (accountsList.get(pos).getHandle() == handle) {
						int commentID =  accountsList.get(pos).createComment(message, id);
						for (Account acc : accountsList) {
							int foundPostIDPos = acc.searchPost(id);
							if (foundPostIDPos != -1) {
								acc.addCommentIDtoPostAt(foundPostIDPos, commentID);
								break;
							}
							int foundCommentIDPos = acc.searchComment(id);
							if (foundCommentIDPos != -1) {
								acc.addCommentIDtoCommentAt(foundCommentIDPos, commentID);
								break;
							}
						}
						return commentID;
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
					return String.format("<pre>\nID: %d\nAccount: %s\nNo. endorsements: %s | No. comments: %s\n%s\n</pre>", id, accountsList.get(pos).getHandle(), commentsList.get(x).getEndorsementsList().size(), commentsList.get(x).getCommentsList().size(), commentsList.get(x).getMessage());
				}
			}
			ArrayList<Endorsement> endorsementsList = accountsList.get(pos).getEndorsements();
			for (int x = 0; x < endorsementsList.size(); x++){
				if (endorsementsList.get(x).getPostID() == id){
					return String.format("<pre>\nID: %d\nAccount: %s\nNo. endorsements: %s N/A | No. comments: %s N/A\n%s\n</pre>", id, accountsList.get(pos).getHandle(), 0, 0, "N/A");
				}
			}
		}
		return null;
	}

	private int indentationNumber = -1;
	@Override
	public StringBuilder showPostChildrenDetails(int id) //so long
			throws PostIDNotRecognisedException, NotActionablePostException {	
		indentationNumber += 1;
		ArrayList<Integer> elementsList = new ArrayList<Integer>();
		for (int pos = 0; pos < accountsList.size(); pos++) {
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			for (int x = 0; x < postsList.size(); x++){
				if (postsList.get(x).getPostID() == id){
					//Adding to the string builder (Very long and have to have thrice here...)
					StringBuilder string = new StringBuilder();
					String tab = "\t";
					int changeableIndentationNumber = indentationNumber;
					if (changeableIndentationNumber == 0){
						string.append("<pre>");
					}
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					if (changeableIndentationNumber != 0){
						string.append("|");
					}
					string.append("> ID:");
					string.append(Integer.toString(id) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("Account: " + Integer.toString(postsList.get(x).getAccountID()) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("No. endorsements: " + Integer.toString(accountsList.get(pos).getEndorsements().size()) + " | No. comments: " + Integer.toString(accountsList.get(pos).getComments().size()) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append(postsList.get(x).getMessage() + "\n |");

					for (int y = 0; y < postsList.get(x).getCommentsList().size(); y++){
						ArrayList<Integer> childElementsList = postsList.get(x).getCommentsList();
						for (int z = 0; z < childElementsList.size(); z++){
							string.append(showPostChildrenDetails(childElementsList.get(z)));
						}
					}
					indentationNumber -= 1;
					return string;
				}
			}
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
			for (int x = 0; x < commentsList.size(); x++){ //for every comment in comments list
				if (commentsList.get(x).getPostID() == id){ //if the comment has the ID we are looking for
					StringBuilder string = new StringBuilder();
					String tab = "\t";
					int changeableIndentationNumber = indentationNumber;
					if (changeableIndentationNumber == 0){
						string.append("<pre>");
					}
					while (changeableIndentationNumber > 1){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					if (changeableIndentationNumber != 0){
						string.append("|");
					}
					string.append(" > ID:");
					string.append(Integer.toString(id) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("Account: " + Integer.toString(commentsList.get(x).getAccountID()) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("No. endorsements: " + Integer.toString(accountsList.get(pos).getEndorsements().size()) + " | No. comments: " + Integer.toString(accountsList.get(pos).getComments().size()) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append(commentsList.get(x).getMessage() + "\n |");

					for (int y = 0; y < commentsList.get(x).getCommentsList().size(); y++){
						ArrayList<Integer> childElementsList = commentsList.get(x).getCommentsList();
						for (int z = 0; z < childElementsList.size(); z++){
							string.append(showPostChildrenDetails(childElementsList.get(z)));
						}
					}
					indentationNumber -= 1;
					return string;
				}
			}
		}
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
	public void erasePlatform() { //???
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
