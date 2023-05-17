package blog.main.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import blog.main.dao.BlogDAO;
import blog.main.dao.CategoryDAO;
import blog.main.dao.SliderDAO;
import blog.main.dao.TagDAO;
import blog.main.dao.UserDAO;
import blog.main.entity.Blog;
import blog.main.entity.Category;
import blog.main.entity.Slider;
import blog.main.entity.Tag;
import blog.main.entity.User;
import blog.main.exception.NotFoundException;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
	
	
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private BlogDAO blogDAO;
	
	@Autowired
	private SliderDAO sliderDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	
	
	
	@RequestMapping("/categories")
	public List<Category> getCategories() {
		
		List<Category> list = categoryDAO.getCategoryList();	
		
		return list;		
	}
	
	@GetMapping("/categories/{id}")
	public Category getCategoryById(@PathVariable int id) {
		
		Category category = categoryDAO.getCategory(id);
		
		if (category==null) {
			throw new NotFoundException("no category for this id: "+id);
		}
		
		return category;		
	}	
	
	@PostMapping("/categories")
	public Category addCategory (@RequestBody Category category) {
		
		category.setId(0);
		
		categoryDAO.saveCategory(category);
		
		return category;
	}
	
	@PutMapping("/categories")
	public Category updateCategory (@RequestBody Category category) {
		
		categoryDAO.saveCategory(category);
		
		return category;
	}
	
	@DeleteMapping("/categories/{id}")
	public MessageResponse deleteCategory (@PathVariable int id) {
		
		Category category = categoryDAO.getCategory(id);
		
		if (category==null) {
			throw new NotFoundException("no category for this id "+id);
		}
		
		categoryDAO.deleteCategory(id);
		
		return new MessageResponse(HttpStatus.OK.value() , "Success deleted", System.currentTimeMillis());
	}
	
	
	
	@RequestMapping("/tags")
	public List<Tag> getTags() {
		
		List<Tag> list = tagDAO.getTagList();	
		
		return list;		
	}
	
	@GetMapping("/tags/{id}")
	public Tag getTagById(@PathVariable int id) {
		
		Tag tag = tagDAO.getTag(id);
		
		if (tag==null) {
			throw new NotFoundException("no tag for this id: "+id);
		}
		
		return tag;		
	}	
	
	@PostMapping("/tags")
	public Tag addTag (@RequestBody Tag tag) {
		
		tag.setId(0);
		
		tagDAO.saveTag(tag);
		
		return tag;
	}
	
	@PutMapping("/tags")
	public Tag updateTag (@RequestBody Tag tag) {
		
		tagDAO.saveTag(tag);
		
		return tag;
	}
	
	@DeleteMapping("/tags/{id}")
	public MessageResponse deleteTag (@PathVariable int id) {
		
		Tag tag = tagDAO.getTag(id);
		
		if (tag==null) {
			throw new NotFoundException("no tag for this id "+id);
		}
		
		tagDAO.deleteTag(id);
		
		return new MessageResponse(HttpStatus.OK.value() , "Success deleted", System.currentTimeMillis());
	}
	
	
	////////////////////////
	
	@RequestMapping("/blogs")
	public List<Blog> getBlogs() {
		
		List<Blog> list = blogDAO.getBlogList();
		
		return list;		
	}
	
	@GetMapping("/blogs/{id}")
	public Blog getBlogById(@PathVariable int id) {
		
		Blog blog = blogDAO.getBlog(id);
		
		if (blog==null) {
			throw new NotFoundException("no blog for this id: "+id);
		}
		
		return blog;		
	}	
	
	@PostMapping("/blogs")
	public Blog addBlog (@RequestBody Blog blog) {
		
		blog.setId(0);
		
		blogDAO.saveBlog(blog);
		
		return blog;
	}
	
	@PutMapping("/blogs")
	public Blog updateBlog (@RequestBody Blog blog) {
		
		blogDAO.saveBlog(blog);
		
		return blog;
	}
	
	@DeleteMapping("/blogs/{id}")
	public MessageResponse deleteBlog (@PathVariable int id) {
		
		Blog blog = blogDAO.getBlog(id);
		
		if (blog==null) {
			throw new NotFoundException("no blog for this id "+id);
		}
		
		blogDAO.deleteBlog(id);
		
		return new MessageResponse(HttpStatus.OK.value() , "Success deleted", System.currentTimeMillis());
	}
	
	/////////////////////////////////////////////////////////////////
	
	@RequestMapping("/sliders")
	public List<Slider> getSliders() {
		
		List<Slider> list = sliderDAO.getSliderList();
		
		return list;		
	}
	
	@GetMapping("/sliders/{id}")
	public Slider getSliderById(@PathVariable int id) {
		
		Slider slider = sliderDAO.getSlider(id);
		
		if (slider==null) {
			throw new NotFoundException("no slider for this id: "+id);
		}
		
		return slider;		
	}	
	
	@PostMapping("/sliders")
	public Slider addSlider (@RequestBody Slider slider) {
		
		slider.setId(0);
		
		sliderDAO.saveSlider(slider);
		
		return slider;
	}
	
	@PutMapping("/sliders")
	public Slider updateBlog (@RequestBody Slider slider) {
		
		sliderDAO.saveSlider(slider);
		
		return slider;
	}
	
	@DeleteMapping("/sliders/{id}")
	public MessageResponse deleteSlider (@PathVariable int id) {
		
		Slider slider = sliderDAO.getSlider(id);
		
		if (slider==null) {
			throw new NotFoundException("no slider for this id "+id);
		}
		
		sliderDAO.deleteSlider(id);
		
		return new MessageResponse(HttpStatus.OK.value() , "Success deleted", System.currentTimeMillis());
	}
	
	
	//////////////////////////////////////////////////////////////////////////////
	
	@RequestMapping("/users")
	public List<User> getUsers() {
		
		List<User> list = userDAO.getUserList();
		
		return list;		
	}
	
	@GetMapping("/users/{username}")
	public User getUserByUsername(@PathVariable String username) {
		
		User user = userDAO.getUserByName(username);
		
		if (user==null) {
			throw new NotFoundException("no user for this id: "+user);
		}
		
		return user;		
	}	
	
	@PostMapping("/users")
	public User addSlider (@RequestBody User user) {
				
		userDAO.saveUser(user);
		
		return user;
	}
	
	@PutMapping("/users")
	public User updateUser (@RequestBody User user) {
		
		userDAO.saveUser(user);
		
		return user;
	}
	
	@DeleteMapping("/users/{username}")
	public MessageResponse deleteUser (@PathVariable String username) {
		
		User user = userDAO.getUserByName(username);
		
		if (user==null) {
			throw new NotFoundException("no user for this username "+username);
		}
		
		userDAO.deleteUser(username);
		
		return new MessageResponse(HttpStatus.OK.value() , "Success deleted", System.currentTimeMillis());
	}
	
	
	

}
