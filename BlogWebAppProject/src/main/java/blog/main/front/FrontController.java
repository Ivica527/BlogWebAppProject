package blog.main.front;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import blog.main.dao.BlogDAO;
import blog.main.dao.CategoryDAO;
import blog.main.dao.CommentDAO;
import blog.main.dao.MessageDAO;
import blog.main.dao.SliderDAO;
import blog.main.dao.TagDAO;
import blog.main.dao.UserDAO;
import blog.main.entity.Blog;
import blog.main.entity.Category;
import blog.main.entity.Comment;
import blog.main.entity.Message;
import blog.main.entity.Slider;
import blog.main.entity.Tag;
import blog.main.entity.User;

@Controller
@RequestMapping("/")
public class FrontController {
	
	@Autowired
	private SliderDAO sliderDAO;
	
	@Autowired
	private BlogDAO blogDAO;
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private MessageDAO messageDAO;
	
	@Autowired
	private CommentDAO commentDAO;
	
	@Autowired
	private TagDAO tagDAO;	
	
	@Autowired
	private UserDAO userDAO;
	
	
	
	
	@RequestMapping({"/","/index-page"})
	public String getIndexPage(Model model){
		
		
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();
		tagDAO.getTagSetCount();
		List<Slider> sliderList = sliderDAO.getSlidersVisibleOnMainPage();
		Blog blog = new Blog();
		List<Blog> blogListImportant = blogDAO.getBlogListWithLastThreeImportantBlogs();
		
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);			
			blog2.setDate(datetime.format(newPattern));			
		}
		List<Blog> blogListLastNine = blogDAO.getBlogListWithLastNineBlogs();
		for (Blog blog3 : blogListLastNine) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd MMMM | yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog3.getDate(), oldPattern);
			blog3.setDate(datetime.format(newPattern));			
		}
		for (Blog blog4 : blogListImportant) {
			blog4.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog4.getId()));			
		}
		
		model.addAttribute("categoryListInPriority", categoryListInPriority);		
		model.addAttribute("sliderList", sliderList);
		model.addAttribute("blog", blog);
		model.addAttribute("blogListImportant", blogListImportant);
		model.addAttribute("blogListLastThree", blogListLastThree);
		model.addAttribute("blogListLastNine", blogListLastNine);
		
		
		return"front/index-page";
	}
	
	@RequestMapping("/blog-search-page")
	public String getBlogSearchPage(@ModelAttribute Blog blog1, Model model){
		
		List<Blog> blogSearchList = blogDAO.getBlogListWhatLookingFor(blog1.getTitle());
		String word = blog1.getTitle();
		for (Blog blog2 : blogSearchList) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd MMMM | yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));	
		}
		
		
		Blog blog = new Blog();
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();		
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();		
		for (Blog blog3 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog3.getDate(), oldPattern);
			blog3.setDate(datetime.format(newPattern));			
		}		
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();
		
		int c = 0;
		int k = 1;
		int b = 1;
		
		if (blogSearchList.size()%4>0) {
			c=(blogSearchList.size()/4)+1;
		}
		else if (blogSearchList.size()%4==0) {
			c=(blogSearchList.size()/4);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		
		if (blogSearchList.size()>4) {
			for (int i = 0; i < 4; i++) {
				blogList.add(blogSearchList.get(i));
			}			
		}
		else if (blogSearchList.size()<=4) {
			for (int i = 0; i < blogSearchList.size(); i++) {
				blogList.add(blogSearchList.get(i));
			}
		}				
		
		model.addAttribute("blog", blog);		
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blogListLastThree", blogListLastThree);
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);	
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);	
		model.addAttribute("blogSearchList", blogList);
		model.addAttribute("word", word);
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);
		
		return"front/blog-search-page";
	}
	
	@RequestMapping("/pagination-blog-search-page")
	public String getPaginationBlogSearchPage(@RequestParam String word, Model model, @RequestParam int k){
		
		List<Blog> blogSearchList = blogDAO.getBlogListWhatLookingFor(word);
		for (Blog blog2 : blogSearchList) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd MMMM | yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));	
		}		
		
		Blog blog = new Blog();
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();		
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();		
		for (Blog blog3 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog3.getDate(), oldPattern);
			blog3.setDate(datetime.format(newPattern));			
		}		
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();
		
		
		int c = 0;		
		if (blogSearchList.size()%4>0) {
			c=(blogSearchList.size()/4)+1;
		}
		else if (blogSearchList.size()%4==0) {
			c=(blogSearchList.size()/4);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		if (blogSearchList.size()>(3*k+k)) {
			for (int i = (3*k+(k-4)); i < (3*k+k); i++) {
			blogList.add(blogSearchList.get(i));
				}			
		}
		else if (blogSearchList.size()<=(3*k+k)) {
			for (int i = (3*k+(k-4)); i < blogSearchList.size(); i++) {
				blogList.add(blogSearchList.get(i));
				}
		}
		int b = k;
		
		model.addAttribute("blog", blog);		
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blogListLastThree", blogListLastThree);
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);	
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);	
		model.addAttribute("blogSearchList", blogList);
		model.addAttribute("word", word);
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);
		
		return"front/blog-search-page";
	}
	
	@RequestMapping("/blog-page")
	public String getBlogPage(Model model){		
		
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();		
		Blog blog = new Blog();		
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));			
		}		
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();
		List<Blog> blogListOnBlogPage = blogDAO.getListWithEnabledBlogs();
		for (Blog blog2 : blogListOnBlogPage) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd MMMM | yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));	
		}
		
		
		int c = 0;
		int k = 1;
		int b = 1;
		
		if (blogListOnBlogPage.size()%4>0) {
			c=(blogListOnBlogPage.size()/4)+1;
		}
		else if (blogListOnBlogPage.size()%4==0) {
			c=(blogListOnBlogPage.size()/4);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		
		if (blogListOnBlogPage.size()>4) {
			for (int i = 0; i < 4; i++) {
			blogList.add(blogListOnBlogPage.get(i));
		}
		}
		else if (blogListOnBlogPage.size()<=4) {
			for (int i = 0; i < blogListOnBlogPage.size(); i++) {
				blogList.add(blogListOnBlogPage.get(i));
			}
		}
				
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blog", blog);
		model.addAttribute("blogListLastThree", blogListLastThree);
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);	
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);	
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);	
		model.addAttribute("blogListOnBlogPage", blogList);
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);
		
		return"front/blog-page";
	}
	
	
	
	@RequestMapping("/pagination-blog-page")
	public String getPaginationBlogPage(Model model, @RequestParam int k){		
		
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();		
		Blog blog = new Blog();		
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));			
		}		
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();
		List<Blog> blogListOnBlogPage = blogDAO.getListWithEnabledBlogs();
		for (Blog blog2 : blogListOnBlogPage) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd MMMM | yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));	
		}
		
		
		int c = 0;		
		if (blogListOnBlogPage.size()%4>0) {
			c=(blogListOnBlogPage.size()/4)+1;
		}
		else if (blogListOnBlogPage.size()%4==0) {
			c=(blogListOnBlogPage.size()/4);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		if (blogListOnBlogPage.size()>(3*k+k)) {
			for (int i = (3*k+(k-4)); i < (3*k+k); i++) {
			blogList.add(blogListOnBlogPage.get(i));
				}			
		}
		else if (blogListOnBlogPage.size()<=(3*k+k)) {
			for (int i = (3*k+(k-4)); i < blogListOnBlogPage.size(); i++) {
				blogList.add(blogListOnBlogPage.get(i));
				}
		}
		int b = k;
				
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blog", blog);
		model.addAttribute("blogListLastThree", blogListLastThree);
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);	
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);	
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);	
		model.addAttribute("blogListOnBlogPage", blogList);
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);
		
		return"front/blog-page";
	}
		
	
	@RequestMapping("/contact-page")
	public String getContactPage(Model model){
				
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();		
		Blog blog = new Blog();
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));			
		}	
		Message message = new Message();		
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();		
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		
		
		model.addAttribute("categoryListInPriority", categoryListInPriority);		
		model.addAttribute("blog", blog);
		model.addAttribute("blogListLastThree", blogListLastThree);		
		model.addAttribute("message", message);
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);	
		
		return"front/contact-page";
	}
	
	@RequestMapping("/blog-author-page")
	public String getBlogByAuthorPage(@RequestParam String name, Model model){
		
		User user = userDAO.getUserByName(name);		
		for (Blog blog : user.getBlogs()) {
			blog.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog.getId()));
			blog.setUser(userDAO.getUserByName(name));
		}		
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();
		Blog blog = new Blog();
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();		
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));			
		}
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();		
		
		int c = 0;
		int k = 1;
		int b = 1;
		
		if (user.getBlogs().size()%4>0) {
			c=(user.getBlogs().size()/4)+1;
		}
		else if (user.getBlogs().size()%4==0) {
			c=(user.getBlogs().size()/4);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		
		if (user.getBlogs().size()>4) {
			for (int i = 0; i < 4; i++) {
			blogList.add(user.getBlogs().get(i));
		}
		}
		else if (user.getBlogs().size()<=4) {
			for (int i = 0; i < user.getBlogs().size(); i++) {
				blogList.add(user.getBlogs().get(i));
			}
		}		
		user.setBlogs(blogList);
		
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blog", blog);
		model.addAttribute("blogListLastThree", blogListLastThree);
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);	
		model.addAttribute("user", user);	
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);		

		return"front/blog-author-page";
	}
	
	@RequestMapping("/pagination-blog-author-page")
	public String getPaginationBlogByAuthorPage(@RequestParam String name, Model model, @RequestParam int k){
		
		User user = userDAO.getUserByName(name);		
		for (Blog blog : user.getBlogs()) {
			blog.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog.getId()));
			blog.setUser(userDAO.getUserByName(name));
		}		
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();
		Blog blog = new Blog();
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();		
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));			
		}
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();		
		
		int c = 0;		
		if (user.getBlogs().size()%4>0) {
			c=(user.getBlogs().size()/4)+1;
		}
		else if (user.getBlogs().size()%4==0) {
			c=(user.getBlogs().size()/4);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		if (user.getBlogs().size()>(3*k+k)) {
			for (int i = (3*k+(k-4)); i < (3*k+k); i++) {
			blogList.add(user.getBlogs().get(i));
				}			
		}
		else if (user.getBlogs().size()<=(3*k+k)) {
			for (int i = (3*k+(k-4)); i < user.getBlogs().size(); i++) {
				blogList.add(user.getBlogs().get(i));
				}
		}
		int b = k;
		
		user.setBlogs(blogList);
		
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blog", blog);
		model.addAttribute("blogListLastThree", blogListLastThree);
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);	
		model.addAttribute("user", user);	
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);		

		return"front/blog-author-page";
	}
	
	
	
	
	
	@RequestMapping("/blog-category-page")
	public String getBlogByCategoryPage(@RequestParam String name, Model model){
		
		Category category = categoryDAO.getCategoryByName(name);		
		category.setBlogs(blogDAO.getBlogListByCategory(category.getId()));		
		for (Blog blog2 : category.getBlogs()) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd MMMM | yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}		
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();
		Blog blog = new Blog();			
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();		
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));			
		}	
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();
		
		int c = 0;
		int k = 1;
		int b = 1;
		
		if (category.getBlogs().size()%4>0) {
			c=(category.getBlogs().size()/4)+1;
		}
		else if (category.getBlogs().size()%4==0) {
			c=(category.getBlogs().size()/4);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		
		if (category.getBlogs().size()>4) {
			for (int i = 0; i < 4; i++) {
			blogList.add(category.getBlogs().get(i));
		}
		}
		else if (category.getBlogs().size()<=4) {
			for (int i = 0; i < category.getBlogs().size(); i++) {
				blogList.add(category.getBlogs().get(i));
			}
		}
		
		category.setBlogs(blogList);
		
		model.addAttribute("category", category);		
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blog", blog);		
		model.addAttribute("blogListLastThree", blogListLastThree);
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);	
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);
		
		return"front/blog-category-page";
	}
	
	
	@RequestMapping("/pagination-blog-category-page")
	public String getPaginationBlogByCategoryPage(@RequestParam String name, Model model, @RequestParam int k){
		
		Category category = categoryDAO.getCategoryByName(name);		
		category.setBlogs(blogDAO.getBlogListByCategory(category.getId()));		
		for (Blog blog2 : category.getBlogs()) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd MMMM | yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}		
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();
		Blog blog = new Blog();			
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();		
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));			
		}	
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();
		
		
		int c = 0;		
		if (category.getBlogs().size()%4>0) {
			c=(category.getBlogs().size()/4)+1;
		}
		else if (category.getBlogs().size()%4==0) {
			c=(category.getBlogs().size()/4);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		if (category.getBlogs().size()>(3*k+k)) {
			for (int i = (3*k+(k-4)); i < (3*k+k); i++) {
			blogList.add(category.getBlogs().get(i));
				}			
		}
		else if (category.getBlogs().size()<=(3*k+k)) {
			for (int i = (3*k+(k-4)); i < category.getBlogs().size(); i++) {
				blogList.add(category.getBlogs().get(i));
				}
		}
		int b = k;
		
		category.setBlogs(blogList);
		
		model.addAttribute("category", category);		
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blog", blog);		
		model.addAttribute("blogListLastThree", blogListLastThree);
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);	
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);	
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);
		
		return"front/blog-category-page";
	}
	
	
	@RequestMapping("/blog-tag-page")
	public String getBlogByTagPage(@RequestParam String name, Model model){
		
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();
		Blog blog = new Blog();			
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));			
		}		
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();
		Tag tagByName = tagDAO.getTagByName(name);	
		List<Blog> blogList1 = new ArrayList<Blog>();
		for (Blog blog2 : tagByName.getBlogs()) {
			if (blog2.getIsEnabled()==true) {
				blogList1.add(blog2);
			}
		}
		tagByName.setBlogs(blogList1);				
		for (Blog blog3 : tagByName.getBlogs()) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd MMMM | yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog3.getDate(), oldPattern);
			blog3.setDate(datetime.format(newPattern));	
			blog3.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog3.getId()));
		}
		
		int c = 0;
		int k = 1;
		int b = 1;
		
		if (tagByName.getBlogs().size()%4>0) {
			c=(tagByName.getBlogs().size()/4)+1;
		}
		else if (tagByName.getBlogs().size()%4==0) {
			c=(tagByName.getBlogs().size()/4);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		
		if (tagByName.getBlogs().size()>4) {
			for (int i = 0; i < 4; i++) {
			blogList.add(tagByName.getBlogs().get(i));
		}
		}
		else if (tagByName.getBlogs().size()<=4) {
			for (int i = 0; i < tagByName.getBlogs().size(); i++) {
				blogList.add(tagByName.getBlogs().get(i));
			}
		}
		
		tagByName.setBlogs(blogList);
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blog", blog);
		model.addAttribute("blogListLastThree", blogListLastThree);		
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);	
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);	
		model.addAttribute("tagByName", tagByName);	
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);
		
		return"front/blog-tag-page";
	}
	
	
	@RequestMapping("/pagination-blog-tag-page")
	public String getPaginationBlogByTagPage(@RequestParam String name, Model model, @RequestParam int k){
		
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();
		Blog blog = new Blog();			
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));			
		}		
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();
		Tag tagByName = tagDAO.getTagByName(name);	
		List<Blog> blogList1 = new ArrayList<Blog>();
		for (Blog blog2 : tagByName.getBlogs()) {
			if (blog2.getIsEnabled()==true) {
				blogList1.add(blog2);
			}
		}
		tagByName.setBlogs(blogList1);	
		for (Blog blog3 : tagByName.getBlogs()) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd MMMM | yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog3.getDate(), oldPattern);
			blog3.setDate(datetime.format(newPattern));	
			blog3.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog3.getId()));
		}
		
		int c = 0;		
		if (tagByName.getBlogs().size()%4>0) {
			c=(tagByName.getBlogs().size()/4)+1;
		}
		else if (tagByName.getBlogs().size()%4==0) {
			c=(tagByName.getBlogs().size()/4);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		if (tagByName.getBlogs().size()>(3*k+k)) {
			for (int i = (3*k+(k-4)); i < (3*k+k); i++) {
			blogList.add(tagByName.getBlogs().get(i));
				}			
		}
		else if (tagByName.getBlogs().size()<=(3*k+k)) {
			for (int i = (3*k+(k-4)); i < tagByName.getBlogs().size(); i++) {
				blogList.add(tagByName.getBlogs().get(i));
				}
		}
		int b = k;
		
		tagByName.setBlogs(blogList);			
		
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blog", blog);
		model.addAttribute("blogListLastThree", blogListLastThree);		
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);	
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);	
		model.addAttribute("tagByName", tagByName);	
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);
		
		return"front/blog-tag-page";
	}
	
	
	@RequestMapping("/blog-post-page")
	public String getBlogPostPage(@RequestParam String title, Model model){
				
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();
		Blog blog = new Blog();	
		List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();	
		for (Blog blog2 : blogListLastThree) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
			blog2.setDate(datetime.format(newPattern));			
		}
		Blog blogByTitle = blogDAO.getBlogByTitle(title);		
		blogByTitle.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blogByTitle.getId()));		
		for (Comment comment : blogByTitle.getComments()) {
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM yyyy");
			LocalDateTime datetime = LocalDateTime.parse(comment.getDate(), oldPattern);
			comment.setDate(datetime.format(newPattern));			
		}
		
		blogDAO.getBlogInkrementNumberSeen(blogByTitle.getId());
		List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
		for (Blog blog2 : blogListThreeMostVisited) {
			blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
		}
		Comment comment = new Comment();
		comment.setIdBlog(blogByTitle.getId());
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();
		Blog previousBlog = blogDAO.getPreviousBlog(blogByTitle.getId());
		Blog nextBlog = blogDAO.getNextBlog(blogByTitle.getId());
		
		model.addAttribute("categoryListInPriority", categoryListInPriority);
		model.addAttribute("blog", blog);
		model.addAttribute("blogListLastThree", blogListLastThree);
		model.addAttribute("blogByTitle", blogByTitle);
		model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);
		model.addAttribute("comment", comment);	
		model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);
		model.addAttribute("tagListOnRightSide", tagListOnRightSide);	
		model.addAttribute("previousBlog", previousBlog);	
		model.addAttribute("nextBlog", nextBlog);	
		
		return"front/blog-post-page";
	}
	
	@RequestMapping("/message-save")
	public String saveMessage(@Valid @ModelAttribute Message message, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			
			List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();		
			Blog blog = new Blog();
			List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();
			for (Blog blog2 : blogListLastThree) {
				DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
				DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
				LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
				blog2.setDate(datetime.format(newPattern));			
			}			
			List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();		
			for (Blog blog2 : blogListThreeMostVisited) {
				blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
			}			
			
			model.addAttribute("categoryListInPriority", categoryListInPriority);		
			model.addAttribute("blog", blog);
			model.addAttribute("blogListLastThree", blogListLastThree);			
			model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);	
			
			return "front/contact-page";
		}
		
		messageDAO.saveMessage(message);
		
		return "redirect: /BlogWebAppProject/index-page";
	}
	
	@RequestMapping("/comment-save")
	public String saveComment(@Valid @ModelAttribute Comment comment, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			
			List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();
			Blog blog = new Blog();	
			List<Blog> blogListLastThree = blogDAO.getBlogListWithLastThreeBlogs();	
			for (Blog blog2 : blogListLastThree) {
				DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
				DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
				LocalDateTime datetime = LocalDateTime.parse(blog2.getDate(), oldPattern);
				blog2.setDate(datetime.format(newPattern));			
			}
			Blog blogByTitle = blogDAO.getBlogWithTags(comment.getIdBlog());			
			blogByTitle.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blogByTitle.getId()));	
			for (Comment comment1 : blogByTitle.getComments()) {
				DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
				DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM yyyy");
				LocalDateTime datetime = LocalDateTime.parse(comment1.getDate(), oldPattern);
				comment1.setDate(datetime.format(newPattern));			
			}
			List<Blog> blogListThreeMostVisited = blogDAO.getThreeMostVisitedBlogPosts();
			for (Blog blog2 : blogListThreeMostVisited) {
				blog2.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog2.getId()));
			}
			List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
			List<Tag> tagListOnRightSide = tagDAO.getTagOnRightSide();
			
			model.addAttribute("categoryListInPriority", categoryListInPriority);
			model.addAttribute("blog", blog);
			model.addAttribute("blogListLastThree", blogListLastThree);
			model.addAttribute("blogByTitle", blogByTitle);
			model.addAttribute("blogListThreeMostVisited", blogListThreeMostVisited);	
			model.addAttribute("categoryListOnRightSide", categoryListOnRightSide);
			model.addAttribute("tagListOnRightSide", tagListOnRightSide);
			
			return "front/blog-post-page";
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss XXX");  
	    Date date = new Date();
	    comment.setDate(formatter.format(date));
	    
	    blogDAO.getBlogInkrementNumberComments(comment.getIdBlog());
	    
		commentDAO.saveComment(comment);
		
		return "redirect: /BlogWebAppProject/index-page";
	}

}
