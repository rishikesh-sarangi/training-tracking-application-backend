package com.cozentus.trainingtrackingapplication.service;

import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.model.Student;
import com.cozentus.trainingtrackingapplication.model.Teacher;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}") private String sender;

	public Boolean sendEmail(String recipient, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(this.sender);
		message.setTo(recipient);
		message.setSubject(subject);
		message.setText(body);
		
		try {			
			javaMailSender.send(message);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
    public String generateRandomPassword() {
        // Generate a random password of length 12
        return RandomStringUtils.random(8, 0, 0, true, true, null, new SecureRandom());
    }
    
	public boolean sendWelcomeEmailStudent(Student student) {
		String recipient = student.getStudentEmail();
		String subject = "Details for your Training App Account";
		String password = generateRandomPassword();
		String body = "Hello Student: \n" + "Your Email: " + student.getStudentEmail() + "\n Your Password: "
				+ password;

		return sendEmail(recipient, subject, body);
	}
	
	public boolean sendWelcomeEmailTeacher(Teacher teacher) {
		String recipient = teacher.getTeacherEmail();
		String subject = "Details for your Training App Account";
		String password = generateRandomPassword();
		String body = "Hello Teacher: \n" + "Your Email: " + teacher.getTeacherEmail() + "\n Your Password: "
				+ password;

		return sendEmail(recipient, subject, body);
	}

}
