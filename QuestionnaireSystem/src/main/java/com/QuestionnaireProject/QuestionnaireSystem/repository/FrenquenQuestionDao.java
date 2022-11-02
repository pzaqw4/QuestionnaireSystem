package com.QuestionnaireProject.QuestionnaireSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.QuestionnaireProject.QuestionnaireSystem.entity.FrenquenQuestion;

@Transactional(rollbackFor = Exception.class)  //�קK�����Ʈw�ɵo�Ϳ��~���ͩU���A�̦n�g�bService�h����k�W
@Repository
public interface FrenquenQuestionDao extends JpaRepository<FrenquenQuestion,Integer>{

}
