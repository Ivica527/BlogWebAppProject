package blog.main.dao;

import java.util.List;

import blog.main.entity.Comment;

public interface CommentDAO {
	
	public List<Comment> getCommentList();
	
	public void saveComment(Comment comment);
	
	public void deleteComment(int id);
	
	public Comment getComment(int id);
	
	public void getCommentEnabled(int id);
	
	public List<Comment> getCommentListSortByDate();

	public List<Comment> getCommentListSortByDateEnabled();
	
	public List<Comment> getBlogCommentListSortByDateEnabled(int idBlog);
	
	public long getUnseenCommentsCount();
	
	public void getCommentIsSeen(int id);
	
	
}
