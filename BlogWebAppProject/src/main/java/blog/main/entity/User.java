package blog.main.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@Column	
	@Size(min = 5, max = 50, message = "minimum je 5, a maksimum je 50 karaktera")
	private String username;
	
	@Column
	@Size(min = 5, max = 70, message = "minimum je 5, a maksimum je 70 karaktera")
	private String password;
	
	@Column
	private boolean enabled;	
	
	@Column
	@Size(min = 5, max = 15, message = "minimum je 5, a maksimum je 15 karaktera")
	private String name;
	
	@Column
	@Size(min = 5, max = 15, message = "minimum je 5, a maksimum je 15 karaktera")
	private String surname;
	
	@Column
	@Size(min = 5, max = 300, message = "minimum je 5, a maksimum je 300 karaktera")
	private String image;	
	
	@Column
	@Size(min = 5, max = 25, message = "minimum je 5, a maksimum je 15 karaktera")
	private String phoneNumber;
	
	@Column
	@Size(min = 10, max = 50, message = "minimum je 5, a maksimum je 50 karaktera")
	private String email;
	
	@Transient
	private List<Blog> blogs;	
	
	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinTable(name="authorities", joinColumns = @JoinColumn(name="username"), inverseJoinColumns = @JoinColumn(name="authority"))
	private List<Role> authorities;	
		
	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String username, String password, boolean enabled, String name, String surname, String image,
			String phoneNumber, String email, List<Blog> blogs, List<Role> authorities) {
		super();
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.name = name;
		this.surname = surname;
		this.image = image;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.blogs = blogs;
		this.authorities = authorities;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public List<Role> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Role> authorities) {
		this.authorities = authorities;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(List<Blog> blogs) {
		this.blogs = blogs;
	}

	@Override
	public String toString() {
		return "Autorithies "+authorities.toString();
	}
	
}
