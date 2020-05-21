package com.project.ide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {
	
	@Autowired
	private TemplateEngine templateEngine;
	

    public String build(String username, String password) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("password", password);
        return templateEngine.process("resetPassword", context);
    }

}
