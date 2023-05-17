package blog.main.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import blog.main.entity.Blog;
import blog.main.entity.Comment;
import blog.main.entity.Role;
import blog.main.entity.User;


@Repository
public class UserDAOImpl implements UserDAO {
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	@Override
	public List<User> getUserList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<User> query = session.createQuery("from User",User.class);
		
		List<User> userList = query.getResultList();
		
		return userList;		
	}
	@Transactional
	@Override
	public User getUserByUsername(String username) {
		
		Session session = sessionFactory.getCurrentSession();	
		
		User user = session.get(User.class, username);
		
		Query<Blog> query = session.createQuery("SELECT b FROM Blog b WHERE b.username IN (:username)", Blog.class);
		
		query.setParameter("username", username);
		
		List<Blog> blogList= query.getResultList();		
		
		user.setBlogs(blogList);

		return user;
	}
	@Transactional
	@Override
	public void saveUser(User user) {

		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(user);

	}
	@Transactional
	@Override
	public void deleteUser(String username) {
		
		Session session = sessionFactory.getCurrentSession();
		
		User user = session.get(User.class, username);
		
		session.delete(user);

	}
	@Transactional
	@Override
	public void enableUser(String username) {
		
		Session session = sessionFactory.getCurrentSession();
		
		User user = session.get(User.class, username);
		
		user.setEnabled(!user.getEnabled());
		
		session.saveOrUpdate(user);

	}
	@Transactional
	@Override
	public List<Role> getRoleList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Role> query = session.createQuery("from Role",Role.class);
		
		List<Role> roleList = query.getResultList();
		
		return roleList;		
	}
	
	@Transactional
	@Override
	public User getUserByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("SELECT u FROM User u WHERE u.name IN (:name)", User.class);
		
		query.setParameter("name", name);
		
		User user = (User) query.getSingleResult();
		
		Query<Blog> query1 = session.createQuery("SELECT b FROM Blog b WHERE b.username IN (:username)", Blog.class);
		
		query1.setParameter("username", user.getUsername());
		
		List<Blog> blogList= query1.getResultList();
		
		user.setBlogs(blogList);
		
		return user;
	}
	

}
