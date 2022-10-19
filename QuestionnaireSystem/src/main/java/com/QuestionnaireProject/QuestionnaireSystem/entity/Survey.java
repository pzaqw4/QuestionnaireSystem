package com.QuestionnaireProject.QuestionnaireSystem.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "survey")
public class Survey {

	@Id
	@Column(name = "postid")
	@Type(type = "uuid-char")
	private UUID postId;

	@Column(name = "title")
	private String title;

	@Column(name = "body")
	private String body;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "start_time" ,nullable = false,columnDefinition = "datetime default getdate()")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "end_time" ,nullable = false,columnDefinition = "datetime default getdate()")
	private Date endTime;
	
	@Column(name = "create_time")
	private Date createTime = new Date();
	
	@Column(name = "available")
	private int available;

	public UUID getPostId() {
		return postId;
	}

	public void setPostId(UUID postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}
	
	
}
