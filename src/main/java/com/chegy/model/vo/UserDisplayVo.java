package com.chegy.model.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserDisplayVo {

	private Integer id;
	private String username;
	private String deptNames;
	private String roleNames;
	
	private String email;
	private Date updatedTime;
	private boolean status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDeptNames() {
		return deptNames;
	}
	public void setDeptNames(String deptNames) {
		this.deptNames = deptNames;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00") 
	public Date getUpdatedTime() {
		return updatedTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00") 
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	public String getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	@Override
	public String toString() {
		return "UserDisplayVo [id=" + id + ", username=" + username + ", deptNames=" + deptNames + ", roleNames="
				+ roleNames + ", email=" + email + ", updatedTime=" + updatedTime + ", status=" + status + "]";
	}
	
}
