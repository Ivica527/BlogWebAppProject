package blog.main.dao;

import java.util.List;

import blog.main.entity.Category;

public interface CategoryDAO {
	
	public List<Category> getCategoryList();
	
	public void saveCategory(Category category);
	
	public Category getCategory (int id);
	
	public Category getCategoryByName (String name);
	
	public void deleteCategory(int id);
	
	public List<Category> getCategoryInPriority();
	
	public List<Category> getCategoryOnRightSide();	
	
	public List<Category> getReductedCategoryList();
	
	public void resetCategoryList();
	

}
