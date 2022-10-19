package com.QuestionnaireProject.QuestionnaireSystem.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuestionnaireProject.QuestionnaireSystem.entity.PersonInfo;
import com.QuestionnaireProject.QuestionnaireSystem.repository.PersonInfoDao;

@Service
public class LoginService {

	@Autowired
	private PersonInfoDao personDao;
	
	public PersonInfo getPerson(String account, String password) {
		PersonInfo person = personDao.getPersonInfoByAccountAndPassword(account, password);
		if (person != null) {
			Optional<PersonInfo> personOp = personDao.findById(person.getPersonId());
			return personOp.get();
		}
		return new PersonInfo();
	}
}
