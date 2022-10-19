package com.QuestionnaireProject.QuestionnaireSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.QuestionnaireProject.QuestionnaireSystem.entity.PersonInfo;
import com.QuestionnaireProject.QuestionnaireSystem.service.LoginService;

@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;

	@GetMapping(value = {"/login"})
    public String loginGet(Model model ,RedirectAttributes redirectAttrs) {   
		model.addAttribute("message", "�޲z���n�J");
        return "login";
    }
	
	 @PostMapping("/login")
	    public String loginPost(Model model
	      ,RedirectAttributes redirectAttrs
	      ,HttpSession session
	      ,@RequestParam("account") String account
	      ,@RequestParam("password") String password
	      ) {
	     
	     PersonInfo person =loginService.getPerson(account,password);
	     
	     if(person.getAccount() != null) {
	    	 session.setAttribute("person", person);
	    	 redirectAttrs.addFlashAttribute("alertMessage",person.getName()+"�n�J���\ �w��ϥ�!");
	    	 
	    	 return "redirect:/backendindex";
	     }

	     redirectAttrs.addFlashAttribute("alertMessage","�d�L���H,�Э��s�T�{�b���K�X!!");
	        return "redirect:/login";
	    }
	 
	 @GetMapping("/logout")
	 public String logout(HttpSession session
			 ,RedirectAttributes redirectAttrs) {
		 session.removeAttribute("person");
		 redirectAttrs.addFlashAttribute("alertMessage","�n�X���\ �w��A���ϥ�!");
		 return "redirect:/index";
	 }
}
