package blog.main.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table
public class Category {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	@Size(min=2,max = 25,message = "minimum je 2, a maksimum je 25 karaktera")
	private String name;
	
	@Column
	@Size(min=50,max = 500,message = "minimum je 50, a maksimum je 500 karaktera")
	private String description;
	
	@Column
	private int numberInPriority;
	
	@Column
	private boolean isVisible;
	
	@Column
	private int count;	
	
	@Transient
	private List<Blog> blogs;
	
	
	public Category() {
		// TODO Auto-generated constructor stub
	}	

	public Category(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getNumberInPriority() {
		return numberInPriority;
	}

	public void setNumberInPriority(int numberInPriority) {
		this.numberInPriority = numberInPriority;
	}

	public boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public long getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}	
	
	public List<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}

	@Override
	public String toString() {		
		return "("+id+") - "+name ;
	}
	
}
