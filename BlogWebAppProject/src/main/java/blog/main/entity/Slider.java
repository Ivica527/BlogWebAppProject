package blog.main.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table
public class Slider {
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	@Size(min = 5, max = 50, message = "minimum je 5, a maksimum je 50 karaktera")
	private String title;
	
	@Column
	@Size(max = 250, message = "maksimum je 250 karaktera")
	private String image;
	
	@Column
	@Size(min = 5, max = 50, message = "minimum je 5, a maksimum je 50 karaktera")
	private String titleButton;
	
	@Column
	@Size(min = 5, max = 250, message = "minimum je 5, a maksimum je 250 karaktera")
	private String urlButton;
	
	@Column
	private int numberInOrder;
	
	@Column
	private boolean isVisible;
	
	public Slider() {
		// TODO Auto-generated constructor stub
	}

	public Slider(String title, String image, String titleButton, String urlButton) {
		super();
		this.title = title;
		this.image = image;
		this.titleButton = titleButton;
		this.urlButton = urlButton;
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

	public String getTitleButton() {
		return titleButton;
	}

	public void setTitleButton(String titleButton) {
		this.titleButton = titleButton;
	}

	public String getUrlButton() {
		return urlButton;
	}

	public void setUrlButton(String urlButton) {
		this.urlButton = urlButton;
	}

	public int getNumberInOrder() {
		return numberInOrder;
	}

	public void setNumberInOrder(int numberInOrder) {
		this.numberInOrder = numberInOrder;
	}

	public boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}	
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "number in order :"+numberInOrder+" - title :"+title;
	}
	
	
}
