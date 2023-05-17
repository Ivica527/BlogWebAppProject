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
public class TagDAOImpl implements TagDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	@Override
	public List<Tag> getTagList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Tag> query = session.createQuery("from Tag",Tag.class);
		
		List<Tag> tagList = query.getResultList();
				
		return tagList;
	}

	@Transactional
	@Override
	public void saveTag(Tag tag) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(tag);
		
	}

	@Transactional
	@Override
	public Tag getTag(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Tag tag = session.get(Tag.class, id);
		
		return tag;
	}
	
	@Transactional
	@Override
	public Tag getTagWithBlogs(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Tag tag = session.get(Tag.class, id);
		
		Hibernate.initialize(tag.getBlogs());
		
		return tag;
	}

	@Transactional
	@Override
	public void deleteTag(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("delete from Tag where id=:id");
		
		query.setParameter("id", id);
		
		query.executeUpdate();
		
	}

	@Transactional
	@Override
	public List<Tag> getTagsById(List<Integer> ids) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Tag> query = session.createQuery("select t from Tag t where t.id IN (:ids)");
		
		query.setParameter("ids", ids);
		
		List<Tag> tags = query.getResultList();
		
		
		return tags;
	}
	@Transactional
	@Override
	public Tag getTagByName(String name) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("select t from Tag t where t.name IN (:name)", Tag.class);
		
		query.setParameter("name", name);
		
		Tag tag = (Tag) query.getSingleResult();
		
		Hibernate.initialize(tag.getBlogs());		
		
		for (Blog blog : tag.getBlogs()) {
			
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
				
		return tag;
	}

	@Transactional
	@Override
	public void getTagSetCount() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Tag> query = session.createQuery("from Tag");
		
		List<Tag> tags = query.getResultList();
		
		for (Tag tag : tags) {
			Hibernate.initialize(tag.getBlogs());
			List<Blog> blogList = new ArrayList<Blog>();
			for (Blog blog : tag.getBlogs()) {
				if (blog.getIsEnabled()==true) {
					blogList.add(blog);
				}
			}
			tag.setCount(blogList.size());
		}		
	}
	
	@Transactional
	@Override
	public List<Tag> getTagOnRightSide() {

		Session session = sessionFactory.getCurrentSession();
		
		Query<Tag> query = session.createQuery("select t from Tag t order by t.count DESC");
		
		List<Tag> tags = query.getResultList();
		
		List<Tag> tagList = new ArrayList<Tag>();
		
		for (int i = 0; i < tags.size(); i++) {
			if (i<10 && tags.get(i).getCount()>0) {
				tagList.add(tags.get(i));
			}			
		}		
		
		return tagList;
	}	

}
