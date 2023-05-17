package blog.main.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import blog.main.entity.Blog;
import blog.main.entity.Category;



@Repository
public class CategoryDAOImpl implements CategoryDAO {

	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	
	
	@Transactional
	@Override
	public List<Category> getCategoryList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Category> query = session.createQuery("from Category", Category.class);
		
		List<Category> categoryList = query.getResultList();		
				
		return categoryList;
	}

	@Transactional
	@Override
	public void saveCategory(Category category) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(category);
		
	}

	@Transactional
	@Override
	public Category getCategory(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Category category = session.get(Category.class, id);
		
		return category;
	}

	@Transactional
	@Override
	public void deleteCategory(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("delete from Category where id=:id");
		
		query.setParameter("id", id);
		
		query.executeUpdate();		
		
	}
	@Transactional
	@Override
	public List<Category> getCategoryInPriority() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Category> query1 = session.createQuery("from Category", Category.class);
		
		List<Category> categoryList1 = query1.getResultList();
		
		for (Category category : categoryList1) {			
				
			Query<Blog> blogQuery = session.createQuery("select b from Blog b where b.category.id=:id");
				
			blogQuery.setParameter("id", category.getId());
				
			category.setBlogs(blogQuery.getResultList());
			
			category.setCount(category.getBlogs().size());			
		}
		int id=1;
		Query<Category> query2= session.createQuery("select c from Category c where c.id!=:id AND c.count>0 order by c.count DESC", Category.class);
		query2.setParameter("id", id);
		List<Category> categoryList2 = query2.getResultList();		
		List<Category> categoryList = new ArrayList<Category>();		
		for (int i = 0; i < categoryList2.size(); i++) {			
			if (i<4) {
				categoryList.add(categoryList2.get(i));
			}			
		}	
		
		return categoryList;		
	}
	@Transactional
	@Override
	public Category getCategoryByName(String name) {		
				
		Session session = sessionFactory.getCurrentSession();
		
		Query<Category> query = session.createQuery("select c from Category c where c.name IN (:name)", Category.class);
		
		query.setParameter("name", name);
		
		Category category = (Category) query.getSingleResult();
		
		Query<Blog> blogQuery = session.createQuery("select b from Blog b where b.category.id=:id");
		
		blogQuery.setParameter("id", category.getId());
			
		category.setBlogs(blogQuery.getResultList());		
		
		return category;
	}
	
	@Transactional
	@Override
	public List<Category> getCategoryOnRightSide() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Category> query = session.createQuery("SELECT c from Category c where c.isVisible=1 order by c.numberInPriority ASC ", Category.class);
		
		List<Category> categoryList1 = query.getResultList();
		List<Category> categoryList = new ArrayList<Category>();		
		for (int i = 0; i < categoryList1.size(); i++) {			
			if (i<5) {				
				categoryList.add(categoryList1.get(i));
			}			
		}		
		
		return categoryList;
	}
	@Transactional
	@Override
	public List<Category> getReductedCategoryList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Category> query = session.createQuery("SELECT c from Category c where c.isVisible=1 order by c.numberInPriority ASC ", Category.class);
		
		List<Category> categoryList = query.getResultList();		
		
		if (categoryList.size() < 5 && categoryList.size() > 0) {
			
			for (int i = 0; i < categoryList.size(); i++) {
				categoryList.get(i).setNumberInPriority(i+1);
			}			
		}		
		return categoryList;
	}
	@Transactional
	@Override
	public void resetCategoryList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Category> query = session.createQuery("SELECT c from Category c where c.isVisible=1 ", Category.class);
		
		List<Category> categoryList = query.getResultList();
		
		for (Category category : categoryList) {
			category.setIsVisible(false);
			category.setNumberInPriority(0);
			session.saveOrUpdate(category);
		}		
	}

	
}
