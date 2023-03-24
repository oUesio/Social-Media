package socialmedia;

import java.io.IOException;
import java.util.ArrayList;

public class SocialMedia implements SocialMediaPlatform {
	private ArrayList<Account> accountsList = new ArrayList<Account>();
	private ArrayList<DeletedPost> deletedPostsList = new ArrayList<DeletedPost>();
	private ArrayList<DeletedComment> deletedCommentsList = new ArrayList<DeletedComment>();

	@Override
	public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
		//Creates new account object, with only a handle, which is added to accountsList
		accountsList.add(new Account(handle)); 
		return accountsList.get(accountsList.size()-1).getAccountID();
	}

	@Override
	public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		//Creates new account object, with a handle and a description, which is added to accountsList
		accountsList.add(new Account(handle, description));
		return accountsList.get(accountsList.size()-1).getAccountID();
	}

	@Override
	public void removeAccount(int id) throws AccountIDNotRecognisedException {
		for (int accPos = 0; accPos < accountsList.size(); accPos++) {
			//Finds the account with the id
			if (accountsList.get(accPos).getAccountID() == id) {
				//Deletes all posts, comments and endorsements of the account
				try {
					ArrayList<Post> tempPosts = new ArrayList<Post>(accountsList.get(accPos).getOriginalPosts());
					for (Post post : tempPosts) {
						deletePost(post.getPostID());
					}
					ArrayList<Comment> tempComments = new ArrayList<Comment>(accountsList.get(accPos).getComments());
					for (Comment comment : tempComments) {
						deletePost(comment.getPostID());
					}
					ArrayList<Endorsement> tempEndorsements = new ArrayList<Endorsement>(accountsList.get(accPos).getEndorsements());
					for (Endorsement endorse : tempEndorsements) {
						deletePost(endorse.getPostID());
					}
				} catch (PostIDNotRecognisedException e) {
					continue;
				}
				//Deletes the account with the id
				accountsList.remove(accountsList.get(accPos));
			}
		}
	}

	@Override
	public void removeAccount(String handle) throws HandleNotRecognisedException {
		for (int accPos = 0; accPos < accountsList.size(); accPos++) {
			//Finds the account with the handle
			if (accountsList.get(accPos).getHandle() == handle) {
				//Deletes all posts, comments and endorsements of the account
				try {
					ArrayList<Post> tempPosts = new ArrayList<Post>(accountsList.get(accPos).getOriginalPosts());
					for (Post post : tempPosts) {
						deletePost(post.getPostID());
					}
					ArrayList<Comment> tempComments = new ArrayList<Comment>(accountsList.get(accPos).getComments());
					for (Comment comment : tempComments) {
						deletePost(comment.getPostID());
					}
					ArrayList<Endorsement> tempEndorsements = new ArrayList<Endorsement>(accountsList.get(accPos).getEndorsements());
					for (Endorsement endorse : tempEndorsements) {
						deletePost(endorse.getPostID());
					}
				} catch (PostIDNotRecognisedException e) {
					continue;
				}
				//Deletes the account with the handle
				accountsList.remove(accountsList.get(accPos));
			}
		}
	}

	@Override
	public void changeAccountHandle(String oldHandle, String newHandle)
			throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
		for (int pos = 0; pos < accountsList.size(); pos++) {
			//Finds the account with the oldHandle
			if (accountsList.get(pos).getHandle() == oldHandle) {
				//Changes the handle
				accountsList.get(pos).setHandle(newHandle);
				return;
			}
		}
	}

	@Override
	public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
		for (int pos = 0; pos < accountsList.size(); pos++) {
			//Finds the account with the handle
			if (accountsList.get(pos).getHandle() == handle) {
				//Updates the description
				accountsList.get(pos).setDescription(description);
				return;
			}
		}
	}

	@Override
	public String showAccount(String handle) throws HandleNotRecognisedException {
		int totalEndorsements = 0;
		for (Account acc : accountsList) {
			//Finds accounts with the handle
			if (acc.getHandle() == handle){
				ArrayList<Post> postsList = acc.getOriginalPosts();
				ArrayList<Comment> commentsList = acc.getComments();
				//Finds sum of endorsements received
				for (int x = 0; x < postsList.size(); x++){
					totalEndorsements += postsList.get(x).getEndorsementsList().size();
				}
				for (int x = 0; x < commentsList.size(); x++){
					totalEndorsements += commentsList.get(x).getEndorsementsList().size();
				}
				return String.format("<pre>\nID: %d\nHandle: %s\nDescription: %s\nPost count: %d\nEndorse count: %s\n</pre>", acc.getAccountID(), acc.getHandle(), acc.getDescription(), (acc.getComments().size() + acc.getEndorsements().size() + acc.getOriginalPosts().size()), totalEndorsements);
			}
		}
		return null;
	}

	@Override
	public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
		//Checks the message is inside the character limit
		if (message.length() <= 100) {
			for (int pos = 0; pos < accountsList.size(); pos++) {
				//Finds account with the handle
				if (accountsList.get(pos).getHandle() == handle) {
					return accountsList.get(pos).createOriginalPost(message);
				}
			}	
		}
		return -1; //Failed to create
	}

	@Override
	public int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
				for (int pos = 0; pos < accountsList.size(); pos++) {
					//Finds account with the handle
					if (accountsList.get(pos).getHandle() == handle) {
						int endorseID = accountsList.get(pos).createEndorsement(accountsList.get(pos).getDescription(), id);
						for (Account acc : accountsList) {
							//Adds endorsement ID to post when the ID of the post being endorsed is found in an account
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
				//Checks the message is inside the character limit
				if (message.length() <= 100) {
					for (int pos = 0; pos < accountsList.size(); pos++) {
						if (accountsList.get(pos).getHandle() == handle) {
							int commentID =  accountsList.get(pos).createComment(message, id);
							for (Account acc : accountsList) {
								//Adds comment ID to post when the ID of the post being commented is found in an account
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
				}
				return -1;
			}

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
		for (int accPos = 0; accPos < accountsList.size(); accPos++) {
			//Looks for post, endorsement or comment with the ID
			int postPos = accountsList.get(accPos).searchPost(id);
			int endorsePos = accountsList.get(accPos).searchEndorsement(id);
			int commentPos = accountsList.get(accPos).searchComment(id);
			//ID found in original posts
			if (postPos != -1) {
				Post oldPost = accountsList.get(accPos).getOriginalPosts().get(postPos);
				//Placeholder creation, doesn't create if there are no comments
				if (oldPost.getCommentsList().size() != 0) {
					deletedPostsList.add(new DeletedPost(oldPost.getPostID(), oldPost.getCommentsList()));
				}
				ArrayList<Integer> oldEndorsements = oldPost.getEndorsementsList();
				//Looks through accounts again to find endorsements made on the post
				for (Account acc : accountsList) { 
					ArrayList<Endorsement> endorsements = new ArrayList<Endorsement>(acc.getEndorsements());
					for (Endorsement endorse : endorsements) {
						//Checks if the ID is in the list of endorsement IDs to be deleted
						if (oldEndorsements.contains(endorse.getPostID())) {
							//Deletes endorsement object
							acc.getEndorsements().remove(endorse);
							//Removes from endorsements to be deleted list since it has been found
							oldEndorsements.remove((Integer) endorse.getPostID()); 
						}
					}
					//Stops early when all endorsements have been found and deleted
					if (oldEndorsements.size() == 0) {
						break;
					}
				}
				//Removes the post object
				accountsList.get(accPos).removePostAt(postPos);
				break;
			} else if (endorsePos != -1) { //ID found in endorsements
				Endorsement oldEndorsement = accountsList.get(accPos).getEndorsements().get(endorsePos);
				for (Account acc : accountsList) {
					//Looks for post or comment with the post reference ID
					int postPos2 = acc.searchPost(oldEndorsement.getPostReferenceID());
					int commentPos2 = acc.searchComment(oldEndorsement.getPostReferenceID());
					//Deletes endorsement ID from post
					if (postPos2 != -1) {
						acc.removeEndorseIDinPostAt(postPos2, oldEndorsement.getPostID());
						break;
					} else if (commentPos2 != -1) {
						acc.removeEndorseIDinCommentAt(commentPos2, oldEndorsement.getPostID());
						break;
					}
				}
				//Removes the endorsement object
				accountsList.get(accPos).removeEndorsementAt(endorsePos);
				break;
			} else if (commentPos != -1) { //ID found in comments
				Comment oldComment = accountsList.get(accPos).getComments().get(commentPos);
				//Placeholder creation, doesn't create if there are no comments
				if (oldComment.getCommentsList().size() != 0) {
					deletedCommentsList.add(new DeletedComment(oldComment.getPostID(), oldComment.getCommentsList(), oldComment.getPostReferenceID())); //placeholder post
				}
				ArrayList<Integer> oldEndorsements = oldComment.getEndorsementsList();
				boolean commentFound = false;
				//Checks the placeholder posts for comment ID
				for (int deletedPostPos = 0; deletedPostPos < deletedPostsList.size(); deletedPostPos++) {
					DeletedPost deletedPost = deletedPostsList.get(deletedPostPos);
					if (deletedPost.searchComment(id) != -1) {
						//Removes ID from the reference post
						deletedPost.removeCommentIDAt(deletedPostPos);
						//Deletes the placeholder if it doesn't contain any comments
						if (deletedPost.getCommentsList().size() == 0) {
							deletedPostsList.remove(deletedPostPos);
						}
						commentFound = true;
						break;
					}
				}
				if (!commentFound) {
					for (int deletedCommentPos = 0; deletedCommentPos < deletedCommentsList.size(); deletedCommentPos++) {
						DeletedComment deletedComment = deletedCommentsList.get(deletedCommentPos);
						if (deletedComment.searchComment(id) != -1) {
							deletedComment.removeCommentIDAt(deletedCommentPos);
							if (deletedComment.getCommentsList().size() == 0) {
								deletedCommentsList.remove(deletedCommentPos);
							}
							commentFound = true;
							break;
						}
					}
				}
				for (Account acc : accountsList) {
					if (oldEndorsements.size() != 0) {
						ArrayList<Endorsement> endorsements = new ArrayList<Endorsement>(acc.getEndorsements());
						//Looks through accounts again to find endorsements made on the post
						for (Endorsement endorse : endorsements) {
							//Checks if the ID is in the list of endorsement IDs to be deleted
							if (oldEndorsements.contains(endorse.getPostID())) {
								//Deletes endorsement object
								acc.getEndorsements().remove(endorse);
								//Removes from endorsements to be deleted list since it has been found
								oldEndorsements.remove((Integer) endorse.getPostID());
							}
						}
					}
					if (!commentFound) {
						//Looks for post or comment with the post reference ID
						int postPos2 = acc.searchPost(oldComment.getPostReferenceID());
						int commentPos2 = acc.searchComment(oldComment.getPostReferenceID());
						//Deletes comment ID from post
						if (postPos2 != -1) {
							acc.removeCommentIDinPostAt(postPos2, oldComment.getPostID());
							commentFound = true;
						} else if (commentPos2 != -1) {
							acc.removeCommentIDinCommentAt(commentPos2, oldComment.getPostID());
							commentFound = true;
						}
					}
					//Stops early when all endorsements are deleted and the ID has been removef from reference post
					if (oldEndorsements.size() == 0 && commentFound) {
						break;
					}
				}
				//Removes the comment object
				accountsList.get(accPos).removeCommentAt(commentPos);
				break;
			}
		}
	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		for (int pos = 0; pos < accountsList.size(); pos++) {
			//Formatted string for original post
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			for (int x = 0; x < postsList.size(); x++){
				if (postsList.get(x).getPostID() == id){
					return String.format("<pre>\nID: %d\nAccount: %s\nNo. endorsements: %s | No. comments: %s\n%s\n</pre>", id, accountsList.get(pos).getHandle(), accountsList.get(pos).getEndorsements().size(), accountsList.get(pos).getComments().size(), postsList.get(x).getMessage());
				}
			}
			//Formatted string for comment
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
			for (int x = 0; x < commentsList.size(); x++){ //for every comment in comments list
				if (commentsList.get(x).getPostID() == id){ //if the comment has the ID we are looking for
					return String.format("<pre>\nID: %d\nAccount: %s\nNo. endorsements: %s | No. comments: %s\n%s\n</pre>", id, accountsList.get(pos).getHandle(), commentsList.get(x).getEndorsementsList().size(), commentsList.get(x).getCommentsList().size(), commentsList.get(x).getMessage());
				}
			}
			//Formatted string for endorsement
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
	//Stores IDs which have been checked
	private ArrayList<Integer> checkedElements = new ArrayList<Integer>();
	@Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		StringBuilder string = new StringBuilder();
		string.append("<pre>\n");
		string.append(recursiveApproach(id));
		string.append("\n</pre>");
		indentationNumber = -1;
		checkedElements.clear();
		return string;
	}
	public StringBuilder recursiveApproach(int id){
		indentationNumber += 1;
		//Looks through all accounts
		for (int pos = 0; pos < accountsList.size(); pos++) {
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			//Looks through all original posts of the account
			for (int x = 0; x < postsList.size(); x++){
				if (postsList.get(x).getPostID() == id && !checkedElements.contains(id)){
					//Adds ID since it is being checked
					checkedElements.add(id);
					StringBuilder string = new StringBuilder();
					String tab = "\t";
					int changeableIndentationNumber = indentationNumber; //?????
					changeableIndentationNumber = indentationNumber; //????? why is this line needed?
					if (changeableIndentationNumber != 0){
						string.append("\n");
					}
					changeableIndentationNumber = indentationNumber; //?????
					while (changeableIndentationNumber > 1){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("ID:");
					string.append(Integer.toString(id) + "\n");
					changeableIndentationNumber = indentationNumber; 
					while (changeableIndentationNumber > 0){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					for (int a = 0; a < accountsList.size(); a++) {
						if (accountsList.get(a).getAccountID() == postsList.get(x).getAccountID()){
							changeableIndentationNumber = indentationNumber;
							while (changeableIndentationNumber > 1){
								string.append(tab);
								changeableIndentationNumber -= 1;
							}
							string.append("Account: " + accountsList.get(a).getHandle() + "\n");
						}
					}
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 0){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("No. endorsements: " + Integer.toString(postsList.get(x).getEndorsementsList().size()) + " | No. comments: " + Integer.toString(postsList.get(x).getCommentsList().size()) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 0){
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append(postsList.get(x).getMessage());

					for (int y = 0; y < postsList.get(x).getCommentsList().size(); y++){
						ArrayList<Integer> childElementsList = postsList.get(x).getCommentsList();
						for (int z = 0; z < childElementsList.size(); z++){
							changeableIndentationNumber = indentationNumber;
							if (changeableIndentationNumber != 0){
								string.append("\n");
							}
							changeableIndentationNumber = indentationNumber + 1;
							while (changeableIndentationNumber > 1){
								string.append(tab);
								changeableIndentationNumber -= 1;
							}
							changeableIndentationNumber = indentationNumber;
							if (changeableIndentationNumber != 0){
								string.append("|");
							}
							string.append(recursiveApproach(childElementsList.get(z)));
						}
					}
					indentationNumber -= 1;
					return string;
				}
			}
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
			for (int x = 0; x < commentsList.size(); x++){ //for every comment in comments list
				if (commentsList.get(x).getPostID() == id && !checkedElements.contains(id)){ //if the comment has the ID we are looking for
					checkedElements.add(id);
					StringBuilder string = new StringBuilder();
					String tab = "\t";
					int changeableIndentationNumber = indentationNumber;
					if (indentationNumber != 0){
						changeableIndentationNumber = indentationNumber;
						if (changeableIndentationNumber != 0){
							string.append("\n");
						}
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1){
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append("|" + tab + "> ID:");
						string.append(Integer.toString(id) + "\n");
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1){
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						for (int a = 0; a < accountsList.size(); a++) {
							if (accountsList.get(a).getAccountID() == commentsList.get(x).getAccountID()){
								changeableIndentationNumber = indentationNumber;
								while (changeableIndentationNumber > 1){
									string.append(tab);
									changeableIndentationNumber -= 1;
								}
								changeableIndentationNumber = indentationNumber;
								if (changeableIndentationNumber == 1){
									string.append(tab);
								}
								string.append("Account: " + accountsList.get(a).getHandle() + "\n");
							}
						}
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1){
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append(tab + "No. endorsements: " + Integer.toString(commentsList.get(x).getEndorsementsList().size()) + " | No. comments: " + Integer.toString(commentsList.get(x).getCommentsList().size()) + "\n");
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1){
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append(tab + commentsList.get(x).getMessage());
					}
					if (indentationNumber == 0){
						changeableIndentationNumber = indentationNumber;
						if (changeableIndentationNumber != 0){
							string.append("\n");
						}
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1){
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append("ID:");
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
						string.append("No. endorsements: " + Integer.toString(commentsList.get(x).getEndorsementsList().size()) + " | No. comments: " + Integer.toString(commentsList.get(x).getCommentsList().size()) + "\n");
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1){
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append(commentsList.get(x).getMessage());
					}

					for (int y = 0; y < commentsList.get(x).getCommentsList().size(); y++){
						ArrayList<Integer> childElementsList = commentsList.get(x).getCommentsList();
						for (int z = 0; z < childElementsList.size(); z++){
							changeableIndentationNumber = indentationNumber;
							if (changeableIndentationNumber != 0){
								string.append("\n");
							}
							changeableIndentationNumber = indentationNumber + 1;
							while (changeableIndentationNumber > 1){
								string.append(tab);
								changeableIndentationNumber -= 1;
							}
							changeableIndentationNumber = indentationNumber;
							if (changeableIndentationNumber != 0){
								string.append("|");
							}
							string.append(recursiveApproach(childElementsList.get(z)));
						}
					}
					indentationNumber -= 1;
					return string;
				}
			}
		}
		StringBuilder string = new StringBuilder();
		indentationNumber -= 1;
		return string;
	}

	@Override
	public int getNumberOfAccounts() {
		return accountsList.size();
	}

	@Override
	public int getTotalOriginalPosts() {
		int total = 0;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			//Increments the size of original post list of the account
			total += accountsList.get(pos).getOriginalPosts().size();
		}
		return total;
	}

	@Override
	public int getTotalEndorsmentPosts() {
		int total = 0;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			//Increments the size of endorsement list of the account
			total += accountsList.get(pos).getEndorsements().size();
		}
		return total;
	}

	@Override
	public int getTotalCommentPosts() {
		int total = 0;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			//Increments the size of comment list of the account
			total += accountsList.get(pos).getComments().size();
		}
		return total;
	}

	@Override
	public int getMostEndorsedPost() { //cannot check yet, endorsements not working, may have to replace the "postsList.get(x).getEndorsementsList().size()"/similar lines with something more if not done as needed
		//Stores the number of endorsements of the currently most endorsed post
		int largestNumber = 0;
		//Stores the ID of the most endorsed post
		int mostEndorsedPostID = -1;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			//Looks through all original posts
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			for (int x = 0; x < postsList.size(); x++){
				//Compares the number endorsements with currently largest number
				if (postsList.get(x).getEndorsementsList().size() > largestNumber){
					largestNumber = postsList.get(x).getEndorsementsList().size();
					mostEndorsedPostID = postsList.get(x).getPostID();
				}
			}
			//Looks through all comments
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
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
	public int getMostEndorsedAccount() { //cannot check yet, endorsements not working
		int maxNumberOfEndorsements = 0;
		int mostEndorsedID = -1;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			//Stores the total number of endorsements for an account
			int numberOfEndorsements = 0;
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			//Adds the number of endorsements for each post and comment 
			for (int x = 0; x < postsList.size(); x++){
				numberOfEndorsements += postsList.get(x).getEndorsementsList().size();
			}
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
			for (int x = 0; x < commentsList.size(); x++){
				numberOfEndorsements += commentsList.get(x).getEndorsementsList().size();
			}
			//Compares the number endorsements with currently largest number
			if (numberOfEndorsements > maxNumberOfEndorsements){
				maxNumberOfEndorsements = numberOfEndorsements;
				mostEndorsedID = accountsList.get(pos).getAccountID();
			}
		}
		return mostEndorsedID;
	}

	@Override
	public void erasePlatform() {
		//Clears all the lists storing accounts (thus clearing all its posts, comments and endorsements), also clear deleted comments and posts to completely erase the platform.
		accountsList.clear();
        	deletedCommentsList.clear();
        	deletedPostsList.clear();
        	//Also resets/clears other counters (which should already be reset, but just to ensure.)
        	checkedElements.clear();
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