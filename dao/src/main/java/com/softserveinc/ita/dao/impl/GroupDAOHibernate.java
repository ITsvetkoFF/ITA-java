package com.softserveinc.ita.dao.impl;

import com.softserveinc.ita.dao.GroupDAO;
import com.softserveinc.ita.entity.Group;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class GroupDAOHibernate implements GroupDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Group addGroup(Group group) {
        Session session = sessionFactory.getCurrentSession();
        Serializable groupId = session.save(group);
        return (Group) session.get(Group.class, groupId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Group> getAllGroups() {
        return (List<Group>) sessionFactory.getCurrentSession().createCriteria(Group.class).list();
    }

    @Override
    public Group getGroupBiId(String groupId) {
        return (Group) sessionFactory.getCurrentSession().get(Group.class, groupId);
    }
}
