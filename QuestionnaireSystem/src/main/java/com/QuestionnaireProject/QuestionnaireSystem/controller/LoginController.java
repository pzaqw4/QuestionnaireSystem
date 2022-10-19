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
		model.addAttribute("message", "管理員登入");
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
	    	 redirectAttrs.addFlashAttribute("alertMessage",person.getName()+"登入成功 歡迎使用!");
	    	 
	    	 return "redirect:/backendindex";
	     }

	     redirectAttrs.addFlashAttribute("alertMessage","查無此人,請重新確認帳號密碼!!");
	        return "redirect:/login";
	    }
	 
	 @GetMapping("/logout")
	 public String logout(HttpSession session
			 ,RedirectAttributes redirectAttrs) {
		 session.removeAttribute("person");
		 redirectAttrs.addFlashAttribute("alertMessage","登出成功 歡迎再次使用!");
		 return "redirect:/index";
	 }
}
