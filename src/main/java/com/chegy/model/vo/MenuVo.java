package com.chegy.model.vo;

import java.util.List;
import java.util.Set;

public class MenuVo {
	private Integer id;
	
	private String name;
	private String url;
	private String perms;
	private Integer orderNum; 
	private String icon;
	private Set<MenuVo> children;
	
	private String checkArr = "0";

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Set<MenuVo> getChildren() {
		return children;
	}

	public void setChildren(Set<MenuVo> children) {
		this.children = children;
	}

	public String getCheckArr() {
		return checkArr;
	}

	public void setCheckArr(String checkArr) {
		this.checkArr = checkArr;
	}
	
	
	
	
}
