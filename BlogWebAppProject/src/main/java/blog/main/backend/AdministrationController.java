package blog.main.backend;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import blog.main.entity.ChangePassword;
import blog.main.entity.Slider;
import blog.main.entity.Tag;
import blog.main.entity.User;

@Controller
@RequestMapping("/administration")
public class AdministrationController {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private BlogDAO blogDAO;
	
	@Autowired
	private SliderDAO sliderDAO;
	
	@Autowired
	private MessageDAO messageDAO;
	
	@Autowired
	private CommentDAO commentDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	
	
	@RequestMapping("/category-list")
	public String getCategoryList(Model model, Principal principal) {
		
		List<Category> list = categoryDAO.getCategoryList();
		List<Category> categoryListOnRightSide = categoryDAO.getCategoryOnRightSide();
		
		model.addAttribute("categoryList", list);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount() );
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount() );		
		model.addAttribute("categoryListOnRightSide",categoryListOnRightSide);	
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "category-list";
	}
	
	@RequestMapping("/category-form")
	public String getCategoryForm(Model model, Principal principal) {
		
		Category category = new Category();
		
		model.addAttribute("category",category);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "category-form";
	}
	
	@RequestMapping("/category-save")
	public String saveCategory(@Valid @ModelAttribute Category category, BindingResult result, Model model, Principal principal) {
		
		List<Category> categoryListInPriority = categoryDAO.getCategoryInPriority();		
		System.out.println("Lista: "+categoryListInPriority.toString());	
		
		if (category.getIsVisible()==true) {
			
			if (categoryListInPriority.size()==0) {
				category.setNumberInPriority(1);
			}
			else if (categoryListInPriority.size()>0) {
				category.setNumberInPriority(categoryListInPriority.get(categoryListInPriority.size()-1).getNumberInPriority()+1);
			}
					
		}
		else if (category.getIsVisible()==false) {			
			category.setNumberInPriority(0);			
		}
		
		if (result.hasErrors()) {
			model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
			model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
			model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
			return "category-form";
		}
		
		categoryDAO.saveCategory(category);		
				
		return "redirect:/administration/category-list";
	}
	
	@RequestMapping("/category-form-update")
	public String getCategoryFormUpdate(@RequestParam int id, Model model, Principal principal) {
		
		Category category = categoryDAO.getCategory(id);
		
		model.addAttribute("category", category);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());	
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "category-form";
	}
	
	@RequestMapping("/category-delete")
	public String deleteCategory(@RequestParam int id) {
		
		categoryDAO.deleteCategory(id);	
		
		return "redirect:/administration/category-list";
	}
	
	@RequestMapping("/category-isVisible")
	public String setCategoryVisible(@RequestParam int id, Model model) {
		
		List<Category> categoryList = categoryDAO.getCategoryOnRightSide();		
	
		Category category = categoryDAO.getCategory(id);
		
		if (category.getIsVisible()==false) {
			
			if (categoryList.size()==0) {
				category.setNumberInPriority(1);
				category.setIsVisible(true);
			}
			else if (categoryList.size()>0 && categoryList.size()<5) {
				category.setNumberInPriority(categoryList.size()+1);
				category.setIsVisible(true);
			}
			else if (categoryList.size()==5) {
				
			}
			categoryDAO.saveCategory(category);							
		}
		else if (category.getIsVisible()==true) {			
				category.setIsVisible(false);
				category.setNumberInPriority(0);
				categoryDAO.saveCategory(category);	
				categoryDAO.getReductedCategoryList();	
			}  
			
		return "redirect:/administration/category-list";
	
	}
	
	@RequestMapping("/category-list-reset")
	public String resetCategoryList(Model model) {
		
		categoryDAO.resetCategoryList();	
		
		return "redirect:/administration/category-list";
	
	}
	
	
	@RequestMapping("/tag-list")
	public String getTagList(Model model, Principal principal) {
		
		List<Tag> tagList = tagDAO.getTagList();
		
		model.addAttribute("tagList", tagList);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "tag-list";
	}
	
	@RequestMapping("/tag-form")
	public String getTagForm(Model model, Principal principal) {
		
		Tag tag = new Tag();
		
		model.addAttribute("tag", tag);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "tag-form";
	}
	
	@RequestMapping("/tag-save")
	public String saveTag(@Valid @ModelAttribute Tag tag, BindingResult result, Principal principal, Model model) {
		
		if (result.hasErrors()) {
			model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
			model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
			model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
			return "tag-form";
		}
		
		tagDAO.saveTag(tag);		
		
		return "redirect:/administration/tag-list";
	}
	
	@RequestMapping("/tag-form-update")
	public String getTagFormUpdate(@RequestParam int id, Model model, Principal principal) {
		
		Tag tag = tagDAO.getTag(id);
		
		model.addAttribute("tag", tag);	
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "tag-form";
	}
	
	@RequestMapping("/tag-delete")
	public String deleteTag(@RequestParam int id) {
		
		tagDAO.deleteTag(id);		
		
		return "redirect:/administration/tag-list";
	}
	
	@RequestMapping({"/", "/blog-list"})
	public String getBlogList(Model model, Principal principal) {
		
		List<Blog> blogList = blogDAO.getBlogList();
		
		model.addAttribute("blogList", blogList);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "blog-list";
	}
	
	@RequestMapping("/blog-form")
	public String getBlogForm(Model model, Principal principal) {
		
		Blog blog = new Blog();
		
		List<Category> categoryList = categoryDAO.getCategoryList();
		List<Tag> tagList = tagDAO.getTagList();
		
		model.addAttribute("blog", blog);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("tagList", tagList);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "blog-form";
	}
	
	@RequestMapping("/blog-save")
	public String saveBlog(@Valid @ModelAttribute Blog blog, BindingResult result, Model model, Principal principal) {		    
		
		if (result.hasErrors()) {
			List<Category> categoryList = categoryDAO.getCategoryList();
			List<Tag> tagList = tagDAO.getTagList();			
			
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("tagList", tagList);
			model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
			model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
			model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
			return "blog-form";
		}
		
		Category category = categoryDAO.getCategory(blog.getCategory().getId());
		
		List<Integer> ids = new ArrayList<Integer>();
		
		for (Tag tag : blog.getTags()) {
			ids.add(Integer.parseInt(tag.getName()));
		}
		
		List<Tag> tags = tagDAO.getTagsById(ids);
		
		blog.setCategory(category);
		blog.setTags(tags);		
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss XXX");  
	    Date date = new Date();
	    blog.setDate(formatter.format(date));
	    blog.setUsername(principal.getName());
		
		blogDAO.saveBlog(blog);		
		
		return "redirect:/administration/blog-list";
	}
	
	@RequestMapping("/blog-form-update")
	public String getBlogUpdateForm(@RequestParam int id, Model model, Principal principal) {
		
		Blog blog = blogDAO.getBlogWithTags(id);
		List<Category> categoryList = categoryDAO.getCategoryList();
		List<Tag> tagList = tagDAO.getTagList();
		
		model.addAttribute("blog", blog);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("tagList", tagList);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "blog-form";
	}
	
	@RequestMapping("/blog-delete")
	public String deleteBlog(@RequestParam int id) {
		
		blogDAO.deleteBlog(id);
		
		return "redirect:/administration/blog-list";
	}
	
	@RequestMapping("/blog-isImportant")
	public String setImportantBlog(@RequestParam int id) {
		
		blogDAO.getBlogImportant(id);
		
		return "redirect:/administration/blog-list";
	}
	
	@RequestMapping("/blog-isEnabled")
	public String setEnabledBlog(@RequestParam int id) {
		
		blogDAO.getBlogEnabled(id);
		
		return "redirect:/administration/blog-list";
	}
	
	@RequestMapping("/blogs-page-order")
	public String getBlogsListOrderBy(@RequestParam int id, Model model) {
		
		model.addAttribute("blogList", blogDAO.getBlogsListOrderBy(id));
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount() );
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount() );		
		
		return "blog-list";
	}
	
	
	@RequestMapping("/slider-list")
	public String getSliderList(Model model, Principal principal) {
		
		List<Slider> sliderList = sliderDAO.getSliderList();
		List<Slider> sliderList0 = sliderDAO.getSlidersVisibleOnMainPage();
		
		model.addAttribute("sliderList",sliderList);
		model.addAttribute("sliderList0",sliderList0);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());	
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "slider-list";
	}
	
	@RequestMapping("/slider-form")
	public String getSliderForm(Model model, Principal principal) {						
		
		Slider slider = new Slider();
		
		model.addAttribute("slider", slider);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "slider-form";		
	}
	
	@RequestMapping("/slider-save")
	public String saveSlider(@Valid @ModelAttribute Slider slider, BindingResult result, Model model, Principal principal) {
		
		List<Slider> sliderList = sliderDAO.getSlidersVisibleOnMainPage();		
		System.out.println("Lista: "+sliderList.toString());	
		
		if (slider.getIsVisible()==true) {
			
			if (sliderList.size()==0) {
				slider.setNumberInOrder(1);
			}
			else if (sliderList.size()>0) {
				slider.setNumberInOrder(sliderList.get(sliderList.size()-1).getNumberInOrder()+1);
			}					
		}
		else if (slider.getIsVisible()==false) {			
				slider.setNumberInOrder(0);			
		}		
		
		if (result.hasErrors()) {
			model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
			model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
			model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
			return "slider-form";
		}
		
		sliderDAO.saveSlider(slider);		
		
		return "redirect:/administration/slider-list";
	}
	
	@RequestMapping("/slider-delete")
	public String deleteSlider(@RequestParam int id) {
		
		sliderDAO.deleteSlider(id);		
		
		return "redirect:/administration/slider-list";
	}
	
	@RequestMapping("/slider-form-update")
	public String getSliderFormUpdate(@RequestParam int id, Model model, Principal principal) {				
		
		Slider slider = sliderDAO.getSlider(id);
		
		model.addAttribute("slider", slider);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));		
		
		return "slider-form";		
	}
	
	
	@RequestMapping("/slider-isVisible")
	public String setSliderVisible(@RequestParam int id, Model model) {
		
		List<Slider> sliderList = sliderDAO.getSlidersVisibleOnMainPage();		
		
		Slider slider = sliderDAO.getSlider(id);
		
		if (slider.getIsVisible()==false) {
			
			if (sliderList.size()==0) {
				slider.setNumberInOrder(1);
				slider.setIsVisible(true);
			}
			else if (sliderList.size()>0) {
				slider.setNumberInOrder(sliderList.size()+1);
				slider.setIsVisible(true);
			}
			
			sliderDAO.saveSlider(slider);							
		}
		else if (slider.getIsVisible()==true) {		
			slider.setIsVisible(false);
			slider.setNumberInOrder(0);
			sliderDAO.saveSlider(slider);
			sliderDAO.getReductedSliderList();
			}
		
		return "redirect:/administration/slider-list";
	
	}
	
	@RequestMapping("/slider-list-reset")
	public String resetSliderList(Model model) {
		
		sliderDAO.resetSliderList();
		
		List<Slider> sliderList = sliderDAO.getSliderList();
		
		model.addAttribute("sliderList",sliderList);
		
		return "redirect:/administration/slider-list";
	
	}
	@RequestMapping("/message-list")
	public String getMessageList(Model model, Principal principal) {
		
		model.addAttribute("messageList",messageDAO.getMessageList() );
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "/message-list";
	}
	
	@RequestMapping("/message-delete")
	public String deleteMessage(@RequestParam int id) {
		
		messageDAO.deleteMessage(id);
		
		return "redirect:/administration/message-list";
	}
	
	@RequestMapping("/message-isSeen")
	public String getMessageIsSeen(@RequestParam int id) {
		
		messageDAO.getMessageIsSeen(id);
		
		return "redirect:/administration/message-list";
	}	

	@RequestMapping("/comment-list")
	public String getCommnetList(Model model, Principal principal) {
		
		model.addAttribute("commentList",commentDAO.getCommentListSortByDate());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());		
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		
		return "/comment-list";
	}
	
	@RequestMapping("/comment-delete")
	public String deleteComment(@RequestParam int id) {
		
		blogDAO.getBlogDekrementNumberComments(commentDAO.getComment(id).getIdBlog());
		
		commentDAO.deleteComment(id);
		
		return "redirect:/administration/comment-list";
	}
	
	@RequestMapping("/comment-isEnabled")
	public String getCommentEnabled(@RequestParam int id) {
		
		commentDAO.getCommentEnabled(id);
		
		return "redirect:/administration/comment-list";
	}
	
	@RequestMapping("/comment-isSeen")
	public String getCommentIsSeen(@RequestParam int id) {
		
		commentDAO.getCommentIsSeen(id);
		
		return "redirect:/administration/comment-list";
	}
		
	@RequestMapping("/user-list")
	public String getUserList(Model model, Principal principal) {		
			
		model.addAttribute("userList",userDAO.getUserList());
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByUsername(principal.getName()));		
		
		return "user-list";
	}
	
	@RequestMapping("/user-form")
	public String getUserForm(Model model, Principal principal) {		
			
		model.addAttribute("user",new User());	
		model.addAttribute("roles", userDAO.getRoleList());
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		model.addAttribute("user1",userDAO.getUserByUsername(principal.getName()));
		
		return "user-form";
	}
	
	
	@RequestMapping("/user-enabled")
	public String getUserEnabled(@RequestParam String username) {
		
		userDAO.enableUser(username);
		
		return "redirect:/administration/user-list";
	}
	
	
	@RequestMapping("/user-save")
	public String saveUser(@Valid @ModelAttribute User user, BindingResult result, Principal principal, Model model) {
		
		if (result.hasErrors()) {
			
			model.addAttribute("roles", userDAO.getRoleList());
			model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
			model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
			model.addAttribute("user1",userDAO.getUserByUsername(principal.getName()));			
			
			return "user-form";
		}
		
		String username = user.getUsername();
		char [] niz = null;		
		if (username.startsWith(",")==true) {
			niz = new char [username.length()-1];
			for (int i = 1; i < username.length(); i++) {			// OVO SAM MORAO DA NAPRAVIM JER MI JE PROGRAM SAM
				niz[i-1] = username.charAt(i);						// NE ZNAM KAKO DODAVAO ZAPETU ISPRED USERNAME-A  
			}														// NA PRIMER KADA UPISEM IVICA ON UPISE ,IVICA
		}		
		String newUsername = new String(niz);		
		user.setUsername(newUsername);		
		
		String passwordEncode = new BCryptPasswordEncoder().encode(user.getPassword());
		
		user.setPassword("{bcrypt}"+passwordEncode);
		
		user.setEnabled(false);		
		
		System.out.println(user.getAuthorities().toString());		
		
		userDAO.saveUser(user);
		
		return "redirect:/administration/user-list";
	}
	
	@RequestMapping("/user-delete")
	public String deleteUser(@RequestParam String username) {
		
		userDAO.deleteUser(username);
		
		return "redirect:/administration/user-list";
	}
	
	
	@RequestMapping("/user-edit-profile")
	public String getUserEditProfile(Principal principal, Model model) {
		
		User user = userDAO.getUserByUsername(principal.getName());	
		
		System.out.println(principal.getName());		
		
		model.addAttribute("user", user);
		model.addAttribute("user1", user);
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		
		return "user-edit-profile";
	}
	
	@RequestMapping("/user-edit")
	public String getUserEdit(@ModelAttribute User user) {
		
		userDAO.saveUser(user);		
		
		return "redirect:/administration/user-list";
	}
	
	@RequestMapping("/information-about-author")
	public String getAuthorPage(@RequestParam String username, Model model, Principal principal) {	
		
		User user = userDAO.getUserByUsername(username);		
		for (Blog blog : user.getBlogs()) {
			blog.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog.getId()));
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog.getDate(), oldPattern);			
			blog.setDate(datetime.format(newPattern));
		}
		
		int c = 0;
		int k = 1;
		int b = 1;

		
		if (user.getBlogs().size()%3>0) {
			c=(user.getBlogs().size()/3)+1;
		}
		else if (user.getBlogs().size()%3==0) {
			c=(user.getBlogs().size()/3);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		
		
		if (user.getBlogs().size()>3) {
			for (int i = 0; i < 3; i++) {
			blogList.add(user.getBlogs().get(i));
		}
		}
		else if (user.getBlogs().size()<=3) {
			for (int i = 0; i < user.getBlogs().size(); i++) {
				blogList.add(user.getBlogs().get(i));
			}
		}
				
		user.setBlogs(blogList);			
		
		model.addAttribute("user", user);
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("b", b);	
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		
		return "information-about-author";
	}
	
	
	@RequestMapping("/user-change-password")
	public String getUserChangePassword(Model model, Principal principal) {		
		
		model.addAttribute("changePassword", new ChangePassword());
		model.addAttribute("user1", userDAO.getUserByUsername(principal.getName()));
		
		return "user-change-password";
	}
	
	@RequestMapping("/user-change-password-action")
	public String getUserChangePasswordAction(@ModelAttribute ChangePassword changePassword, Principal principal, Model model) {		
		
		if (changePassword.getNewpassword().equalsIgnoreCase(changePassword.getConfirmpassword())) {			
			
			User user = userDAO.getUserByUsername(principal.getName());	
			
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			
			if (encoder.matches(changePassword.getOldpassword(), user.getPassword().replace("{bcrypt}", ""))) {				
				
				user.setPassword("{bcrypt}"+encoder.encode(changePassword.getNewpassword()));
				
				userDAO.saveUser(user);
			}
			else {
				
				String message1="The old password is incorect";
				model.addAttribute("message1", message1);
				model.addAttribute("user1", userDAO.getUserByUsername(principal.getName()));
				return "user-change-password";
			}
			
		}
		else {
			String message2="The new password and confirm password is not the same";
			model.addAttribute("message2", message2);
			model.addAttribute("user1", userDAO.getUserByUsername(principal.getName()));
			return "user-change-password";
		}
		
		
		return "redirect:/administration/user-list";
	}
	
	
	
	@RequestMapping("/pagination-author-page")
	public String getPaginationAuthorPage(@RequestParam String username, Model model,@RequestParam int k, Principal principal) {	
		
		User user = userDAO.getUserByUsername(username);		
		for (Blog blog : user.getBlogs()) {
			blog.setComments(commentDAO.getBlogCommentListSortByDateEnabled(blog.getId()));
			DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss XXX");
			DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
			LocalDateTime datetime = LocalDateTime.parse(blog.getDate(), oldPattern);			
			blog.setDate(datetime.format(newPattern));
		}
		
		int c = 0;		
		if (user.getBlogs().size()%3>0) {
			c=(user.getBlogs().size()/3)+1;
		}
		else if (user.getBlogs().size()%3==0) {
			c=(user.getBlogs().size()/3);
		}
		
		List<Blog> blogList = new ArrayList<Blog>();
		if (user.getBlogs().size()>(2*k+(k-1))) {
			for (int i = (2*k+(k-3)); i < (2*k+k); i++) {
			blogList.add(user.getBlogs().get(i));
				}			
		}
		else if (user.getBlogs().size()<=(2*k+(k-1))) {
			for (int i = (2*k+(k-3)); i < user.getBlogs().size(); i++) {
				blogList.add(user.getBlogs().get(i));
				}
		}
		int b = k;
		
		user.setBlogs(blogList);			
		
		model.addAttribute("user", user);
		model.addAttribute("b", b);
		model.addAttribute("c", c);
		model.addAttribute("k", k);
		model.addAttribute("user1",userDAO.getUserByName(principal.getName()));
		model.addAttribute("messageCount",messageDAO.getUnreadMessageCount());
		model.addAttribute("commentCount",commentDAO.getUnseenCommentsCount());
		
		return "information-about-author";
	}
	
	
	
	
	
	
	
	
	
	
	
		
	
}
