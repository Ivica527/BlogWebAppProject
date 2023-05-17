package blog.main.dao;

import java.util.List;

import blog.main.entity.Blog;

public interface BlogDAO {
	
	public List<Blog> getBlogList();
	
	public void saveBlog(Blog blog);
	
	public void deleteBlog (int id);
	
	public Blog getBlog(int id);
	
	public Blog getBlogWithTags(int id);
	
	public Blog getBlogByTitle(String title);	
	
	public void getBlogImportant(int id);
	
	public void getBlogEnabled(int id);	
	
	public List<Blog> getBlogListWithLastThreeBlogs();
	
	public List<Blog> getBlogListWithLastThreeImportantBlogs();
	
	public List<Blog> getThreeMostVisitedBlogPosts();
	
	public List<Blog> getBlogListWithLastNineBlogs();
	
	public List<Blog> getBlogListByCategory(int id);
	
	public void getBlogInkrementNumberSeen(int id);

	public void getBlogInkrementNumberComments(int id);
	
	public void getBlogDekrementNumberComments(int id);	
	
	public Blog getPreviousBlog(int id);
	
	public Blog getNextBlog(int id);
	
	public List<Blog> getListWithEnabledBlogs();
	
	public List<Blog> getBlogListWhatLookingFor(String word);
	
	public List<Blog> getBlogsListOrderBy(int id);
	
	public List<Blog> getBlogListByAuthorID(int id);

}
