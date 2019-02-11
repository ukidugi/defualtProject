package com.wan.basis;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import com.wan.basis.dto.user;


@Repository
public class UserDao {
	
	@Inject
	private SqlSession session;
	
	private static String namespace = "org.zerock.mapper.BoardMapper";
	
	public void create(User user) throws Exception{
		session.insert(namespace+".create" , user);
	
	}
	
	public user read(String username) throws Exception{
		user user = null;
		try {
			user = (user) session.selectOne(namespace+".read" , username);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return user;
	
	}
	
	public boolean createUser(String userId,String userPassword){
		user human = new user();
		human.setAge("33");
		human.setPassword(userPassword);
		human.setEnabled(true);
		human.setUsername(userId);
		
		session.insert(namespace+".usercreate",human);
		
		return true;
	}

}
