package blog.main.dao;

import java.util.List;

import blog.main.entity.Tag;

public interface TagDAO {
	
	public List<Tag> getTagList();
	
	public void saveTag(Tag tag);
	
	public Tag getTag(int id);
	
	public Tag getTagByName(String name);
	
	public void deleteTag(int id);
	
	public List<Tag> getTagsById(List<Integer> ids);
	
	public void getTagSetCount();
	
	public List<Tag> getTagOnRightSide();

	public Tag getTagWithBlogs(int id);	
	
}
