package com.aic.ssm.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.aic.ssm.entity.User;

public interface TestService {

	JSONArray getFrameOrgan(String menuId,HttpServletRequest request);
	
}
