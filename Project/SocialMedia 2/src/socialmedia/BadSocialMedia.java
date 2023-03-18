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
	private ArrayList<DeletedComment> deletedCommentsList = new ArrayList<DeletedComment>();

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
		for (int accPos = 0; accPos < accountsList.size(); accPos++) {
			int postPos = accountsList.get(accPos).searchPost(id);
			int endorsePos = accountsList.get(accPos).searchEndorsement(id);
			int commentPos = accountsList.get(accPos).searchComment(id);
			if (postPos != -1) { //post found in this account's posts
				Post oldPost = accountsList.get(accPos).getOriginalPosts().get(postPos);
				if (oldPost.getCommentsList().size() != 0) { //doesn't create placeholder if no comments
					deletedPostsList.add(new DeletedPost(oldPost.getPostID(), oldPost.getCommentsList())); //placeholder post
				}
				ArrayList<Integer> oldEndorsements = oldPost.getEndorsementsList();
				for (Account acc : accountsList) { //looks at account list again
					ArrayList<Endorsement> endorsements = new ArrayList<Endorsement>(acc.getEndorsements());
					for (Endorsement endorse : endorsements) { //looks at endorsements
						if (oldEndorsements.contains(endorse.getPostID())) { //checks if id is in the list
							acc.getEndorsements().remove(endorse);
							oldEndorsements.remove((Integer) endorse.getPostID()); 
						}
					}
					if (oldEndorsements.size() == 0) {
						break;
					}
				}
				accountsList.get(accPos).removePostAt(postPos); //removes post object
				break; //stops looking through accounts when found
			} else if (endorsePos != -1) {
				Endorsement oldEndorsement = accountsList.get(accPos).getEndorsements().get(endorsePos);
				for (Account acc : accountsList) {
					int postPos2 = acc.searchPost(oldEndorsement.getPostReferenceID()); //finds post with same postrefid
					int commentPos2 = acc.searchComment(oldEndorsement.getPostReferenceID());
					if (postPos2 != -1) {
						acc.removeEndorseIDinPostAt(postPos2, oldEndorsement.getPostID()); //deletes endorseid from post
						break; //stops looking through accounts when found
					} else if (commentPos2 != -1) {
						acc.removeEndorseIDinCommentAt(commentPos2, oldEndorsement.getPostID());
						break;
					}
				}
				accountsList.get(accPos).removeEndorsementAt(endorsePos);
				break;
			} else if (commentPos != -1) {
				Comment oldComment = accountsList.get(accPos).getComments().get(commentPos);
				if (oldComment.getCommentsList().size() != 0) { //doesn't create placeholder if no comments
					deletedCommentsList.add(new DeletedComment(oldComment.getPostID(), oldComment.getCommentsList(), oldComment.getPostReferenceID())); //placeholder post
				}
				ArrayList<Integer> oldEndorsements = oldComment.getEndorsementsList();
				boolean commentFound = false;
				//checks for id in deleted lists
				for (int deletedPostPos = 0; deletedPostPos < deletedPostsList.size(); deletedPostPos++) {
					DeletedPost deletedPost = deletedPostsList.get(deletedPostPos);
					if (deletedPost.searchComment(id) != -1) {
						deletedPost.removeCommentIDAt(deletedPostPos); //removes id from deleted ref
						if (deletedPost.getCommentsList().size() == 0) { //checks if empty
							deletedPostsList.remove(deletedPostPos); //deletes if comment empty
						}
						commentFound = true;
						break;
					}
				}
				if (!commentFound) { //checks deleted comments
					for (int deletedCommentPos = 0; deletedCommentPos < deletedCommentsList.size(); deletedCommentPos++) {
						DeletedComment deletedComment = deletedCommentsList.get(deletedCommentPos);
						if (deletedComment.searchComment(id) != -1) {
							deletedComment.removeCommentIDAt(deletedCommentPos); //removes id from deleted ref
							if (deletedComment.getCommentsList().size() == 0) { //checks if empty
								deletedCommentsList.remove(deletedCommentPos); //deletes if comment empty
							}
							commentFound = true;
							break;
						}
					}
				}
				for (Account acc : accountsList) {
					if (oldEndorsements.size() != 0) {
						ArrayList<Endorsement> endorsements = new ArrayList<Endorsement>(acc.getEndorsements());
						for (Endorsement endorse : endorsements) { //looks at endorsements
							if (oldEndorsements.contains(endorse.getPostID())) { //checks if id is in the list
								acc.getEndorsements().remove(endorse); //removes endorse object
								oldEndorsements.remove((Integer) endorse.getPostID());
							}
						}
					}
					if (!commentFound) { //doesn't have to search if in deleted list
						int postPos2 = acc.searchPost(oldComment.getPostReferenceID()); //finds post with same postrefid
						int commentPos2 = acc.searchComment(oldComment.getPostReferenceID());
						if (postPos2 != -1) {
							acc.removeCommentIDinPostAt(postPos2, oldComment.getPostID()); //deletes commentid from post
							commentFound = true;
						} else if (commentPos2 != -1) {
							acc.removeCommentIDinCommentAt(commentPos2, oldComment.getPostID());
							commentFound = true;
						}
					}
					if (oldEndorsements.size() == 0 && commentFound) { //stops when all endorsements deleted and id removed from ref
						break;
					}
				}
				accountsList.get(accPos).removeCommentAt(commentPos);
				break;
			}
		}
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
		for (int pos = 0; pos < deletedPostsList.size(); pos++) {
			total += deletedPostsList.get(pos).getCommentsList().size();
		}
		for (int pos = 0; pos < deletedCommentsList.size(); pos++) {
			total += deletedCommentsList.get(pos).getCommentsList().size();
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
		indentationNumber = -1;

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
