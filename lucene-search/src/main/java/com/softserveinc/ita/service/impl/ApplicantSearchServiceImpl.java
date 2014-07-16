package com.softserveinc.ita.service.impl;

import com.softserveinc.ita.entity.Applicant;
import com.softserveinc.ita.service.ApplicantSearchService;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.WildcardQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicantSearchServiceImpl implements ApplicantSearchService {
    @Autowired
    private SessionFactory sessionFactory;

    private static boolean setUpIsDone = false;

    /**
     * This method is used PURELY for test purposes. As we haven't got any real DB yet, this will give some values to play with
     */
    public void writeTestValuesToDB() {
        if (setUpIsDone) {
            return;
        }
        setUpIsDone = true;
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.save(new Applicant("John", "Doe"));
        session.save(new Applicant("Lola", "Stimorola"));
        session.save(new Applicant("Tony", "Ballony"));
        session.save(new Applicant("Caroline", "Woods"));
        session.save(new Applicant("Cinderella", "Hopkins"));
        session.save(new Applicant("George", "Underlobster"));
        session.save(new Applicant("Kelly", "Mitsubishi"));
        session.getTransaction().commit();
        FullTextSession fullTextSession = Search.getFullTextSession(session);
        try {
            fullTextSession.createIndexer().startAndWait();
        }catch (InterruptedException ex){
            System.err.println("Error creating search indexer: " + ex.getMessage());
        }
        session.close();
    }

    @Override
    public List<Applicant> getApplicantsByName(String wildcard) {
        writeTestValuesToDB();
        Session session = sessionFactory.openSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        Transaction tx = fullTextSession.beginTransaction();

        org.apache.lucene.search.Query luceneQuery = new WildcardQuery(new Term("name",wildcard));

        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Applicant.class);
        List<Applicant> applicants = fullTextQuery.list();

        tx.commit();
        session.close();

        return applicants;
    }

    @Override
    public List<Applicant> getApplicantsByLastName(String wildcard) {
        writeTestValuesToDB();
        Session session = sessionFactory.openSession();
        FullTextSession fullTextSession = Search.getFullTextSession(session);

        Transaction tx = fullTextSession.beginTransaction();

        org.apache.lucene.search.Query luceneQuery = new WildcardQuery(new Term("surname",wildcard));

        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Applicant.class);
        List<Applicant> applicants = fullTextQuery.list();

        tx.commit();
        session.close();

        return applicants;
    }
}
