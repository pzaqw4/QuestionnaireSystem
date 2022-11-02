package com.QuestionnaireProject.QuestionnaireSystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Feedback;

@Transactional(rollbackFor = Exception.class)  //�קK�����Ʈw�ɵo�Ϳ��~���ͩU���A�̦n�g�bService�h����k�W
@Repository
public interface FeedbackDao extends JpaRepository<Feedback,Integer>{
	public List<Feedback> findByPostId(UUID postId);
}
