package com.revature.aroma.order;

import com.revature.aroma.util.HibernateUtil;
import com.revature.aroma.util.interfaces.MenuCrudable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class OrderDao implements MenuCrudable<Order> {

        @Override
        public Order create(Order newOrder) {
            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                session.save(newOrder);
                transaction.commit();
                return newOrder;
            } catch (HibernateException | IOException e){
                e.printStackTrace();
                return null;
            } finally {
                HibernateUtil.closeSession();
            }
        }

        @Override
        public List<Order> findAll() {

            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                List<Order> orders = session.createQuery("FROM Order ").list();
                transaction.commit();
                return orders;
            } catch (HibernateException | IOException e){
                e.printStackTrace();
                return null;
            } finally {
                HibernateUtil.closeSession();
            }
        }



    @Override
        public Order findByMenuItem(String menuItem) {
            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                Order order = session.get(Order.class, menuItem);
                transaction.commit();
                return order;
            } catch(HibernateException | IOException e){
                e.printStackTrace();
                return null;
            } finally {
                HibernateUtil.closeSession();
            }
        }

        @Override
        public boolean update(Order order) {
            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                session.merge(order);
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
        public boolean delete(String id) {
            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                Order order = session.load(Order.class, id);
                session.remove(order);
                transaction.commit();
                return true;
            } catch (HibernateException | IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                HibernateUtil.closeSession();
            }
        }

    public Order authenticateOrder(String id, String menu_item){

        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("from Order where id= :id and menu_item= :menu_item");
            query.setParameter("id", id);
            query.setParameter("menu_item", menu_item);
            Order order = (Order) query.uniqueResult();
            transaction.commit();
            return order;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            HibernateUtil.closeSession();
        }

    }
    public boolean checkId(String id) {

        try {
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("from Order where id= :id");
            query.setParameter("id", id);
            Order order = (Order) query.uniqueResult();
            transaction.commit();
            if(order == null) return false;
            return true;
        } catch (HibernateException | IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            HibernateUtil.closeSession();
        }
    }
}

