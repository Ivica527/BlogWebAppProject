package blog.main.dao;

import java.util.List;

import blog.main.entity.Message;

public interface MessageDAO {
	
	public List<Message> getMessageList();
	
	public void saveMessage(Message message);
	
	public Message getMessage(int id);
	
	public void deleteMessage(int id);	
	
	public long getUnreadMessageCount();
	
	public void getMessageIsSeen(int id);

}
