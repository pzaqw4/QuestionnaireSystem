package com.QuestionnaireProject.QuestionnaireSystem.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "frenquenquestion")
public class FrenquenQuestion {

	@Id //PK  主要Key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //自動增量
	@Column(name = "id")
	private int id;
	
	@Column(name = "caption")
	private String caption;
	
	@Column(name = "selection")
	private String selection;
	
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "nullable")
	private Integer nullable;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getNullable() {
		return nullable;
	}

	public void setNullable(Integer nullable) {
		this.nullable = nullable;
	}
		
}
