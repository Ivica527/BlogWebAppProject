package blog.main.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import blog.main.entity.Comment;



@Repository
public class CommentDAOImpl implements CommentDAO {
	
	@Autowired 
	private SessionFactory sessionFactory;
	
	
	@Transactional
	@Override
	public List<Comment> getCommentList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Comment> query = session.createQuery("from Comment", Comment.class);
		
		List<Comment> commentList = query.getResultList();		
		
		return commentList;
	}
	
	@Transactional
	@Override
	public void saveComment(Comment comment) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(comment);
	}
	
	@Transactional
	@Override
	public void deleteComment(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Comment comment = session.get(Comment.class, id);
		
		session.delete(comment);		
		
	}
	
	@Transactional
	@Override
	public Comment getComment(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Comment comment = session.get(Comment.class, id);
		
		return comment;
	}
	
	@Transactional
	@Override
	public List<Comment> getCommentListSortByDate() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Comment> query = session.createQuery("SELECT c from Comment c ORDER BY c.date DESC",Comment.class);
		
		List<Comment> commentList = query.getResultList();		
		
		return commentList;
	}
	
	@Transactional
	@Override
	public List<Comment> getCommentListSortByDateEnabled() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Comment> query = session.createQuery("SELECT c from Comment c WHERE c.isEnabled = 1 ORDER BY c.date DESC",Comment.class);
		
		List<Comment> commentList = query.getResultList();		
		
		return commentList;
	}
	
	@Transactional
	@Override
	public void getCommentEnabled(int id) {

		Session session = sessionFactory.getCurrentSession();
		
		Comment comment = session.get(Comment.class, id);
		
		comment.setIsEnabled(!comment.getIsEnabled());
		
		session.save(comment);		
	}
	@Transactional
	@Override
	public List<Comment> getBlogCommentListSortByDateEnabled(int idBlog) {

		Session session = sessionFactory.getCurrentSession();
		
		Query<Comment> query = session.createQuery("SELECT c from Comment c WHERE c.isEnabled = 1 AND c.idBlog=:idBlog ORDER BY c.date DESC",Comment.class);
		
		query.setParameter("idBlog", idBlog);
		
		List<Comment> commentList = query.getResultList();		
		
		return commentList;
	}
	@Transactional
	@Override
	public long getUnseenCommentsCount() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("select count(*) from Comment c where c.isSeen=0");
		
		return (long) query.uniqueResult();
	}
	@Transactional
	@Override
	public void getCommentIsSeen(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Comment comment = session.get(Comment.class, id);
		
		comment.setIsSeen(!comment.getIsSeen());
		
		session.save(comment);		
		
	}

}
