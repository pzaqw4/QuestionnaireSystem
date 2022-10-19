package com.QuestionnaireProject.QuestionnaireSystem.repository;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Survey;

@Transactional // 避免控制資料庫時發生錯誤產生垃圾
@Repository
public interface SurveyDao extends JpaRepository<Survey, UUID> {

	@Query(value = "select s from Survey s where s.title like %:title%")
	public Page<Survey> findByTitleMatch(Pageable pageable, @Param("title") String title);

	@Query("select s " + "from Survey s "
			+ "where s.endTime is null and s.startTime >= :startTime and s.startTime < :endTimePlus1 "
			+ "or s.startTime >= :startTime and s.endTime < :endTimePlus1")
	public Page<Survey> findByStartTimeAndEndTime(Pageable pageable, @Param("startTime") Date startTime,
			@Param("endTimePlus1") Date endTimePlus1);

}
