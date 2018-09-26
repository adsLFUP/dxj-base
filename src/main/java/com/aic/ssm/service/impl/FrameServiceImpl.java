package com.aic.ssm.service.impl;


import com.aic.ssm.cache.RedisCache;
import com.aic.ssm.dao.GoodsDao;
import com.aic.ssm.dao.OrderDao;
import com.aic.ssm.dao.UserDao;
import com.aic.ssm.entity.Goods;
import com.aic.ssm.entity.SidebarTree;
import com.aic.ssm.entity.User;
import com.aic.ssm.enums.ResultEnum;
import com.aic.ssm.exception.BizException;
import com.aic.ssm.service.FrameService;
import com.aic.ssm.service.GoodsService;
import com.aic.ssm.util.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class FrameServiceImpl  implements FrameService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RedisCache cache;

	@Resource(name="sqlSessionFactory")
	private SqlSessionFactory sqlMapClient;
	
	public void setSqlMapClient(SqlSessionFactory sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	@Override
	public JSONArray getFrameOrgan(String menuId, HttpServletRequest request) {
		
		//获取组织机构信息
		String cache_key = RedisCache.CAHCENAME + "|queryXJOrgan|";
		String sql = "xjOrgan.queryXJOrgan";
	    List result_cache = this.getCacheOrDB(cache_key,sql,null,SidebarTree.class,true);
		
		//获取组织机构下各信息
		JSONArray menuJA = this.queryOrganMenu(result_cache,menuId,request);
		return menuJA;
	}
	
	/**
	 * @Description 
	 * @param cache_key 缓存主键key
	 * @param sql 执行的sql语句
	 * @param dataMap 上送的参数Map
	 * @param Class 返回的实体类
	 * @return
	 * @Date 2018年9月8日
	 */
	public List getCacheOrDB(String cache_key,String sql,Map dataMap,Class entityTable,Boolean useCache) {
		List result_cache = null;
		if(useCache){
			result_cache = cache.getListCache(cache_key, entityTable);
		}
		if (result_cache != null) {
			LOG.info("get cache with key:" + cache_key);
		} else {
			// 缓存中没有再去数据库取，并插入缓存（缓存时间为60秒）
			SqlSession openSession = sqlMapClient.openSession();
			result_cache = openSession.selectList(sql,dataMap);
			if(useCache){
				cache.putListCacheWithExpireTime(cache_key, result_cache, RedisCache.CAHCETIME);
				LOG.info("put cache with key:" + cache_key);
			}
			openSession.close();
		}
		return result_cache;
	}
	
	/**
	 * @Description 获取组织机构下的线路，站点，设备，巡检项等信息组成组织机构
	 * @param list 组织机构list
	 * @param menuId 当前菜单的ID
	 * @param request http请求信息
	 * @return 返回组装好的组织机构JSONArray
	 * @Date 2018年9月8日
	 */
	private JSONArray queryOrganMenu(List list,String menuId,HttpServletRequest request) {
		
		//查询组织机构
		SidebarTree root=new SidebarTree();
        SidebarTree node=new SidebarTree();
        List<SidebarTree> treelist=new CopyOnWriteArrayList(); //拼凑好的Json数据  
        List<SidebarTree> parentNodes=new CopyOnWriteArrayList(); // 存放所有父节点 
		if(list!=null && list.size()>0){
			for (int k = 0; k < list.size(); k++) {
				if("00000000-0000-0000-0000-000000000000".equals(((SidebarTree)list.get(k)).getParent_guid())){
						root=(SidebarTree) list.get(k); //第一个一定是根节点 0 
						for(int i=1; i<list.size(); i++){
							node=(SidebarTree) list.get(i);
							if(!Util.isNullOrEmpty(node.getParent_guid())){
								if(node.getParent_guid().equals(root.getGuid())){ //从根节点开始遍历是不是子节点  
									parentNodes.add(node);
									root.getNodes().add(node);
								}else{ //获取root子节点的孩子节点  
									getChildrenNodes(parentNodes, node);
									parentNodes.add(node);
								}
							}
						}
						treelist.add(root);
				}
			}
		}


		//查询组织机构下的线路，设备，检测项
		for (int i = 0; i < treelist.size(); i++) {
			SidebarTree oneOrgan = treelist.get(i);
			List<SidebarTree> twoNodesList = oneOrgan.getNodes();
			if(!Util.isNullOrEmpty(twoNodesList) && twoNodesList.size()>0){
				for (int j = 0; j < twoNodesList.size(); j++) {
					SidebarTree twoOrgan = twoNodesList.get(i);
					List<SidebarTree> threeNodesList = twoOrgan.getNodes();
					if(!Util.isNullOrEmpty(threeNodesList) && threeNodesList.size()>0){
						for (int k = 0; k < threeNodesList.size(); k++) {//添加组织机构下的的子组织机构线路
							SidebarTree threeOrgan = threeNodesList.get(k);
							String threeGuid = threeOrgan.getGuid();
							SidebarTree organOfLine = this.getOrganOfLine(threeGuid,threeOrgan);
							threeNodesList.set(k, organOfLine);
							organOfLine = null;
//							List<SidebarTree> lineNodesList = threeNodesList.get(k).getNodes();
//							for (int l = 0; l < lineNodesList.size(); l++) {//添加线路的子组织机构设备
//								SidebarTree oneLine = lineNodesList.get(l);
//								oneLine.getNodes().add(new SidebarTree());
//								String t_line_r_guid = oneLine.getT_line_r_guid();
//								SidebarTree organOfDevice = this.getOrganOfDevice(threeGuid,t_line_r_guid,oneLine);
//								lineNodesList.set(l, organOfDevice);
//								organOfDevice = null;
//								List<SidebarTree> deviceNodesList = lineNodesList.get(l).getNodes();
//								for (int m = 0; m < deviceNodesList.size(); m++) {//添加设备的子组织机构巡检项
//									SidebarTree oneDevice = deviceNodesList.get(m);
//									String t_device_r_guid = oneDevice.getT_device_r_guid();
//									SidebarTree organOfItem = this.getOrganOfItem(threeGuid,t_line_r_guid,t_device_r_guid,oneDevice);
//									deviceNodesList.set(m, organOfItem);
//									organOfItem = null;
//								}
//							}
						}
					}
				}
			}
		}
		Boolean isOrgan = true;
		JSONArray lastValue = this.getLastValue(JSONArray.fromObject(treelist), isOrgan,request);
		return lastValue;
	}

	/**
	 * @Description 获取组织机构下的线路
	 * @param threeGuid 当前三级组织机构下的guid
	 * @param threeOrgan 三级组织机构
	 * @return
	 * @Date 2018年9月8日
	 */
	private SidebarTree getOrganOfLine(String threeGuid,SidebarTree threeOrgan) {

		Map<String, Object> dataMap = new HashMap();
		dataMap.put("t_organization_r_guid", threeGuid);
		String cache_key = RedisCache.CAHCENAME + "|queryXJLineByOrgan|";
		String sql = "xjOrgan.queryXJLineByOrgan";
	    List lineList = this.getCacheOrDB(cache_key,sql,dataMap,SidebarTree.class,false);
		
		SidebarTree queryOrganMenuForLine = this.queryOrganMenuForLine(lineList, null,null,threeOrgan);
		lineList.clear();
		return queryOrganMenuForLine;
		
	}
	private SidebarTree getOrganOfItem(String threeGuid,String t_line_r_guid,String t_device_r_guid,SidebarTree deviceOrgan) {
		
		Map<String, Object> dataMap = new HashMap();
		dataMap.put("t_organization_r_guid", threeGuid);
		dataMap.put("t_line_r_guid", t_line_r_guid);
		dataMap.put("t_device_r_guid", t_device_r_guid);
		String cache_key = RedisCache.CAHCENAME + "|queryXJItemByDevice|";
		String sql = "xjOrgan.queryXJItemByDevice";
		List<SidebarTree> itemList = this.getCacheOrDB(cache_key,sql,dataMap,SidebarTree.class,false);
		
		SidebarTree queryOrganMenuForLine = queryOrganMenuForItem(null, null,itemList,deviceOrgan);
		itemList.clear();
		return queryOrganMenuForLine;
		
	}
	
	private SidebarTree getOrganOfDevice(String t_organization_r_guid,String t_line_r_guid,SidebarTree lineOrgan) {
		Map<String, Object> dataMap = new HashMap();
		dataMap.put("t_organization_r_guid", t_organization_r_guid);
		dataMap.put("t_line_r_guid", t_line_r_guid);
		String cache_key = RedisCache.CAHCENAME + "|queryXJDeviceByLine|";
		String sql = "xjOrgan.queryXJDeviceByLine";
		List<SidebarTree> deviceList = this.getCacheOrDB(cache_key,sql,dataMap,SidebarTree.class,false);
		
		SidebarTree queryOrganMenuForLine = queryOrganMenuForDevice(null, deviceList,null,lineOrgan);
		deviceList.clear();
		return queryOrganMenuForLine;
		
	}

	/**组装机构下的线路，设备，检测项
	 * @param allList
	 * @param lineList
	 * @return
	 */
	private SidebarTree queryOrganMenuForItem(List<SidebarTree> lineList,List<SidebarTree> deviceList,List<SidebarTree> itemList,SidebarTree deviceOrgan) {
		
		
		//查询组织机构
		SidebarTree rootPart=new SidebarTree();
		SidebarTree nodePart=new SidebarTree();
		List<SidebarTree> parentNodesPart=new CopyOnWriteArrayList(); // 存放所有父节点 
		rootPart = deviceOrgan;
		if(itemList!=null && itemList.size()>0){
			for(int i=0; i<itemList.size(); i++){
				nodePart= itemList.get(i);
				if(!Util.isNullOrEmpty(nodePart.getT_organization_r_guid())){
					if(nodePart.getT_device_r_guid().equals(rootPart.getT_device_r_guid()) && nodePart.getT_line_r_guid().equals(rootPart.getT_line_r_guid()) && nodePart.getT_organization_r_guid().equals(rootPart.getT_organization_r_guid())){ //从根节点开始遍历是不是子节点  
						nodePart.setLevel("item");
						parentNodesPart.add(nodePart);
						rootPart.getNodes().add(nodePart);
					}else{ //获取root子节点的孩子节点
						getChildrenNodesForLine(parentNodesPart, nodePart);
						parentNodesPart.add(nodePart);
					}
				}
			}
		}
		return rootPart;
	}
	/**组装机构下的线路，设备，检测项
	 * @param allList
	 * @param lineList
	 * @return
	 */
	private SidebarTree queryOrganMenuForDevice(List<SidebarTree> lineList,List<SidebarTree> deviceList,List<SidebarTree> itemList,SidebarTree threeOrgan) {
		
		
		//查询组织机构
		SidebarTree rootPart=new SidebarTree();
		SidebarTree nodePart=new SidebarTree();
		List<SidebarTree> parentNodesPart=new CopyOnWriteArrayList(); // 存放所有父节点 
		rootPart = threeOrgan;
		if(deviceList!=null && deviceList.size()>0){
			for(int i=0; i<deviceList.size(); i++){
				nodePart= deviceList.get(i);
				if(!Util.isNullOrEmpty(nodePart.getT_organization_r_guid())){
					if(nodePart.getT_line_r_guid().equals(rootPart.getT_line_r_guid()) && nodePart.getT_organization_r_guid().equals(rootPart.getT_organization_r_guid())){ //从根节点开始遍历是不是子节点  
						nodePart.setLevel("device");
						parentNodesPart.add(nodePart);
						rootPart.getNodes().add(nodePart);
					}else{ //获取root子节点的孩子节点  
						getChildrenNodesForLine(parentNodesPart, nodePart);
						parentNodesPart.add(nodePart);
					}
				}
			}
		}
		return rootPart;
	}
	/**组装机构下的线路，设备，检测项
	 * @param allList
	 * @param lineList
	 * @return
	 */
	private SidebarTree queryOrganMenuForLine(List<SidebarTree> lineList,List<SidebarTree> deviceList,List<SidebarTree> itemList,SidebarTree threeOrgan) {
		
		
		//查询组织机构
		SidebarTree rootPart=new SidebarTree();
        SidebarTree nodePart=new SidebarTree();
        List<SidebarTree> parentNodesPart=new CopyOnWriteArrayList(); // 存放所有父节点 
        rootPart = threeOrgan;
		if(lineList!=null && lineList.size()>0){
				for(int i=0; i<lineList.size(); i++){
					nodePart= lineList.get(i);
					if(!Util.isNullOrEmpty(nodePart.getT_organization_r_guid())){
						if(nodePart.getT_organization_r_guid().equals(rootPart.getGuid())){ //从根节点开始遍历是不是子节点  
							nodePart.setLevel("line");
							parentNodesPart.add(nodePart);
							rootPart.getNodes().add(nodePart);
						}else{ //获取root子节点的孩子节点  
							getChildrenNodesForLine(parentNodesPart, nodePart);
							parentNodesPart.add(nodePart);
						}
					}
				}
		}
		return rootPart;
	}
	
	private static void getChildrenNodesForLine(List<SidebarTree> parentNodes , SidebarTree node){  
		for(int i=parentNodes.size()-1; i>=0; i--){
			SidebarTree pnode=parentNodes.get(i);
			try {
				if(pnode.getGuid().equals(node.getParent_guid())){
					if(!pnode.getNodes().contains(node)){
						pnode.getNodes().add(node);
					}
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private static void getChildrenNodes(List<SidebarTree> parentNodes , SidebarTree node){  
        for(int i=parentNodes.size()-1; i>=0; i--){  
             SidebarTree pnode=parentNodes.get(i);  
            	 if(pnode.getGuid().equals(node.getParent_guid())){
            		 	if(!pnode.getNodes().contains(node)){
            		 		pnode.getNodes().add(node);
            		 	}
            		 return;
            	 }
        }
   }
	
	private JSONArray getLastValue(JSONArray organInfoJA,Boolean isOrgan,HttpServletRequest request) {
		for(int i=0;i<organInfoJA.size();i++){
			 JSONObject job = organInfoJA.getJSONObject(i);
			 boolean con = false;
			 job.remove("guid");
			 job.remove("parent_guid");
			 job.remove("parent_level");
			 
			 if("line".equals(job.get("level"))){//默认折叠line下的tree机构
				 job.put("state","closed");
			 }
			 
			 if(Util.isNullOrEmpty((List)job.get("nodes")) || ((List)job.get("nodes")).size() <= 0 || "line".equals(job.get("level"))){//根据需要判断筛选的level
				 if(isOrgan){
					 job.put("iconCls","icon-text_gang");
				 }else{
					 job.put("iconCls","icon-cog_start");
				 }
				 String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
					 job.put("href", basePath+"xjQueryData/queryAllDataOfXJ.do");
			 }else{
				 if(isOrgan){
					 job.put("iconCls","icon-org");
				 }else{
					 job.put("state","closed");
					 job.put("iconCls","icon-cog");
				 }
				 job.put("href","");
				 job.remove("t_item_r_guid");
				 job.remove("t_device_r_guid");
				 job.remove("t_line_r_guid");
				 job.remove("t_organization_r_guid");
				 this.getLastValue((JSONArray)job.get("nodes"), isOrgan,request);
			 }
		 }
		return organInfoJA;
	}

	@Override
	public JSONArray getInnerOrgan(String lastOrganGuid,String t_line_r_guid, HttpServletRequest request) {
		
		Map<String, Object> dataMap = new HashMap();
		dataMap.put("t_organization_r_guid", lastOrganGuid);
		dataMap.put("t_line_r_guid", t_line_r_guid);
		String cache_key = RedisCache.CAHCENAME + "|queryXJDeviceByLine|";
		String sql = "xjOrgan.queryXJDeviceByLine";
		List<SidebarTree> deviceList = this.getCacheOrDB(cache_key,sql,dataMap,SidebarTree.class,false);
		for (int m = 0; m < deviceList.size(); m++) {//添加设备的子组织机构巡检项
			SidebarTree oneDevice = deviceList.get(m);
			String t_device_r_guid = oneDevice.getT_device_r_guid();
			SidebarTree organOfItem = this.getOrganOfItem(lastOrganGuid,t_line_r_guid,t_device_r_guid,oneDevice);
			deviceList.set(m, organOfItem);
			organOfItem = null;
		}
		Boolean isOrgan = false;
		JSONArray lastValue = this.getLastValue(JSONArray.fromObject(deviceList),isOrgan,request);
		return lastValue;
	}
}
