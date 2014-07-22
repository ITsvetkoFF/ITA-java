package com.softserveinc.ita.service.impl;

import com.softserveinc.ita.dao.GroupDao;
import com.softserveinc.ita.entity.Applicant;
import com.softserveinc.ita.entity.Group;
import com.softserveinc.ita.exception.impl.WrongGroupStatusException;
import com.softserveinc.ita.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Override
    public List<Group> getGroupsByStatus(String groupStatus) {
        if(isWrongStatus(groupStatus)){
            throw new WrongGroupStatusException();
        }
        return groupDao.getGroupsByStatus(groupStatus);
    }

    @Override
    public List<Applicant> getApplicantsByGroupID(String groupID) {
        return groupDao.getApplicantsByGroupID(groupID);
    }
    private boolean isWrongStatus(String groupStatus){
        for(Group.Status status : Group.Status.values()){
            if(status.getName().equals(groupStatus)){
                return false;
            }
        }
        return true;
    }
}
