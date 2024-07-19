package com.cozentus.trainingtrackingapplication.service;

import java.security.SecureRandom;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.dto.TeacherEditDTO;
import com.cozentus.trainingtrackingapplication.model.MyUsers;
import com.cozentus.trainingtrackingapplication.model.Student;
import com.cozentus.trainingtrackingapplication.model.Teacher;

@Service
public class EmailService {

	private JavaMailSender javaMailSender;

	private TeacherService teacherService;

	private MyUserService myUserService;
	
	EmailService(JavaMailSender javaMailSender, TeacherService teacherService, MyUserService myUserService){		
		this.javaMailSender = javaMailSender;
		this.teacherService = teacherService;
		this.myUserService = myUserService;
	}

	@Value("${spring.mail.username}")
	private String sender;

	public Boolean sendEmail(String recipient, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(this.sender);
		message.setTo(recipient);
		message.setSubject(subject);
		message.setText(body);

		try {
			javaMailSender.send(message);
			return true;
		} catch (Exception e) {
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

//		for adding new Users
		MyUsers newUser = new MyUsers();
		newUser.setUserEmail(teacher.getTeacherEmail());
		newUser.setUsername(teacher.getTeacherName());
		newUser.setUserPassword(password);
		newUser.setUserRole("TEACHER");

		myUserService.addUser(newUser);
		teacherService.addTeacher(teacher);
//		teacherService.editTeacher(teacher.getTeacherId(), user);

		return sendEmail(recipient, subject, body);
	}

	public boolean sendWelcomeEmailTeacherEdit(Integer teacherId, TeacherEditDTO teacher) {
//		to deal with existing users
		Optional<MyUsers> existingUser = Optional.ofNullable(myUserService.findByEmailId(teacher.getOldEmail()));
		if (existingUser.isPresent()) {
			if (teacher.getNewEmail() == null) {
//				send a user object with only the updated name
				MyUsers user = new MyUsers();
				user.setUsername(teacher.getTeacherName());

//				send this object to the editUser method
				myUserService.editUser(existingUser.get().getUserId(), user);

//				send the updated details of teacher for updating
				teacherService.editTeacher(teacherId, teacher);
				return true;
			} else {
				
				myUserService.deleteUser(existingUser.get().getUserId());
				
				String recipient = teacher.getNewEmail();
				String subject = "Details for your Training App Account";
				String password = generateRandomPassword();
				String body = "Hello Teacher: \n" + "Your New Email: " + teacher.getNewEmail() + "\nYour Password: "
						+ password;

				MyUsers editedUser = new MyUsers();
				editedUser.setUserEmail(teacher.getNewEmail());
				editedUser.setUsername(teacher.getTeacherName());
				editedUser.setUserPassword(password);
				editedUser.setUserRole("TEACHER");

				myUserService.addUser(editedUser);
				teacherService.editTeacher(teacherId, teacher);

				return sendEmail(recipient, subject, body);
			}
		}
		return false;
	}
}
