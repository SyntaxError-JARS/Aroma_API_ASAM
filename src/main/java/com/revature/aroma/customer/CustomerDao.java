package com.revature.aroma.customer;



import com.revature.aroma.util.HibernateUtil;
import com.revature.aroma.util.interfaces.Crudable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.io.*;
import java.util.List;

public class CustomerDao implements Crudable<Customer> {

    @Override
    public Customer create(Customer newCustomer) {
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.save(newCustomer);
            transaction.commit();
            return newCustomer;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Override
    public List<Customer> findAll() {
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            List<Customer> customer = session.createQuery("FROM Customer").list();
            transaction.commit();
            return customer;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }

    }

    @Override
    public Customer findByUsername(String username) {

        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Customer customer = session.get(Customer.class, username);
            transaction.commit();
            return customer;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }

    }

    @Override
    public boolean update(Customer updatedCustomer) {
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.merge(updatedCustomer);
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
    public boolean delete(String username) {
        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Customer customer = session.load(Customer.class, username);
            session.remove(customer);
            transaction.commit();
            return true;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public Customer authenticateCustomer(String username, String password){

        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();

            //Todo: important Javax.persistence, which has SetParameter method
            Query query = session.createQuery("from Customer where username= :username and password= :password");
            query.setParameter("username", username);
            query.setParameter("password", password);
            //TODO: import the query.Query
            Customer customer = (Customer) query.uniqueResult();
            transaction.commit();
            return customer;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }

    }

    public Customer authenticateAdmin(String username, String password, String isAdmin){

        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();

            //Todo: important Javax.persistence, which has SetParameter method
            Query query = session.createQuery("select username, password, is_admin from Customer where username= :username");
            query.setParameter("username", username);
            query.setParameter("password", password);
            query.setParameter("is_admin", isAdmin);

            //TODO: import the query.Query
            Customer customer = (Customer) query.uniqueResult();
            transaction.commit();
            return customer;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }

    }

    public boolean checkUsername(String username) {

        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("from Customer where username= :username");
            query.setParameter("username", username);
            Customer customer = (Customer) query.uniqueResult();
            transaction.commit();
            if(customer == null) return false;
            return true;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            System.out.println("Here is the problem");
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }
}
