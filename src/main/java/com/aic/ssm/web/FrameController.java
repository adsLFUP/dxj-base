package com.aic.ssm.web;

import com.aic.ssm.entity.User;
import com.aic.ssm.service.FrameService;
import com.aic.ssm.service.UserService;
import com.aic.ssm.service.impl.FrameServiceImpl;

import net.sf.json.JSONArray;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/frame")
public class FrameController  {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FrameService frameService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, Integer offset, Integer limit) {
		LOG.info("invoke----------/user/list");
		offset = offset == null ? 0 : offset;//默认便宜0
		limit = limit == null ? 50 : limit;//默认展示50条
		List<User> list = userService.getUserList(offset, limit);
		model.addAttribute("userlist", list);
		return "frame/mainframe";
	}
	@RequestMapping(value = "/showMain", method = RequestMethod.GET)
	public String showMain(Model model,HttpServletRequest request) {
		
		LOG.info("invoke----------/frame/showMain");
		JSONArray frameOrgan = frameService.getFrameOrgan("queryAllDataOfDJ", request);
		String string = frameOrgan.toString();
		String replaceAll = string.replaceAll("nodes", "children");
		frameOrgan = JSONArray.fromObject(replaceAll);
		model.addAttribute("frameOrgan", frameOrgan);
		return "frame/mainframe";
	}
	
	@RequestMapping(value = "/getInnerOrgan", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getInnerOrgan(Model model,HttpServletRequest request) {
		
		LOG.info("invoke----------/frame/getInnerOrgan");
		String lastOrganGuid = request.getParameter("lastOrganGuid");
		String lineGuid = request.getParameter("lineGuid");
		JSONArray frameOrgan = frameService.getInnerOrgan(lastOrganGuid, lineGuid, request);
		
		String string = frameOrgan.toString();
		String replaceAll = string.replaceAll("nodes", "children");
		frameOrgan = JSONArray.fromObject(replaceAll);
		model.addAttribute("frameOrgan", frameOrgan);
		System.out.println(frameOrgan);
		return frameOrgan;
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String testMain(Model model,HttpServletRequest request) {
		
		LOG.info("invoke----------/frame/showMain");
		return "frame/testaddnodesframe";
	}

}
