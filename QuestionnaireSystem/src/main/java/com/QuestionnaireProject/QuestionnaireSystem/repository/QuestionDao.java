package com.QuestionnaireProject.QuestionnaireSystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Question;

@Transactional(rollbackFor = Exception.class)  //�קK�����Ʈw�ɵo�Ϳ��~���ͩU���A�̦n�g�bService�h����k�W
@Repository
public interface QuestionDao extends JpaRepository<Question,Integer>{
	public List<Question> findByPostId(UUID postId);
}
