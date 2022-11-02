package com.QuestionnaireProject.QuestionnaireSystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Question;

@Transactional(rollbackFor = Exception.class)  //避免控制資料庫時發生錯誤產生垃圾，最好寫在Service層的方法上
@Repository
public interface QuestionDao extends JpaRepository<Question,Integer>{
	public List<Question> findByPostId(UUID postId);
}
