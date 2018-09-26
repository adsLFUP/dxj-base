package com.aic.ssm.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class SidebarTree {  
	private String text;  
    private String href;  
    private String guid; 
    private String level;
    private String parent_guid; //父节点  
    private String parent_level;
    private String t_organization_r_guid;
    private String t_line_r_guid;
    private String t_line_r_content_guid;
    private String t_device_r_guid;
    private String t_item_r_guid;
    private String state;
    
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getT_line_r_content_guid() {
		return t_line_r_content_guid;
	}
	public void setT_line_r_content_guid(String t_line_r_content_guid) {
		this.t_line_r_content_guid = t_line_r_content_guid;
	}
	public void setT_item_r_guid(String t_item_r_guid) {
		this.t_item_r_guid = t_item_r_guid;
	}
	public String getT_item_r_guid() {
		return t_item_r_guid;
	}
	public String getT_organization_r_guid() {
		return t_organization_r_guid;
	}
	public void setT_organization_r_guid(String t_organization_r_guid) {
		this.t_organization_r_guid = t_organization_r_guid;
	}
	public String getT_line_r_guid() {
		return t_line_r_guid;
	}
	public void setT_line_r_guid(String t_line_r_guid) {
		this.t_line_r_guid = t_line_r_guid;
	}
	public String getT_device_r_guid() {
		return t_device_r_guid;
	}
	public void setT_device_r_guid(String t_device_r_guid) {
		this.t_device_r_guid = t_device_r_guid;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
    private List<SidebarTree> nodes= new ArrayList(); //存放子节点  
//    private List<SidebarTree> nodes= new CopyOnWriteArrayList(); //存放子节点  
    
   public String getParent_guid() {
		return parent_guid;
	}
	public void setParent_guid(String parent_guid) {
		this.parent_guid = parent_guid;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getParent_level() {
		return parent_level;
	}
	public void setParent_level(String parent_level) {
		this.parent_level = parent_level;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<SidebarTree> getNodes() {
		return nodes;
	}
	public void setNodes(List<SidebarTree> nodes) {
		this.nodes = nodes;
	}
	@Override
	public String toString() {
		return "text=" + text + ", guid=" + guid + ", level="
				+ level + ", parent_guid=" + parent_guid + ", parent_level="
				+ parent_level + ", nodes="
				+ nodes+ ", href=" + href+ ", t_organization_r_guid=" + t_organization_r_guid+ ", t_line_r_guid=" + t_line_r_guid+ ", t_device_r_guid=" + t_device_r_guid+ ", t_item_r_guid=" + t_item_r_guid+",state="+state;
	}
    
}