package com.QuestionnaireProject.QuestionnaireSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.QuestionnaireProject.QuestionnaireSystem.entity.FrenquenQuestion;

@Transactional  //避免控制資料庫時發生錯誤產生垃圾
@Repository
public interface FrenquenQuestionDao extends JpaRepository<FrenquenQuestion,Integer>{

}
