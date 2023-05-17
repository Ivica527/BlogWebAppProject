package blog.main.dao;

import java.util.List;

import blog.main.entity.Slider;

public interface SliderDAO {
	
	public List<Slider> getSliderList();
	
	public Slider getSlider(int id);
	
	public void saveSlider(Slider slider);
	
	public void deleteSlider(int id);
	
	public List<Slider> getSlidersVisibleOnMainPage();
	
	public List<Slider> getReductedSliderList();
	
	public void resetSliderList();

}
