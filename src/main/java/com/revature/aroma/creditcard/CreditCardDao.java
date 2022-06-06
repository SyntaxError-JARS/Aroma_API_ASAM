package com.revature.aroma.creditcard;

import com.revature.aroma.util.HibernateUtil;
import com.revature.aroma.util.interfaces.Crudable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;

import java.util.List;

public class CreditCardDao implements Crudable<CreditCard> {

    @Override
    public CreditCard create(CreditCard newCreditCard) {
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.save(newCreditCard);
            transaction.commit();
            return newCreditCard;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Override
    public List<CreditCard> findAll() {
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            List<CreditCard> CreditCard = session.createQuery("FROM credit_card").list();
            transaction.commit();
            return CreditCard;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }

    }
    @Override
    public CreditCard findByUsername(String username) {

        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            CreditCard creditCard = session.get(CreditCard.class, username);
            transaction.commit();
            return creditCard;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }

    }

    @Override
    public boolean update(CreditCard updatedCreditCard) {
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.merge(updatedCreditCard);
            transaction.commit();
            return true;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Override
    public boolean delete(String ccNumber) {
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            CreditCard creditCard = session.load(CreditCard.class, ccNumber);
            session.remove(creditCard);
            transaction.commit();
            return true;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public CreditCard authenticateCreditCard(String username, String ccNumber) {

        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();

            //Todo: important Javax.persistence, which has SetParameter method
            Query query = session.createQuery("from CreditCard where cc_number= :ccNumber and customer_username= :username");
            query.setParameter("cc_number", ccNumber);
            query.setParameter("customer_username", username);
            //TODO: import the query.Query
            CreditCard creditCard = (CreditCard) query.uniqueResult();
            transaction.commit();
            return creditCard;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }

    }

    public boolean checkLimit(double limit) {

        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("from CreditCard where limit= :limit");
            query.setParameter("limit", limit);
            CreditCard creditCard = (CreditCard) query.uniqueResult();
            transaction.commit();
            if (creditCard == null) return false;
            return true;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public static CreditCard addToBalance(String limit, String ccNumber) {
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();

            //Todo: important Javax.persistence, which has SetParameter method
            Query query = session.createQuery("from CreditCard where cc_number= :ccNumber and limit= :limit");
            query.setParameter("cc_number", ccNumber);
            query.setParameter("limit", limit);
            //TODO: import the query.Query
            CreditCard creditCard = (CreditCard) query.uniqueResult();
            transaction.commit();
            return creditCard;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public static CreditCard subtractFromBalance(String limit, String ccNumber) {
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();

            //Todo: important Javax.persistence, which has SetParameter method
            Query query = session.createQuery("from CreditCard where cc_number= :ccNumber and limit= :limit");
            query.setParameter("cc_number", ccNumber);
            query.setParameter("limit", limit);
            //TODO: import the query.Query
            CreditCard creditCard = (CreditCard) query.uniqueResult();
            transaction.commit();
            return creditCard;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }
    }


}




