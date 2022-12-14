package com.QuestionnaireProject.QuestionnaireSystem.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.QuestionnaireProject.QuestionnaireSystem.entity.Survey;
import com.QuestionnaireProject.QuestionnaireSystem.repository.SurveyDao;

@Service
public class SurveyService {

	@Autowired
	private SurveyDao surveyDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public Page<Survey> getSurveyPageList(int pageNum) {
		Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
		Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(order));
		Page<Survey> surveys = surveyDao.findAll(pageable);
		return surveys;
	}

	public Page<Survey> searchSurveyByTitleMatch(int pageNum, String title) {
		Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
		Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(order));
		Page<Survey> surveys = surveyDao.findByTitleMatch(pageable, title);
		return surveys;
	}

	public Page<Survey> searchSurveyByAllTime(int pageNum, Date startTime, Date endTime) {
		Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
		Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(order));
		Page<Survey> surveys = surveyDao.findByStartTimeAndEndTime(pageable, startTime, endTime);
		return surveys;
	}

	public void deleteSurvey(UUID[] postID) {
		try {
			for (UUID uuid : postID) {
				surveyDao.deleteById(uuid);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public int timeCheck(Date startTime, Date endTime) {
		if (Calendar.getInstance().getTime().after(startTime) == true && // ?{?b?????b?_?l????????
				Calendar.getInstance().getTime().before(endTime) == true) { // ?{?b?????b???????????e
			return 1; // ????
		}
		return 0; // ????
	}

	public void timeCheck() {
		try {
			List<Survey> surveyList = surveyDao.findAll();
			for (Survey survey : surveyList) {
				if (Calendar.getInstance().getTime().after(survey.getStartTime()) == true
						&& Calendar.getInstance().getTime().before(survey.getEndTime()) == true) {
					survey.setAvailable(1);
					surveyDao.save(survey);
				} else if (Calendar.getInstance().getTime().after(survey.getStartTime()) == false
						|| Calendar.getInstance().getTime().before(survey.getEndTime()) == false) {
					survey.setAvailable(0);
					surveyDao.save(survey);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public Date timeParse(String time) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = df.parse(time);
		return date;
	}

	public String timeFormatCheck(String time) {
		// ?S???????????P?w
		if (time.isEmpty()) { 
			return "";
		}
		String pattern = "20\\d{2}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";
		if (!time.matches(pattern)) {
			return "?????d???J????????!!";
		}
		return "";
	}

	public String timeFormatCheck(String startTime, String endTime) throws ParseException {
		Date parseStartTime = timeParse(startTime);
		Date parseEndTime = timeParse(endTime);
		if (parseStartTime.after(parseEndTime)) {
			return "?????d???J????!!";
		}
		return "";
	}

	public String paramCheck(String title, String body, String startTime, String endTime) throws ParseException {
		if (title == "") {
			return "?????J???????D!!";
		} else if (body == "") {
			return "?????J?????y?z!!";
		} else if (startTime == "") {
			return "?????J?????}?l????!!";
		} else if (endTime == "") {
			return "?????J????????????!!";
		} else {
			String startTimeCheck = timeFormatCheck(startTime);
			if (!startTimeCheck.isEmpty()) {
				return startTimeCheck;
			}
			String endTimeCheck = timeFormatCheck(endTime);
			if (!endTimeCheck.isEmpty()) {
				return endTimeCheck;
			}
			String formatCheck = timeFormatCheck(startTime, endTime);
			if (!formatCheck.isEmpty()) {
				return formatCheck;
			}
		}
		return "";
	}
	
	public Date endTimePlus1(Date endtime) {
		Calendar c = Calendar.getInstance();
		c.setTime(endtime);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}
}
