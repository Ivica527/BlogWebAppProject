package blog.main.dao;

import java.util.List;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import blog.main.entity.Slider;

@Repository
public class SliderDAOImpl implements SliderDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	
	@Transactional
	@Override
	public List<Slider> getSliderList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Slider> query=session.createQuery("from Slider", Slider.class);
		
		List<Slider> sliderList = query.getResultList();		
		
		return sliderList;
	}

	@Transactional
	@Override
	public Slider getSlider(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Slider slider = session.get(Slider.class, id);
		
		return slider;
	}

	@Transactional
	@Override
	public void saveSlider(Slider slider) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(slider);

	}

	@Transactional
	@Override
	public void deleteSlider(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Slider slider = session.get(Slider.class, id);
		
		session.delete(slider);

	}
	
	
	@Transactional
	@Override
	public List<Slider> getSlidersVisibleOnMainPage() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Slider> query=session.createQuery("select s from Slider s where s.isVisible=1 order by s.numberInOrder asc", Slider.class);
		
		List<Slider> sliderList = query.getResultList();		
		
		return sliderList;
	}

	@Transactional
	@Override
	public void resetSliderList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Slider> query = session.createQuery("from Slider", Slider.class);
		
		List<Slider> sliderList = query.getResultList();
		
		for (Slider slider : sliderList) {
			slider.setIsVisible(false);
			slider.setNumberInOrder(0);
			session.saveOrUpdate(slider);
		}
		
	}
	
	@Transactional
	@Override
	public List<Slider> getReductedSliderList() {

		Session session = sessionFactory.getCurrentSession();
		
		Query<Slider> query = session.createQuery("SELECT s from Slider s where s.isVisible=1 order by s.numberInOrder ASC ", Slider.class);
		
		List<Slider> sliderList = query.getResultList();		
		
		if (sliderList.size() > 0) {
			
			for (int i = 0; i < sliderList.size(); i++) {
				sliderList.get(i).setNumberInOrder(i+1);
			}			
		}		
		return sliderList;
	}

}
