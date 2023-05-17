package blog.main.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import blog.main.entity.Blog;
import blog.main.entity.Tag;
import blog.main.entity.User;

@Repository
public class BlogDAOImpl implements BlogDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	

	@Transactional
	@Override
	public List<Blog> getBlogList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Blog> query = session.createQuery("from Blog", Blog.class);
		
		List<Blog> blogList = query.getResultList();
		
		for (Blog blog : blogList) {				
			
			blog.setUser(session.get(User.class, blog.getUsername()));		
		}	
		
		return blogList;
	}

	@Transactional
	@Override
	public void saveBlog(Blog blog) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(blog);
	}

	@Transactional
	@Override
	public void deleteBlog(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Blog blog = session.get(Blog.class, id);
		
		session.delete(blog);
	}

	@Transactional
	@Override
	public Blog getBlog(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Blog blog = session.get(Blog.class, id);
		
		blog.setUser(session.get(User.class, blog.getUsername()));	
		
		return blog;
	}
	
	@Transactional
	@Override
	public Blog getBlogWithTags(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Blog blog = session.get(Blog.class, id);
		
		blog.setUser(session.get(User.class, blog.getUsername()));	
		
		Hibernate.initialize(blog.getTags());
		
		for (Tag tag : blog.getTags()) {
			Hibernate.initialize(tag.getBlogs());
		}
		
		return blog;
	}
	
	@Transactional
	@Override
	public void getBlogImportant(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Blog blog = session.get(Blog.class, id);
		
		blog.setUser(session.get(User.class, blog.getUsername()));		
		
		blog.setIsImportant(!blog.getIsImportant());
		
		session.save(blog);		
	}

	@Transactional
	@Override
	public void getBlogEnabled(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Blog blog = session.get(Blog.class, id);
		
		blog.setIsEnabled(!blog.getIsEnabled());
		
		session.save(blog);		
	}
		
	
	@Transactional
	@Override
	public List<Blog> getBlogListWithLastThreeBlogs() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Blog> query = session.createQuery("SELECT b from Blog b where b.isEnabled=1 order by b.date DESC ", Blog.class);
		
		List<Blog> blogList = new ArrayList<>();
		
		List<Blog> blogList1 = query.getResultList();		
	
		for (int i = 0; i < blogList1.size(); i++) {
			if (i<3) {				
				blogList1.get(i).setUser(session.get(User.class, blogList1.get(i).getUsername()));				
				blogList.add(blogList1.get(i));
				
			}							
		}
		
		return blogList;
	}
	@Transactional
	@Override
	public List<Blog> getBlogListWithLastThreeImportantBlogs() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Blog> query = session.createQuery("SELECT b from Blog b where b.isImportant=1 and b.isEnabled=1 order by b.date DESC ", Blog.class);
		
		List<Blog> blogList = new ArrayList<>();
		
		List<Blog> blogList1 = query.getResultList();
		
		for (int i = 0; i < blogList1.size(); i++) {
			
			blogList1.get(i).setUser(session.get(User.class, blogList1.get(i).getUsername()));		
			
			if (i<3) {				
							
				DateTimeFormatter Pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
				LocalDateTime datetime1 = LocalDateTime.parse(blogList1.get(i).getDate(), Pattern);
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss XXX");			   
			    LocalDateTime datetime2 = LocalDateTime.parse(formatter.format( new Date()), Pattern);
			    
			    if (datetime2.getYear()-datetime1.getYear()>0) {
			    	blogList1.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " years ago");
				}		
				else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()>0) {
					blogList1.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " months ago");
				}
				else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()>0) {
					blogList1.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " days ago");
				}
				else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()>0) {
					blogList1.get(i).setSinceThen(datetime2.getHour()-datetime1.getHour()+ " hours ago");
				}
				else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()>0) {
					blogList1.get(i).setSinceThen(datetime2.getMinute()-datetime1.getMinute()+ " minutes ago");
				}
				else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()==0 && datetime2.getSecond()-datetime1.getSecond()>0) {
					blogList1.get(i).setSinceThen(datetime2.getSecond()-datetime1.getSecond()+ " seconds ago");
				}    
				
				blogList.add(blogList1.get(i));	
			}							
		}
		
		return blogList;		
	}

	
	@Transactional
	@Override
	public List<Blog> getBlogListWithLastNineBlogs() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Blog> query = session.createQuery("SELECT b from Blog b where b.isEnabled=1 order by b.date DESC ", Blog.class);
		
		List<Blog> blogList = new ArrayList<>();
		
		List<Blog> blogList1 = query.getResultList();
		
		for (int i = 0; i < blogList1.size(); i++) {
			if (i<9) {
				blogList1.get(i).setUser(session.get(User.class, blogList1.get(i).getUsername()));		
				blogList.add(blogList1.get(i));				
			}							
		}
		
		return blogList;		
	}
	@Transactional
	@Override
	public List<Blog> getBlogListByCategory(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Blog> query = session.createQuery("SELECT b from Blog b where b.category.id=:id", Blog.class);
		
		query.setParameter("id", id);		
		
		List<Blog> blogList = query.getResultList();
		
		for (Blog blog : blogList) {			
			
			blog.setUser(session.get(User.class, blog.getUsername()));		
			
			DateTimeFormatter Pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			LocalDateTime datetime1 = LocalDateTime.parse(blog.getDate(), Pattern);
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss XXX");			   
		    LocalDateTime datetime2 = LocalDateTime.parse(formatter.format( new Date()), Pattern);
		    
		    if (datetime2.getYear()-datetime1.getYear()>0) {
		    	blog.setSinceThen(datetime2.compareTo(datetime1)+ " years ago");
			}		
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()>0) {
				blog.setSinceThen(datetime2.compareTo(datetime1)+ " months ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()>0) {
				blog.setSinceThen(datetime2.compareTo(datetime1)+ " days ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()>0) {
				blog.setSinceThen(datetime2.getHour()-datetime1.getHour()+ " hours ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()>0) {
				blog.setSinceThen(datetime2.getMinute()-datetime1.getMinute()+ " minutes ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()==0 && datetime2.getSecond()-datetime1.getSecond()>0) {
				blog.setSinceThen(datetime2.getSecond()-datetime1.getSecond()+ " seconds ago");
			}			
		}
		
		return blogList;
	}
	@Transactional
	@Override
	public Blog getBlogByTitle(String title) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("select b from Blog b where b.title IN (:title)", Blog.class);
		
		query.setParameter("title", title);		
		
		Blog blog = (Blog) query.getSingleResult();			
		
		blog.setUser(session.get(User.class, blog.getUsername()));
		
		Hibernate.initialize(blog.getTags());
		
		List<Tag> tagList = blog.getTags();
		
		for (Tag tag : tagList) {
			Hibernate.initialize(tag.getBlogs());
		}
		
		DateTimeFormatter Pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
		LocalDateTime datetime1 = LocalDateTime.parse(blog.getDate(), Pattern);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss XXX");			   
	    LocalDateTime datetime2 = LocalDateTime.parse(formatter.format( new Date()), Pattern);
	    
	    if (datetime2.getYear()-datetime1.getYear()>0) {
	    	blog.setSinceThen(datetime2.compareTo(datetime1)+ " years ago");
		}		
		else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()>0) {
			blog.setSinceThen(datetime2.compareTo(datetime1)+ " months ago");
		}
		else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()>0) {
			blog.setSinceThen(datetime2.compareTo(datetime1)+ " days ago");
		}
		else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()>0) {
			blog.setSinceThen(datetime2.getHour()-datetime1.getHour()+ " hours ago");
		}
		else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()>0) {
			blog.setSinceThen(datetime2.getMinute()-datetime1.getMinute()+ " minutes ago");
		}
		else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()==0 && datetime2.getSecond()-datetime1.getSecond()>0) {
			blog.setSinceThen(datetime2.getSecond()-datetime1.getSecond()+ " seconds ago");
		}					
		
		return blog;
	}
	@Transactional
	@Override
	public List<Blog> getThreeMostVisitedBlogPosts() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Blog> query = session.createQuery("SELECT b from Blog b where b.isEnabled=1 order by b.numberSeen DESC ", Blog.class);		
		
		List<Blog> blogList = new ArrayList<>();
		
		List<Blog> blogList1 = query.getResultList();
		
		for (int i = 0; i < blogList1.size(); i++) {
			if (i<3) {				
				blogList1.get(i).setUser(session.get(User.class, blogList1.get(i).getUsername()));
				blogList.add(blogList1.get(i));				
			}							
		}		
		return blogList;
	}
	@Transactional
	@Override
	public void getBlogInkrementNumberSeen(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Blog blog = session.get(Blog.class, id);
		blog.setUser(session.get(User.class, blog.getUsername()));
		
		blog.setNumberSeen(blog.getNumberSeen()+1);
		
		session.saveOrUpdate(blog);
		
	}
	
	@Transactional
	@Override
	public void getBlogInkrementNumberComments(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Blog blog = session.get(Blog.class, id);
		blog.setUser(session.get(User.class, blog.getUsername()));
		
		blog.setNumberComments(blog.getNumberComments()+1);
		
		session.saveOrUpdate(blog);
		
	}
	
	@Transactional
	@Override
	public void getBlogDekrementNumberComments(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Blog blog = session.get(Blog.class, id);
		blog.setUser(session.get(User.class, blog.getUsername()));
		
		blog.setNumberComments(blog.getNumberComments()-1);
		
		session.saveOrUpdate(blog);
		
	}
	@Transactional
	@Override
	public Blog getPreviousBlog(int id) {

		Session session = sessionFactory.getCurrentSession();
		
		Blog blog = session.get(Blog.class, id);
		
		Query<Blog> query = session.createQuery("select b from Blog b where b.isEnabled=1");
		
		List<Blog> blogList = query.getResultList();
		Blog previusBlog=null;
		for (int i = 0; i < blogList.size(); i++) {
			
			if (blog.equals(blogList.get(i)) & i==0) {
				previusBlog =blogList.get(blogList.size()-1);
			}			
			else if (blog.equals(blogList.get(i))) {
				previusBlog =blogList.get(i-1);
			}			
		}		
		previusBlog.setUser(session.get(User.class, previusBlog.getUsername()));
		return previusBlog;
	}
	@Transactional
	@Override
	public Blog getNextBlog(int id) {

		Session session = sessionFactory.getCurrentSession();
		
		Blog blog = session.get(Blog.class, id);
		
		Query<Blog> query = session.createQuery("select b from Blog b where b.isEnabled=1");
		
		List<Blog> blogList = query.getResultList();
		Blog nextBlog=null;
		for (int i = 0; i < blogList.size(); i++) {
			
			if (blog.equals(blogList.get(i)) & i==(blogList.size()-1)) {
				nextBlog =blogList.get(0);
			}
			else if (blog.equals(blogList.get(i))) {
				nextBlog =blogList.get(i+1);
			}			
		}	
		nextBlog.setUser(session.get(User.class, nextBlog.getUsername()));
		return nextBlog;
	}
	@Transactional
	@Override
	public List<Blog> getListWithEnabledBlogs() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Blog> query = session.createQuery("select b from Blog b where b.isEnabled=1 order by b.date DESC", Blog.class);
		
		List<Blog> blogList = query.getResultList();
		
		for (int i = 0; i < blogList.size(); i++) {	
			
			blogList.get(i).setUser(session.get(User.class, blogList.get(i).getUsername()));
							
				DateTimeFormatter Pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
				LocalDateTime datetime1 = LocalDateTime.parse(blogList.get(i).getDate(), Pattern);
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss XXX");			   
			    LocalDateTime datetime2 = LocalDateTime.parse(formatter.format( new Date()), Pattern);
			    
			    if (datetime2.getYear()-datetime1.getYear()>0) {
			    	blogList.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " years ago");
				}		
				else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()>0) {
					blogList.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " months ago");
				}
				else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()>0) {
					blogList.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " days ago");
				}
				else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()>0) {
					blogList.get(i).setSinceThen(datetime2.getHour()-datetime1.getHour()+ " hours ago");
				}
				else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()>0) {
					blogList.get(i).setSinceThen(datetime2.getMinute()-datetime1.getMinute()+ " minutes ago");
				}
				else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()==0 && datetime2.getSecond()-datetime1.getSecond()>0) {
					blogList.get(i).setSinceThen(datetime2.getSecond()-datetime1.getSecond()+ " seconds ago");
				}					
		}
		
		return blogList;
	}

	@Transactional
	@Override
	public List<Blog> getBlogListWhatLookingFor(String word) {
		
		Session session = sessionFactory.getCurrentSession();
		
		String queryString = "select b from Blog b where b.title like '%"+word+"%' or b.description like '%"+word+"%' or b.text like '%"+word+"%'";		
		
		Query<Blog> query = session.createQuery(queryString, Blog.class);	
		
		List<Blog> blogList = query.getResultList();
		
		for (int i = 0; i < blogList.size(); i++) {	
			
			blogList.get(i).setUser(session.get(User.class, blogList.get(i).getUsername()));
			
			DateTimeFormatter Pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			LocalDateTime datetime1 = LocalDateTime.parse(blogList.get(i).getDate(), Pattern);
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss XXX");			   
		    LocalDateTime datetime2 = LocalDateTime.parse(formatter.format( new Date()), Pattern);
		    
		    if (datetime2.getYear()-datetime1.getYear()>0) {
		    	blogList.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " years ago");
			}		
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()>0) {
				blogList.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " months ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()>0) {
				blogList.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " days ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()>0) {
				blogList.get(i).setSinceThen(datetime2.getHour()-datetime1.getHour()+ " hours ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()>0) {
				blogList.get(i).setSinceThen(datetime2.getMinute()-datetime1.getMinute()+ " minutes ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()==0 && datetime2.getSecond()-datetime1.getSecond()>0) {
				blogList.get(i).setSinceThen(datetime2.getSecond()-datetime1.getSecond()+ " seconds ago");
			}					
		}		
		
		return blogList;
	}

	@Transactional
	@Override
	public List<Blog> getBlogsListOrderBy(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Blog> query1 = session.createQuery("from Blog", Blog.class);
		
		List<Blog> blogList1 = query1.getResultList();
		for (Blog blog : blogList1) {			
			blog.setUser(session.get(User.class, blog.getUsername()));
		}	
		
		Query<Blog> query=null;
		if (id==0) {
			query = session.createQuery("select b from Blog b ORDER BY b.title",Blog.class);
		}
		else if (id==1) {
			query = session.createQuery("select b from Blog b  ORDER BY b.category.name",Blog.class);
		}
		else if (id==2) {
			query = session.createQuery("select b from Blog b  ORDER BY b.author.name",Blog.class);
		}
		else if (id==3) {
			query = session.createQuery("select b from Blog b WHERE b.isEnabled=1 ORDER BY b.id",Blog.class);
		}
		else if (id==4) {
			query = session.createQuery("select b from Blog b WHERE b.isEnabled=0 ORDER BY b.id",Blog.class);
		}
		else {
			query = session.createQuery("from Blog",Blog.class);
		}
		
		List<Blog> blogList = query.getResultList();
		
		
		return blogList;
	}
	@Transactional
	@Override
	public List<Blog> getBlogListByAuthorID(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Blog> query = session.createQuery("select b from Blog b where b.idAuthor=:id order by b.date DESC", Blog.class);
		
		query.setParameter("id", id);		
		
		List<Blog> blogList = query.getResultList();
		
		for (int i = 0; i < blogList.size(); i++) {	
			
			blogList.get(i).setUser(session.get(User.class, blogList.get(i).getUsername()));
			
			DateTimeFormatter Pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			LocalDateTime datetime1 = LocalDateTime.parse(blogList.get(i).getDate(), Pattern);
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss XXX");			   
		    LocalDateTime datetime2 = LocalDateTime.parse(formatter.format( new Date()), Pattern);
		    
		    if (datetime2.getYear()-datetime1.getYear()>0) {
		    	blogList.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " years ago");
			}		
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()>0) {
				blogList.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " months ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()>0) {
				blogList.get(i).setSinceThen(datetime2.compareTo(datetime1)+ " days ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()>0) {
				blogList.get(i).setSinceThen(datetime2.getHour()-datetime1.getHour()+ " hours ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()>0) {
				blogList.get(i).setSinceThen(datetime2.getMinute()-datetime1.getMinute()+ " minutes ago");
			}
			else if (datetime2.getYear()-datetime1.getYear()==0 && datetime2.getMonthValue()-datetime1.getMonthValue()==0 && datetime2.getDayOfMonth()-datetime1.getDayOfMonth()==0 && datetime2.getHour()-datetime1.getHour()==0 && datetime2.getMinute()-datetime1.getMinute()==0 && datetime2.getSecond()-datetime1.getSecond()>0) {
				blogList.get(i).setSinceThen(datetime2.getSecond()-datetime1.getSecond()+ " seconds ago");
			}					
		}		
		
		return blogList;		
	}
	
}
