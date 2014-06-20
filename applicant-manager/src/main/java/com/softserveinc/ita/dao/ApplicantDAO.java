package com.softserveinc.ita.dao;

import com.softserveinc.ita.entity.Applicant;
import com.softserveinc.ita.exception.ApplicantDoesNotExistException;
import com.softserveinc.ita.exception.GroupNotFoundException;

import java.util.List;

public interface ApplicantDAO {
    List<Applicant> getApplicants();
    List<Applicant> getApplicantsInAGroup(String groupID) throws GroupNotFoundException;
    Applicant getApplicantById(String applicantId) throws ApplicantDoesNotExistException;
}
