package com.aic.ssm.service;

import java.util.List;

import com.aic.ssm.entity.User;

public interface UserService {

	List<User> getUserList(int offset, int limit);
	 
}
