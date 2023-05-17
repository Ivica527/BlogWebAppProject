package blog.main.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table
public class Blog {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	@Size(min = 20, max= 255, message = "minimum je 20, a maksimum je 255 karaktera")
	private String title;
	
	@Column
	@Size(max= 250, message = "maksimum je 250 karaktera")
	private String image;
	
	@Column
	@Size(min = 50, max= 500, message = "minimum je 50, a maksimum je 500 karaktera")
	private String description;
	
	@Column
	@Size(min = 5, max= 500, message = "minimum je 5, a maksimum je 500 karaktera")
	private String text;
	
	@Column
	private int numberSeen;
	
	@Column
	private int numberComments;
	
	@Column
	private String date;	
	
	@Column
	private boolean isImportant;
	
	@Column
	private boolean isEnabled;
	
	@Transient
	private String sinceThen;	
	
	@Column
	private String username;
	
	@Transient
	private User user;	
	
	@JoinColumn(name = "idCategory")
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private Category category;
	
	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name="Blog_Tag", joinColumns = @JoinColumn(name="id_blog"), inverseJoinColumns = @JoinColumn(name="id_tag"))
	private List<Tag> tags;	
	
	@OneToMany(mappedBy = "idBlog")
	private List<Comment> comments;
	
	public Blog() {
		// TODO Auto-generated constructor stub
	}	


	public Blog(@Size(min = 20, max = 255, message = "minimum je 20, a maksimum je 255 karaktera") String title,
			@Size(max = 250, message = "maksimum je 250 karaktera") String image,
			@Size(min = 50, max = 500, message = "minimum je 50, a maksimum je 500 karaktera") String description,
			@Size(min = 5, max = 500, message = "minimum je 5, a maksimum je 500 karaktera") String text,
			int numberSeen, int numberComments, String date, boolean isImportant, boolean isEnabled, String sinceThen,
			String username, User user, Category category, List<Tag> tags, List<Comment> comments) {
		super();
		this.title = title;
		this.image = image;
		this.description = description;
		this.text = text;
		this.numberSeen = numberSeen;
		this.numberComments = numberComments;
		this.date = date;
		this.isImportant = isImportant;
		this.isEnabled = isEnabled;
		this.sinceThen = sinceThen;
		this.username = username;
		this.user = user;
		this.category = category;
		this.tags = tags;
		this.comments = comments;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public int getNumberSeen() {
		return numberSeen;
	}

	public void setNumberSeen(int numberSeen) {
		this.numberSeen = numberSeen;
	}

	public int getNumberComments() {
		return numberComments;
	}

	public void setNumberComments(int numberComments) {
		this.numberComments = numberComments;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public boolean getIsImportant() {
		return isImportant;
	}

	public void setIsImportant(boolean isImportant) {
		this.isImportant = isImportant;
	}
	
	public boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public String getSinceThen() {
		return sinceThen;
	}

	public void setSinceThen(String sinceThen) {
		this.sinceThen = sinceThen;
	}
		
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	@Override
	public String toString() {		
		return "blog - "+title;
	}
	
}
