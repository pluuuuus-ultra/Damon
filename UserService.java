package com.gyf.bookstore.service;

import java.sql.SQLException;

import com.gyf.bookstore.dao.UserDao;
import com.gyf.bookstore.domain.User;
import com.gyf.bookstore.exception.UserException;
import com.gyf.bookstore.utils.SendJMail;

public class UserService {

	UserDao ud = new UserDao();
	public void register(User user)throws UserException{
		try {
			ud.add(user);
			String activeLink = "http://localhost:8080/bookstore/active?activeCode=" + user.getActiveCode();
			String html = "welcome! please click <a href=\"" + activeLink+ "\">this</a> to activate";
			SendJMail.sendMail(user.getEmail(), html);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("failed to resigter");
		}
	}
	
	/**
	 * @param activeCode 
	 */
	public void activeUser(String activeCode)throws UserException{
		try {
			User user = ud.findUserByActionCode(activeCode);
			if(user!=null){
				ud.updateActiveState(activeCode);
			}else{
				throw new UserException("activation failed");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("failed activation");
		}
	}
	
	/**
	 * login
	 */
	public User login(String username,String password)throws UserException{
		
		try {
			User user = ud.findUserByUsernameAndPassword(username, password);
			if(user == null){
				throw new UserException("incorrect usename or password ");
			}
			
			if(user.getState() == 0){
				throw new UserException("the user is nonactivated ");
			}
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException("failed to login ");
		}
	}

	/**
	 * 通过ID查找用户
	 * @param id
	 * @return
	 * @throws UserException
	 */
	public User findUserById(String id)throws UserException {
		// TODO Auto-generated method stub
		User user = null;
		try {
			user = ud.findUserById(id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException("user not found");
		}
		return user;
	}

	public void modifyUser(User user)throws UserException {

		try {
			ud.modifyUser(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException("failed to modify user");
		}
	}
}
