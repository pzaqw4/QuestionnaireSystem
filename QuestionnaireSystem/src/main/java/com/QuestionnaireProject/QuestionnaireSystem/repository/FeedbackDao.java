package com.QuestionnaireProject.QuestionnaireSystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Feedback;

@Transactional  //避免控制資料庫時發生錯誤產生垃圾
@Repository
public interface FeedbackDao extends JpaRepository<Feedback,Integer>{
	public List<Feedback> findByPostId(UUID postId);
}
