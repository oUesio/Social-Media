package socialmedia;

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Dictionary;
import java.util.Hashtable;

public class SocialMedia implements SocialMediaPlatform {
	private ArrayList<Account> accountsList = new ArrayList<Account>();
	private ArrayList<DeletedPost> deletedPostsList = new ArrayList<DeletedPost>();
	private ArrayList<DeletedComment> deletedCommentsList = new ArrayList<DeletedComment>();

	@Override
	public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
		// First checks if the newHandle exists in the system, if so, throw IllegalHandleException
		for (Account acc : accountsList) {
			if (acc.getHandle() == handle) {
				throw new IllegalHandleException();
			}
		}
		// Check if the new handle is legal within the system spec
		if (handle == "" || handle == null || handle.length() > 30 || handle.contains(" ")) {
			throw new InvalidHandleException();
		}
		// Creates new account object, with only a handle, which is added to accountsList
		accountsList.add(new Account(handle));
		return accountsList.get(accountsList.size() - 1).getAccountID();

	}

	@Override
	public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		// First checks if the newHandle exists in the system, if so, throw IllegalHandleException
		for (Account acc : accountsList) {
			if (acc.getHandle() == handle) {
				throw new IllegalHandleException();
			}
		}
		// Check if the new handle is legal within the system spec
		if (handle == "" || handle == null || handle.length() > 30 || handle.contains(" ")) {
			throw new InvalidHandleException();
		}
		// Creates new account object, with a handle and a description, which is added to accountsList
		accountsList.add(new Account(handle, description));
		return accountsList.get(accountsList.size() - 1).getAccountID();
	}

	@Override
	public void removeAccount(int id) throws AccountIDNotRecognisedException {
		for (int accPos = 0; accPos < accountsList.size(); accPos++) {
			// Finds the account with the id
			if (accountsList.get(accPos).getAccountID() == id) {
				// Deletes all posts, comments and endorsements of the account
				try {
					ArrayList<Post> tempPosts = new ArrayList<Post>(accountsList.get(accPos).getOriginalPosts());
					for (Post post : tempPosts) {
						deletePost(post.getPostID());
					}
					ArrayList<Comment> tempComments = new ArrayList<Comment>(accountsList.get(accPos).getComments());
					for (Comment comment : tempComments) {
						deletePost(comment.getPostID());
					}
					ArrayList<Endorsement> tempEndorsements = new ArrayList<Endorsement>(
							accountsList.get(accPos).getEndorsements());
					for (Endorsement endorse : tempEndorsements) {
						deletePost(endorse.getPostID());
					}
				} catch (PostIDNotRecognisedException e) {
					continue;
				}
				// Deletes the account with the id
				accountsList.remove(accountsList.get(accPos));
				return;
			}
		}
		// If it has looped through here and not returned, then the id must not be in the system, so throws id not recognised.
		throw new AccountIDNotRecognisedException();
	}

	@Override
	public void removeAccount(String handle) throws HandleNotRecognisedException {
		for (int accPos = 0; accPos < accountsList.size(); accPos++) {
			// Finds the account with the handle
			if (accountsList.get(accPos).getHandle() == handle) {
				// Deletes all posts, comments and endorsements of the account
				try {
					ArrayList<Post> tempPosts = new ArrayList<Post>(accountsList.get(accPos).getOriginalPosts());
					for (Post post : tempPosts) {
						deletePost(post.getPostID());
					}
					ArrayList<Comment> tempComments = new ArrayList<Comment>(accountsList.get(accPos).getComments());
					for (Comment comment : tempComments) {
						deletePost(comment.getPostID());
					}
					ArrayList<Endorsement> tempEndorsements = new ArrayList<Endorsement>(
							accountsList.get(accPos).getEndorsements());
					for (Endorsement endorse : tempEndorsements) {
						deletePost(endorse.getPostID());
					}
				} catch (PostIDNotRecognisedException e) {
					continue;
				}
				// Deletes the account with the handle
				accountsList.remove(accountsList.get(accPos));
				return;
			}
		}
		// If it has looped through here and not returned, then the handle must not be in the system, so throws handle not recognised.
		throw new HandleNotRecognisedException();
	}

	@Override
	public void changeAccountHandle(String oldHandle, String newHandle)
			throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
		// First checks if the newHandle exists in the system, if so, throw IllegalHandleException
		for (Account acc : accountsList) {
			if (acc.getHandle() == newHandle) {
				throw new IllegalHandleException();
			}
		}
		// Check if the new handle is legal within the system spec
		if (newHandle == "" || newHandle == null || newHandle.length() > 30 || newHandle.contains(" ")) {
			throw new InvalidHandleException();
		}
		for (int pos = 0; pos < accountsList.size(); pos++) {
			// Finds the account with the oldHandle
			if (accountsList.get(pos).getHandle() == oldHandle) {
				// Changes the handle
				accountsList.get(pos).setHandle(newHandle);
				return;
			}
		}
		// If it has looped through here and not returned, then the oldHandle must not be in the system, so throws handle not recognised.
		throw new HandleNotRecognisedException();
	}

	@Override
	public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
		for (int pos = 0; pos < accountsList.size(); pos++) {
			// Finds the account with the handle
			if (accountsList.get(pos).getHandle() == handle) {
				// Updates the description
				accountsList.get(pos).setDescription(description);
				return;
			}
		}
		// If it has looped through here and not returned, then the handle must not be in the system, so throws handle not recognised.
		throw new HandleNotRecognisedException();
	}

	@Override
	public String showAccount(String handle) throws HandleNotRecognisedException {
		int totalEndorsements = 0;
		for (Account acc : accountsList) {
			// Finds accounts with the handle
			if (acc.getHandle() == handle) {
				ArrayList<Post> postsList = acc.getOriginalPosts();
				ArrayList<Comment> commentsList = acc.getComments();
				// Finds sum of endorsements received
				for (int x = 0; x < postsList.size(); x++) {
					totalEndorsements += postsList.get(x).getEndorsementsList().size();
				}
				for (int x = 0; x < commentsList.size(); x++) {
					totalEndorsements += commentsList.get(x).getEndorsementsList().size();
				}
				return String.format(
						"<pre>\nID: %d\nHandle: %s\nDescription: %s\nPost count: %d\nEndorse count: %s\n</pre>",
						acc.getAccountID(), acc.getHandle(), acc.getDescription(),
						(acc.getComments().size() + acc.getEndorsements().size() + acc.getOriginalPosts().size()),
						totalEndorsements);
			}
		}
		// If it has looped through here and not returned, then the handle must not be in the system, so throws handle not recognised.
		throw new HandleNotRecognisedException();
	}

	@Override
	public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
		// Checks the message is inside the character limit
		if (message.length() <= 100 && message != "" && message != null) {
			for (int pos = 0; pos < accountsList.size(); pos++) {
				// Finds account with the handle
				if (accountsList.get(pos).getHandle() == handle) {
					return accountsList.get(pos).createOriginalPost(message);
				}
			}
			throw new HandleNotRecognisedException();
		} else {
			throw new InvalidPostException();
		}
	}

	@Override
	public int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
		boolean handleExists = false;
		boolean notActionable = false;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			// Finds account with the handle
			if (accountsList.get(pos).getHandle() == handle) {
				handleExists = true;
				int endorseID = -1;
				for (Account acc : accountsList) {
					// Adds endorsement ID to post when the ID of the post being endorsed is found in an account
					int foundPostIDPos = acc.searchPost(id);
					if (foundPostIDPos != -1) {
						endorseID = accountsList.get(pos).createEndorsement("EP@" + acc.getHandle() + ": "
								+ acc.getOriginalPosts().get(foundPostIDPos).getMessage(), id);
						acc.addEndorseIDtoPostAt(foundPostIDPos, endorseID);
						break;
					}
					int foundCommentIDPos = acc.searchComment(id);
					if (foundCommentIDPos != -1) {
						endorseID = accountsList.get(pos).createEndorsement(
								"EP@" + acc.getHandle() + ": " + acc.getComments().get(foundCommentIDPos).getMessage(),
								id);
						acc.addEndorseIDtoCommentAt(foundCommentIDPos, endorseID);
						break;
					}
					int foundEndorseIDPos = acc.searchEndorsement(id);
					if (foundEndorseIDPos != -1) {
						notActionable = true;
						break;
					}
				}
				if (endorseID != -1) {
					return endorseID;
				}
			}
		}
		if (!handleExists) {
			throw new HandleNotRecognisedException();
		} else if (notActionable) {
			throw new NotActionablePostException();
		}
		throw new PostIDNotRecognisedException();
	}

	@Override
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
		// Checks the message is inside the character limit
		if (message.length() <= 100 && message != "" && message != null) {
			boolean handleExists = false;
			boolean postIDExists = false;
			boolean notActionable = false;
			for (int pos = 0; pos < accountsList.size(); pos++) {
				if (accountsList.get(pos).getHandle() == handle) {
					handleExists = true;
					int commentID = -1;
					for (Account acc : accountsList) {
						// Adds comment ID to post when the ID of the post being commented is found in an account
						int foundPostIDPos = acc.searchPost(id);
						if (foundPostIDPos != -1) {
							postIDExists = true;
							commentID = accountsList.get(pos).createComment(message, id);
							acc.addCommentIDtoPostAt(foundPostIDPos, commentID);
							break;
						}
						int foundCommentIDPos = acc.searchComment(id);
						if (foundCommentIDPos != -1) {
							postIDExists = true;
							commentID = accountsList.get(pos).createComment(message, id);
							acc.addCommentIDtoCommentAt(foundCommentIDPos, commentID);
							break;
						}
						int foundEndorseIDPos = acc.searchEndorsement(id);
						if (foundEndorseIDPos != -1) {
							postIDExists = true;
							notActionable = true;
							break;
						}
					}
					if (commentID != -1) {
						return commentID;
					}
				}
			}
			if (!handleExists) {
				throw new HandleNotRecognisedException();
			} else if (notActionable) {
				throw new NotActionablePostException();
			} else if (!postIDExists) {
				throw new PostIDNotRecognisedException();
			}
		}
		throw new InvalidPostException();
	}

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
		int postPos = -1;
		int endorsePos = -1;
		int commentPos = -1;
		int accPos = 0;
		while (accPos < accountsList.size()) {
			// Looks for post, endorsement or comment with the ID
			postPos = accountsList.get(accPos).searchPost(id);
			endorsePos = accountsList.get(accPos).searchEndorsement(id);
			commentPos = accountsList.get(accPos).searchComment(id);
			// Stops searching when object has been found
			if (postPos != -1 || endorsePos != -1 || commentPos != -1) {
				break;
			}
			accPos++;
		}
		// Throws exception if the object doesn't exist
		if (postPos == -1 && endorsePos == -1 && commentPos == -1) {
			throw new PostIDNotRecognisedException();
		}
		// ID found in original posts
		if (postPos != -1) {
			Post oldPost = accountsList.get(accPos).getOriginalPosts().get(postPos);
			// Placeholder creation, doesn't create if there are no comments
			if (oldPost.getCommentsList().size() != 0) {
				deletedPostsList.add(new DeletedPost(oldPost.getPostID(), oldPost.getCommentsList()));
			}
			ArrayList<Integer> oldEndorsements = oldPost.getEndorsementsList();
			// Looks through accounts again to find endorsements made on the post
			for (Account acc : accountsList) {
				ArrayList<Endorsement> endorsements = new ArrayList<Endorsement>(acc.getEndorsements());
				for (Endorsement endorse : endorsements) {
					// Checks if the ID is in the list of endorsement IDs to be deleted
					if (oldEndorsements.contains(endorse.getPostID())) {
						// Deletes endorsement object
						acc.getEndorsements().remove(endorse);
						// Removes from endorsements to be deleted list since it has been found
						oldEndorsements.remove((Integer) endorse.getPostID());
					}
				}
				// Stops early when all endorsements have been found and deleted
				if (oldEndorsements.size() == 0) {
					break;
				}
			}
			// Removes the post object
			accountsList.get(accPos).removePostAt(postPos);
		} else if (endorsePos != -1) { // ID found in endorsements
			Endorsement oldEndorsement = accountsList.get(accPos).getEndorsements().get(endorsePos);
			for (Account acc : accountsList) {
				// Looks for post or comment with the post reference ID
				int postPos2 = acc.searchPost(oldEndorsement.getPostReferenceID());
				int commentPos2 = acc.searchComment(oldEndorsement.getPostReferenceID());
				// Deletes endorsement ID from post
				if (postPos2 != -1) {
					acc.removeEndorseIDinPostAt(postPos2, oldEndorsement.getPostID());
					break;
				} else if (commentPos2 != -1) {
					acc.removeEndorseIDinCommentAt(commentPos2, oldEndorsement.getPostID());
					break;
				}
			}
			// Removes the endorsement object
			accountsList.get(accPos).removeEndorsementAt(endorsePos);
		} else if (commentPos != -1) { // ID found in comments
			Comment oldComment = accountsList.get(accPos).getComments().get(commentPos);
			// If true, deletes the id from reference post
			boolean emptyComments = true;
			boolean commentFound = false;
			// Placeholder creation, doesn't create if there are no comments
			if (oldComment.getCommentsList().size() != 0) {
				emptyComments = false;
				deletedCommentsList.add(new DeletedComment(oldComment.getPostID(), oldComment.getCommentsList(),
						oldComment.getPostReferenceID())); // placeholder post
			}
			ArrayList<Integer> oldEndorsements = oldComment.getEndorsementsList();
			// Checks the placeholder posts for comment ID
			if (emptyComments) {
				for (int deletedPostPos = 0; deletedPostPos < deletedPostsList.size(); deletedPostPos++) {
					DeletedPost deletedPost = deletedPostsList.get(deletedPostPos);
					if (deletedPost.searchComment(id) != -1) {
						// Removes ID from the reference post
						deletedPost.removeCommentIDAt(deletedPost.searchComment(id));
						// Deletes the placeholder if it doesn't contain any comments
						if (deletedPost.getCommentsList().size() == 0) {
							deletedPostsList.remove(deletedPostPos);
						}
						commentFound = true;
						break;
					}
				}
				if (!commentFound) {
					for (int deletedCommentPos = 0; deletedCommentPos < deletedCommentsList
							.size(); deletedCommentPos++) {
						// Placeholder storing the id for the comment to be deleted
						DeletedComment deletedComment = deletedCommentsList.get(deletedCommentPos);
						if (deletedComment.searchComment(id) != -1) {
							deletedComment.removeCommentIDAt(deletedComment.searchComment(id));
							// Deletes newly empty DeletedComment objects and their ID
							int change = 0;
							while (deletedCommentsList.size() != 0) {
								ArrayList<DeletedComment> tempDeletedComments = new ArrayList<DeletedComment>(
										deletedCommentsList);
								for (DeletedComment del : tempDeletedComments) {
									if (del.getCommentsList().size() == 0) {
										boolean deletedCommentFound = false;
										// Looks in accounts
										for (Account acc : accountsList) {
											boolean found = false;
											for (Post post : acc.getOriginalPosts()) {
												if (post.getCommentsList().contains(del.getPostID())) {
													post.getCommentsList().remove((Integer) del.getPostID());
													found = true;
													deletedCommentFound = true;
													break;
												}
											}
											for (Comment comment : acc.getComments()) {
												if (comment.getCommentsList().contains(del.getPostID())) {
													comment.getCommentsList().remove((Integer) del.getPostID());
													found = true;
													deletedCommentFound = true;
													break;
												}
											}
											if (found) {
												break;
											}
										}
										// Looks in deletedposts
										if (!deletedCommentFound) {
											for (int deletedPostPos = 0; deletedPostPos < deletedPostsList
													.size(); deletedPostPos++) {
												DeletedPost deletedPost = deletedPostsList.get(deletedPostPos);
												if (deletedPost.searchComment(del.getPostID()) != -1) {
													// Removes ID from the reference post
													deletedPost.removeCommentIDAt(
															deletedPost.searchComment(del.getPostID()));
													// Deletes the placeholder if it doesn't contain any comments
													if (deletedPost.getCommentsList().size() == 0) {
														deletedPostsList.remove(deletedPostPos);
													}
													deletedCommentFound = true;
													break;
												}
											}
										}
										// Looks in deletedcomments
										if (!deletedCommentFound) {
											for (int deletedCommentPos2 = 0; deletedCommentPos2 < deletedCommentsList
													.size(); deletedCommentPos2++) {
												DeletedComment deletedComment2 = deletedCommentsList
														.get(deletedCommentPos2);
												if (deletedComment2.searchComment(del.getPostID()) != -1) {
													// Removes ID from the reference post
													deletedComment2.removeCommentIDAt(
															deletedComment2.searchComment(del.getPostID()));
													change++;
													break;
												}
											}
										}
										deletedCommentsList.remove(del);
									}
								}
								if (change == 0) {
									break;
								}
								change = 0;
							}
							commentFound = true;
							break;
						}
					}
				}
			}
			for (Account acc : accountsList) {
				if (oldEndorsements.size() != 0) {
					ArrayList<Endorsement> endorsements = new ArrayList<Endorsement>(acc.getEndorsements());
					// Looks through accounts again to find endorsements made on the post
					for (Endorsement endorse : endorsements) {
						// Checks if the ID is in the list of endorsement IDs to be deleted
						if (oldEndorsements.contains(endorse.getPostID())) {
							// Deletes endorsement object
							acc.getEndorsements().remove(endorse);
							// Removes from endorsements to be deleted list since it has been found
							oldEndorsements.remove((Integer) endorse.getPostID());
						}
					}
				}
				if (!commentFound && emptyComments) {
					// Looks for post or comment with the post reference ID
					int postPos2 = acc.searchPost(oldComment.getPostReferenceID());
					int commentPos2 = acc.searchComment(oldComment.getPostReferenceID());
					// Deletes comment ID from post
					if (postPos2 != -1) {
						acc.removeCommentIDinPostAt(postPos2, oldComment.getPostID());
						commentFound = true;
					} else if (commentPos2 != -1) {
						acc.removeCommentIDinCommentAt(commentPos2, oldComment.getPostID());
						commentFound = true;
					}
				}
				// Stops early when all endorsements are deleted and the ID has been removed from reference post
				if (oldEndorsements.size() == 0 && commentFound) {
					break;
				}
			}
			// Removes the comment object
			accountsList.get(accPos).removeCommentAt(commentPos);
		}
	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		for (int pos = 0; pos < accountsList.size(); pos++) {
			// Formatted string for original post
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			for (int x = 0; x < postsList.size(); x++) {
				if (postsList.get(x).getPostID() == id) {
					return String.format(
							"<pre>\nID: %d\nAccount: %s\nNo. endorsements: %s | No. comments: %s\n%s\n</pre>", id,
							accountsList.get(pos).getHandle(), accountsList.get(pos).getEndorsements().size(),
							accountsList.get(pos).getComments().size(), postsList.get(x).getMessage());
				}
			}
			// Formatted string for comment
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
			for (int x = 0; x < commentsList.size(); x++) { // for every comment in comments list
				if (commentsList.get(x).getPostID() == id) { // if the comment has the ID we are looking for
					return String.format(
							"<pre>\nID: %d\nAccount: %s\nNo. endorsements: %s | No. comments: %s\n%s\n</pre>", id,
							accountsList.get(pos).getHandle(), commentsList.get(x).getEndorsementsList().size(),
							commentsList.get(x).getCommentsList().size(), commentsList.get(x).getMessage());
				}
			}
			// Formatted string for endorsement
			ArrayList<Endorsement> endorsementsList = accountsList.get(pos).getEndorsements();
			for (int x = 0; x < endorsementsList.size(); x++) {
				if (endorsementsList.get(x).getPostID() == id) {
					return String.format(
							"<pre>\nID: %d\nAccount: %s\nNo. endorsements: N/A | No. comments: N/A\n%s\n</pre>", id,
							accountsList.get(pos).getHandle(), endorsementsList.get(x).getMessage());
				}
			}
		}
		// Formatted string for deleted comments
		for (int x = 0; x < deletedCommentsList.size(); x++) {
			if (deletedCommentsList.get(x).getPostID() == id) {
				return String.format(
						"<pre>\nID: %d\nAccount: N/A\nNo. endorsements: N/A | No. comments: %s\n%s\n</pre>", id,
						deletedCommentsList.get(x).getCommentsList().size(), deletedCommentsList.get(x).getMessage());
			}
		}
		// Formatted string for deleted posts
		for (int x = 0; x < deletedPostsList.size(); x++) {
			if (deletedPostsList.get(x).getPostID() == id) {
				return String.format(
						"<pre>\nID: %d\nAccount: N/A\nNo. endorsements: N/A | No. comments: %s\n%s\n</pre>", id,
						deletedPostsList.get(x).getCommentsList().size(), deletedPostsList.get(x).getMessage());
			}
		}
		// Now it has looped through all the lists of each account and deleted lists, if
		// not found, we now the post does not exist, sop throw post ID not recognised
		// exception
		throw new PostIDNotRecognisedException();
	}

	private int indentationNumber = -1;
	// Stores IDs which have been checked
	private ArrayList<Integer> checkedElements = new ArrayList<Integer>();

	@Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		// The following 3 for loops check each list to see if the post is non
		// actionable, creates and sets a flag to false if actionable, false if not
		// actionable. They also set a flag postIDRecognised to true if the post ID is
		// found in a list.
		Boolean notActionable = false;
		Boolean postIDRecognised = false;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			for (int x = 0; x < postsList.size(); x++) {
				if (postsList.get(x).getPostID() == id) {
					notActionable = false;
					postIDRecognised = true;
				}
			}
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
			for (int x = 0; x < commentsList.size(); x++) {
				if (commentsList.get(x).getPostID() == id) {
					notActionable = false;
					postIDRecognised = true;
				}
			}
			ArrayList<Endorsement> endorsementsList = accountsList.get(pos).getEndorsements();
			for (int x = 0; x < endorsementsList.size(); x++) {
				if (endorsementsList.get(x).getPostID() == id) {
					notActionable = true;
					postIDRecognised = true;
				}
			}
		}
		for (int x = 0; x < deletedPostsList.size(); x++) {
			if (deletedPostsList.get(x).getPostID() == id) {
				notActionable = false;
				postIDRecognised = true;
			}
		}
		for (int x = 0; x < deletedCommentsList.size(); x++) {
			if (deletedCommentsList.get(x).getPostID() == id) {
				notActionable = false;
				postIDRecognised = true;
			}
		}
		// I can then do 2 simple if statements to throw the correct errors should the
		// booleans be fulfilled
		if (!postIDRecognised) {
			throw new PostIDNotRecognisedException();
		}
		if (notActionable) {
			throw new NotActionablePostException();
		}
		StringBuilder string = new StringBuilder();
		string.append("<pre>\n");
		string.append(recursiveApproach(id));
		string.append("\n</pre>");
		indentationNumber = -1;
		checkedElements.clear();
		return string;
	}

	public StringBuilder recursiveApproach(int id) {
		indentationNumber += 1;
		// Looks through all accounts
		for (int pos = 0; pos < accountsList.size(); pos++) {
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			// Looks through all original posts of the account
			for (int x = 0; x < postsList.size(); x++) {
				if (postsList.get(x).getPostID() == id && !checkedElements.contains(id)) {
					// Adds ID since it is being checked
					checkedElements.add(id);
					StringBuilder string = new StringBuilder();
					String tab = "\t";
					int changeableIndentationNumber = indentationNumber;
					changeableIndentationNumber = indentationNumber;
					if (changeableIndentationNumber != 0) {
						string.append("\n");
					}
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("ID:");
					string.append(Integer.toString(id) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 0) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					for (int a = 0; a < accountsList.size(); a++) {
						if (accountsList.get(a).getAccountID() == postsList.get(x).getAccountID()) {
							changeableIndentationNumber = indentationNumber;
							while (changeableIndentationNumber > 1) {
								string.append(tab);
								changeableIndentationNumber -= 1;
							}
							string.append("Account: " + accountsList.get(a).getHandle() + "\n");
						}
					}
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 0) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("No. endorsements: " + Integer.toString(postsList.get(x).getEndorsementsList().size())
							+ " | No. comments: " + Integer.toString(postsList.get(x).getCommentsList().size()) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 0) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append(postsList.get(x).getMessage());

					for (int y = 0; y < postsList.get(x).getCommentsList().size(); y++) {
						ArrayList<Integer> childElementsList = postsList.get(x).getCommentsList();
						for (int z = 0; z < childElementsList.size(); z++) {
							changeableIndentationNumber = indentationNumber;
							if (changeableIndentationNumber != 0) {
								string.append("\n");
							}
							changeableIndentationNumber = indentationNumber + 1;
							while (changeableIndentationNumber > 1) {
								string.append(tab);
								changeableIndentationNumber -= 1;
							}
							changeableIndentationNumber = indentationNumber;
							if (changeableIndentationNumber != 0) {
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
			for (int x = 0; x < commentsList.size(); x++) {
				if (commentsList.get(x).getPostID() == id && !checkedElements.contains(id)) {																	// ID we are looking for
					checkedElements.add(id);
					StringBuilder string = new StringBuilder();
					String tab = "\t";
					int changeableIndentationNumber = indentationNumber;
					if (indentationNumber != 0) {
						changeableIndentationNumber = indentationNumber;
						if (changeableIndentationNumber != 0) {
							string.append("\n");
						}
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1) {
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append("|" + tab + "> ID:");
						string.append(Integer.toString(id) + "\n");
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1) {
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						for (int a = 0; a < accountsList.size(); a++) {
							if (accountsList.get(a).getAccountID() == commentsList.get(x).getAccountID()) {
								changeableIndentationNumber = indentationNumber;
								while (changeableIndentationNumber > 1) {
									string.append(tab);
									changeableIndentationNumber -= 1;
								}
								changeableIndentationNumber = indentationNumber;
								if (changeableIndentationNumber == 1) {
									string.append(tab);
								}
								string.append("Account: " + accountsList.get(a).getHandle() + "\n");
							}
						}
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1) {
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append(tab + "No. endorsements: "
								+ Integer.toString(commentsList.get(x).getEndorsementsList().size())
								+ " | No. comments: " + Integer.toString(commentsList.get(x).getCommentsList().size())
								+ "\n");
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1) {
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append(tab + commentsList.get(x).getMessage());
					}
					if (indentationNumber == 0) {
						changeableIndentationNumber = indentationNumber;
						if (changeableIndentationNumber != 0) {
							string.append("\n");
						}
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1) {
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append("ID:");
						string.append(Integer.toString(id) + "\n");
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1) {
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append("Account: " + Integer.toString(commentsList.get(x).getAccountID()) + "\n");
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1) {
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append("No. endorsements: "
								+ Integer.toString(commentsList.get(x).getEndorsementsList().size())
								+ " | No. comments: " + Integer.toString(commentsList.get(x).getCommentsList().size())
								+ "\n");
						changeableIndentationNumber = indentationNumber;
						while (changeableIndentationNumber > 1) {
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						string.append(commentsList.get(x).getMessage());
					}

					for (int y = 0; y < commentsList.get(x).getCommentsList().size(); y++) {
						ArrayList<Integer> childElementsList = commentsList.get(x).getCommentsList();
						for (int z = 0; z < childElementsList.size(); z++) {
							changeableIndentationNumber = indentationNumber;
							if (changeableIndentationNumber != 0) {
								string.append("\n");
							}
							changeableIndentationNumber = indentationNumber + 1;
							while (changeableIndentationNumber > 1) {
								string.append(tab);
								changeableIndentationNumber -= 1;
							}
							changeableIndentationNumber = indentationNumber;
							if (changeableIndentationNumber != 0) {
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
		ArrayList<DeletedComment> deleteCommentsListObj = deletedCommentsList;
		for (int x = 0; x < deleteCommentsListObj.size(); x++) {
			if (deleteCommentsListObj.get(x).getPostID() == id && !checkedElements.contains(id)) {																				// looking for
				checkedElements.add(id);
				StringBuilder string = new StringBuilder();
				String tab = "\t";
				int changeableIndentationNumber = indentationNumber;
				if (indentationNumber != 0) {
					changeableIndentationNumber = indentationNumber;
					if (changeableIndentationNumber != 0) {
						string.append("\n");
					}
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("|" + tab + "> ID:");
					string.append(Integer.toString(id) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					changeableIndentationNumber = indentationNumber;
					if (changeableIndentationNumber == 1) {
						string.append(tab);
					}
					string.append("Account: N/A \n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append(tab + "No. endorsements: N/A | No. comments: "
							+ Integer.toString(deleteCommentsListObj.get(x).getCommentsList().size()) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append(tab + deleteCommentsListObj.get(x).getMessage());
				}
				if (indentationNumber == 0) {
					changeableIndentationNumber = indentationNumber;
					if (changeableIndentationNumber != 0) {
						string.append("\n");
					}
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("ID:");
					string.append(Integer.toString(id) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("Account: N/A \n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("No. endorsements: N/A | No. comments: "
							+ Integer.toString(deleteCommentsListObj.get(x).getCommentsList().size()) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append(deleteCommentsListObj.get(x).getMessage());
				}

				for (int y = 0; y < deleteCommentsListObj.get(x).getCommentsList().size(); y++) {
					ArrayList<Integer> childElementsList = deleteCommentsListObj.get(x).getCommentsList();
					for (int z = 0; z < childElementsList.size(); z++) {
						changeableIndentationNumber = indentationNumber;
						if (changeableIndentationNumber != 0) {
							string.append("\n");
						}
						changeableIndentationNumber = indentationNumber + 1;
						while (changeableIndentationNumber > 1) {
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						changeableIndentationNumber = indentationNumber;
						if (changeableIndentationNumber != 0) {
							string.append("|");
						}
						string.append(recursiveApproach(childElementsList.get(z)));
					}
				}
				indentationNumber -= 1;
				return string;
			}
		}
		ArrayList<DeletedPost> deletePostsListObj = deletedPostsList;
		for (int x = 0; x < deletePostsListObj.size(); x++) {
			if (deletePostsListObj.get(x).getPostID() == id && !checkedElements.contains(id)) {																			// for
				checkedElements.add(id);
				StringBuilder string = new StringBuilder();
				String tab = "\t";
				int changeableIndentationNumber = indentationNumber;
				if (indentationNumber != 0) {
					changeableIndentationNumber = indentationNumber;
					if (changeableIndentationNumber != 0) {
						string.append("\n");
					}
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("|" + tab + "> ID:");
					string.append(Integer.toString(id) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					changeableIndentationNumber = indentationNumber;
					if (changeableIndentationNumber == 1) {
						string.append(tab);
					}
					string.append("Account: N/A \n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append(tab + "No. endorsements: N/A | No. comments: "
							+ Integer.toString(deletePostsListObj.get(x).getCommentsList().size()) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append(tab + deletePostsListObj.get(x).getMessage());
				}
				if (indentationNumber == 0) {
					changeableIndentationNumber = indentationNumber;
					if (changeableIndentationNumber != 0) {
						string.append("\n");
					}
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("ID:");
					string.append(Integer.toString(id) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("Account: N/A \n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append("No. endorsements: N/A | No. comments: "
							+ Integer.toString(deletePostsListObj.get(x).getCommentsList().size()) + "\n");
					changeableIndentationNumber = indentationNumber;
					while (changeableIndentationNumber > 1) {
						string.append(tab);
						changeableIndentationNumber -= 1;
					}
					string.append(deletePostsListObj.get(x).getMessage());
				}

				for (int y = 0; y < deletePostsListObj.get(x).getCommentsList().size(); y++) {
					ArrayList<Integer> childElementsList = deletePostsListObj.get(x).getCommentsList();
					for (int z = 0; z < childElementsList.size(); z++) {
						changeableIndentationNumber = indentationNumber;
						if (changeableIndentationNumber != 0) {
							string.append("\n");
						}
						changeableIndentationNumber = indentationNumber + 1;
						while (changeableIndentationNumber > 1) {
							string.append(tab);
							changeableIndentationNumber -= 1;
						}
						changeableIndentationNumber = indentationNumber;
						if (changeableIndentationNumber != 0) {
							string.append("|");
						}
						string.append(recursiveApproach(childElementsList.get(z)));
					}
				}
				indentationNumber -= 1;
				return string;
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
			// Increments the size of original post list of the account
			total += accountsList.get(pos).getOriginalPosts().size();
		}
		return total;
	}

	@Override
	public int getTotalEndorsmentPosts() {
		int total = 0;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			// Increments the size of endorsement list of the account
			total += accountsList.get(pos).getEndorsements().size();
		}
		return total;
	}

	@Override
	public int getTotalCommentPosts() {
		int total = 0;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			// Increments the size of comment list of the account
			total += accountsList.get(pos).getComments().size();
		}
		return total;
	}

	@Override
	public int getMostEndorsedPost() {
		// Stores the number of endorsements of the currently most endorsed post
		int largestNumber = -1;
		// Stores the ID of the most endorsed post
		int mostEndorsedPostID = -1;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			// Looks through all original posts
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			for (int x = 0; x < postsList.size(); x++) {
				// Compares the number endorsements with currently largest number
				if (postsList.get(x).getEndorsementsList().size() > largestNumber) {
					largestNumber = postsList.get(x).getEndorsementsList().size();
					mostEndorsedPostID = postsList.get(x).getPostID();
				}
			}
			// Looks through all comments
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
			for (int x = 0; x < commentsList.size(); x++) {
				if (commentsList.get(x).getEndorsementsList().size() > largestNumber) {
					largestNumber = commentsList.get(x).getEndorsementsList().size();
					mostEndorsedPostID = commentsList.get(x).getPostID();
				}
			}
		}
		return mostEndorsedPostID;
	}

	@Override
	public int getMostEndorsedAccount() {
		int maxNumberOfEndorsements = -1;
		int mostEndorsedID = -1;
		for (int pos = 0; pos < accountsList.size(); pos++) {
			// Stores the total number of endorsements for an account
			int numberOfEndorsements = 0;
			ArrayList<Post> postsList = accountsList.get(pos).getOriginalPosts();
			// Adds the number of endorsements for each post and comment
			for (int x = 0; x < postsList.size(); x++) {
				numberOfEndorsements += postsList.get(x).getEndorsementsList().size();
			}
			ArrayList<Comment> commentsList = accountsList.get(pos).getComments();
			for (int x = 0; x < commentsList.size(); x++) {
				numberOfEndorsements += commentsList.get(x).getEndorsementsList().size();
			}
			// Compares the number endorsements with currently largest number
			if (numberOfEndorsements > maxNumberOfEndorsements) {
				maxNumberOfEndorsements = numberOfEndorsements;
				mostEndorsedID = accountsList.get(pos).getAccountID();
			}
		}
		return mostEndorsedID;
	}

	@Override
	public void erasePlatform() {
		// Resets post counter
		for (Account acc : accountsList) {
			if (acc.getOriginalPosts().size() != 0) {
				// Not called if there are no existing posts, so the counter was not changed
				acc.getOriginalPosts().get(0).resetNumberOfPosts();
				break;
			}
		}
		// Clears all the lists
		accountsList.clear();
		deletedCommentsList.clear();
		deletedPostsList.clear();
		// Also resets/clears other counters
		checkedElements.clear();
		indentationNumber = -1;
	}

	@Override
	public void savePlatform(String filename) throws IOException {
		// Creates a dictionary to store the arraylists
		Dictionary<String, Object> all = new Hashtable<>();
		all.put("Accounts", accountsList);
		all.put("DeletedPosts", deletedPostsList);
		all.put("DeletedComments", deletedCommentsList);
		try {
			// Stores the dictionary into a file
			FileOutputStream file = new FileOutputStream(filename + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(all);
			out.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		Dictionary<String, ArrayList<Object>> all = null;
		try {
			// Retrieves a dictionary from a file
			FileInputStream file = new FileInputStream(filename + ".ser");
			ObjectInputStream in = new ObjectInputStream(file);
			all = (Dictionary<String, ArrayList<Object>>) in.readObject();
			in.close();
			file.close();
			// Replaces the arraylists with the ones in the file
			erasePlatform();
			for (Object obj : all.get("Accounts")) {
				accountsList.add((Account) obj);
			}
			for (Object obj : all.get("DeletedPosts")) {
				deletedPostsList.add((DeletedPost) obj);
			}
			for (Object obj : all.get("DeletedComments")) {
				deletedCommentsList.add((DeletedComment) obj);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
