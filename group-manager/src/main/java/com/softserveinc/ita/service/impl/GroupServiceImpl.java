package com.softserveinc.ita.service.impl;

import com.softserveinc.ita.dao.GroupDao;
import com.softserveinc.ita.entity.Course;
import com.softserveinc.ita.entity.Group;
import com.softserveinc.ita.exception.impl.WrongGroupStatusException;
import com.softserveinc.ita.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Override
    public ArrayList<Group> getGroupsByStatus(Group.Status groupStatus) {
        ArrayList<Group> groups = groupDao.getAllGroups();
        ArrayList<Group> choosenGroups = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        for (Group group : groups) {
            switch (groupStatus) {
                case PLANNED:
                    if (currentTime < group.getStartBoardingTime()) {
                        choosenGroups.add(group);
                    }
                    break;
                case BOARDING:
                    if (currentTime > group.getStartBoardingTime() && currentTime < group.getStartTime()) {
                        choosenGroups.add(group);
                    }
                    break;
                case IN_PROCESS:
                    if (currentTime > group.getStartTime() && currentTime < group.getEndTime()) {
                        choosenGroups.add(group);
                    }
                    break;
                case FINISHED:
                    if (currentTime > group.getEndTime()) {
                        choosenGroups.add(group);
                    }
                    break;
            }
        }
        return choosenGroups;
    }

    @Override
    public ArrayList<Course> getCourses() {
        return groupDao.getCourses();
    }

    @Override
    public Group createGroup(Group group) {
        return groupDao.addGroup(group);
    }

    @Override
    public ArrayList<Group> getAllGroups() {
        return groupDao.getAllGroups();
    }

    private boolean isWrongStatus(Group.Status groupStatus) {
        for(Group.Status status : Group.Status.values()){
            if (status.equals(groupStatus)) {
                return false;
            }
        }
        return true;
    }
}
