package com.aic.ssm.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

public interface FrameService {

	/**
	 * @Description 获取组织机构
	 * @param menuId
	 * @param request
	 * @return
	 * @Date 2018年9月8日
	 */
	JSONArray getFrameOrgan(String menuId,HttpServletRequest request);
	
	/**
	 * @Description 获取内部组织机构（设备，巡检项）
	 * @param lastOrganGuid
	 * @param lineGuid
	 * @param request
	 * @return 
	 * @Date 2018年9月12日
	 */
	JSONArray getInnerOrgan(String lastOrganGuid,String lineGuid,HttpServletRequest request);
	
	
}
