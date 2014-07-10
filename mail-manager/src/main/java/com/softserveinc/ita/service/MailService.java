package com.softserveinc.ita.service;

import com.softserveinc.ita.entity.*;
import com.softserveinc.ita.service.exception.HttpRequestException;
import com.softserveinc.ita.service.impl.HttpRequestExecutorRestImpl;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("mailService")
public class MailService {

    public static final String DATE_FORMAT = "dd/MM/yyyy hh:mm";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String FROM = "javasendertest@gmail.com";
    public static final String LOGO = "ItAcademyLogo";
    public static final String LOGO_IMAGE_REF = "images/joinProfessionals.png";
    public static final String TIME = "time";
    public static final String COURSE = "course";
    public static final String GROUP_START_TIME = "groupStartTime";
    public static final String HR_NAME = "HRName";
    public static final String HR_SURNAME = "HRSurname";
    public static final String HR_PHONE = "HRPhone";
    public static final String HR_EMAIL = "HRemail";
    public static final String COURSE_ADDRESS = "courseAddress";


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private HttpRequestExecutorRestImpl httpRequestExecutor;

    private MimeMessageHelper helper;
    private MimeMessage mimeMessage;

    public void notifyApplicant(String appointmentId) {
        mimeMessage = mailSender.createMimeMessage();
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // TODO get applicant id from queue
        Appointment appointment = null;
        Applicant applicant = null;

        try {
            appointment = httpRequestExecutor.getObjectByID(appointmentId, Appointment.class);
            String applicantId = appointment.getApplicantId();
            applicant = httpRequestExecutor.getObjectByID(applicantId, Applicant.class);
        } catch (HttpRequestException e) {
            e.printStackTrace();
        }
        switch (applicant.getStatus()) {
            case NOT_SCHEDULED:
                sendNotScheduledLetterModel(applicant);
                break;
            case SCHEDULED:
                sendSchedulingLetterModel(applicant, appointment);
                break;
            case NOT_PASSED:
                sendFailedLetterModel(applicant);
                break;
            case PASSED:
                sendPassedLetterModel(applicant, appointment);
                break;
        }
    }



    public void sendMail() throws MessagingException {
        mimeMessage = mailSender.createMimeMessage();
        helper = new MimeMessageHelper(mimeMessage, true);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(NAME, "andrey");
        model.put(COURSE_ADDRESS,"Deht street 42");
        model.put(SURNAME, "Braslavskiy");
        model.put(TIME,"11/06");
        model.put(HR_NAME, "Lena");
        model.put(HR_SURNAME, "Golovach");
        model.put(HR_PHONE, "0663459412");
        model.put(HR_EMAIL, "dfdf@dsf");
        model.put(COURSE,"Java");
        model.put(GROUP_START_TIME,"11/07");

        String emailText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                "mailTemplaits/interviewPassed.vm","UTF-8",model);
        helper.setFrom("javasendertest@gmail.com");
        helper.setTo("braslavskiyandrey@gmail.com");
        helper.setText(emailText,true);
        helper.setSubject("Hey hey hey internet");
        ClassPathResource image = new ClassPathResource("images/joinProfessionals.png");
        helper.addInline("ItAcademyLogo", image);
        mailSender.send(mimeMessage);
    }

    private void sendNotScheduledLetterModel(Applicant applicant){
        Map<String, Object> model = new HashMap<>();
        model.put(NAME, applicant.getName());
        model.put(SURNAME, applicant.getSurname());
        sendLetter(applicant, model);
    }
    private void sendPassedLetterModel(Applicant applicant, Appointment appointment) {
        Group applicantGroup = null;
        User responsibleHR = null;
        try {
            responsibleHR = httpRequestExecutor.getObjectByID(appointment.getOwnerId(), User.class);
            applicantGroup = httpRequestExecutor.getObjectByID(appointment.getGroupId(), Group.class);
        } catch (HttpRequestException e) {
            e.printStackTrace();
        }
        Map<String, Object> model = new HashMap<>();
        model.put(NAME, applicant.getName());
        model.put(SURNAME, applicant.getSurname());
        model.put(COURSE,applicantGroup.getCourse().getName());
        model.put(COURSE_ADDRESS, applicantGroup.getAddress());
        model.put(GROUP_START_TIME,convertTimeToDate(applicantGroup.getStartTime()));
        model.put(HR_NAME, responsibleHR.getName());
        model.put(HR_SURNAME, responsibleHR.getSurname());
        model.put(HR_PHONE,responsibleHR.getPhone());
        model.put(HR_EMAIL, responsibleHR.getEmail());

        sendLetter(applicant, model);
    }

    private void sendFailedLetterModel(Applicant applicant) {
        Map<String, Object> model = new HashMap<>();
        model.put(NAME, applicant.getName());
        model.put(SURNAME, applicant.getSurname());
        sendLetter(applicant,model);
    }



    private void sendSchedulingLetterModel(Applicant applicant, Appointment appointment) {
        User responsibleHR = null;
        try {
            responsibleHR = httpRequestExecutor.getObjectByID(appointment.getOwnerId(), User.class);
        } catch (HttpRequestException e) {
            e.printStackTrace();
        }
        Map<String, Object> model = new HashMap<>();
        model.put(NAME, applicant.getName());
        model.put(SURNAME, applicant.getSurname());
        model.put(TIME, convertTimeToDate(appointment.getStartTime()));
        model.put(HR_NAME, responsibleHR.getName());
        model.put(HR_SURNAME, responsibleHR.getSurname());
        model.put(HR_PHONE,responsibleHR.getPhone());
        model.put(HR_EMAIL, responsibleHR.getEmail());
        sendLetter(applicant,model);
    }


    private void sendLetter(Applicant applicant, Map<String,Object> letterModel){
        String emailText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                applicant.getStatus().getTemplateRef(),"UTF-8",letterModel);
        try {
            helper.setFrom(FROM);
            helper.setTo(applicant.getEmail());
            helper.setText(emailText, true);
            helper.setSubject(applicant.getStatus().getSubject());
            ClassPathResource image = new ClassPathResource(LOGO_IMAGE_REF);
            helper.addInline(LOGO, image);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static String convertTimeToDate(long milliseconds) {
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        return formatter.format(calendar.getTime());
    }
}