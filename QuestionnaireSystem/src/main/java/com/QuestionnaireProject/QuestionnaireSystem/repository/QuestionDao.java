package com.QuestionnaireProject.QuestionnaireSystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Question;

@Transactional  //�קK�����Ʈw�ɵo�Ϳ��~���ͩU��
@Repository
public interface QuestionDao extends JpaRepository<Question,Integer>{
	public List<Question> findByPostId(UUID postId);
}
