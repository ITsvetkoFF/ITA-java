package com.softserveinc.ita.interviewfactory.service.questionsBlocksServices;

import com.softserveinc.ita.dao.InterviewDAO;
import com.softserveinc.ita.dao.QuestionsBlockDAO;
import com.softserveinc.ita.entity.QuestionsBlock;
import com.softserveinc.ita.service.exception.HttpRequestException;
import exceptions.InterviewNotFoundException;
import exceptions.QuestionsBlockNotFound;
import exceptions.WrongCriteriaException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Вадим
 * Date: 11.07.14
 * Time: 14:05
 * To change this template use File | Settings | File Templates.
 */
public class QuestionsBlockServicesImpl implements QuestionsBlockServices {

    @Autowired
    private QuestionsBlockDAO questionsBlockDAO;

    @Override
    public QuestionsBlock getQuestionsBlockFromInterviewByUserId(String userID, String appointmentId){
          return questionsBlockDAO.getQuestionsBlockFromInterviewByUserId(userID, appointmentId);
    }

    @Override
    public QuestionsBlock getQuestionsBlockByQuestionsBlockId(String questionsBlockId) {
        return questionsBlockDAO.getQuestionsBlockFromInterviewByQuestionsBlockId(questionsBlockId);
    }

    @Override
    public String updateQuestionsBlock(QuestionsBlock newQuestionsBlock) {
        return questionsBlockDAO.updateQuestionsBlock(newQuestionsBlock);
    }

    @Override
    public QuestionsBlock getStandardQuestionsBlockFromInterview(String interviewId){
        return questionsBlockDAO.getStandardQuestionsBlockFromInterview(interviewId);
    }

    @Override
    public QuestionsBlock getStandardQuestionsBlock() {
        return questionsBlockDAO.getStandardQuestionsBlock();
    }

    @Override
    public void setStandardQuestionsBlock(QuestionsBlock standardQuestionsBlock) {
        questionsBlockDAO.setStandardQuestionsBlock(standardQuestionsBlock);
    }
}
