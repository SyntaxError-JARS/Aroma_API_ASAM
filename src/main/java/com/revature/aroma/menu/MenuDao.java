package com.revature.aroma.menu;

import com.revature.aroma.util.HibernateUtil;
import com.revature.aroma.util.interfaces.Crudable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;


public class MenuDao implements Crudable<Menu> {

        @Override
        public Menu create(Menu newMenu) {
            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                session.save(newMenu);
                transaction.commit();
                return newMenu;
            } catch (HibernateException | IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                HibernateUtil.closeSession();
            }
        }

        @Override
        public List<Menu> findAll() {
            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                List<Menu> menus = session.createQuery("FROM Menu").list();
                transaction.commit();
                return menus;
            } catch (HibernateException | IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                HibernateUtil.closeSession();
            }

        }

        @Override
        public Menu findByUsername(String item_name) {

            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                Menu menu = session.get(Menu.class, item_name);
                transaction.commit();
                return menu;
            } catch (HibernateException | IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                HibernateUtil.closeSession();
            }

        }

        @Override
        public boolean update(Menu updatedMenu) {
            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                session.merge(updatedMenu);
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
        public boolean delete(String itemName) {
            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                Menu menu = session.load(Menu.class, itemName);
                session.remove(menu);
                transaction.commit();
                return true;
            } catch (HibernateException | IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                HibernateUtil.closeSession();
            }
        }

        public Menu authenticateMenu(String item_name, String protein){

            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                Query query = session.createQuery("from Menu where item_name= :item_name and protein= :protein");
                query.setParameter("item_name", item_name);
                query.setParameter("protein", protein);
                Menu menu = (Menu) query.uniqueResult();
                transaction.commit();
                return menu;
            } catch (HibernateException | IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                HibernateUtil.closeSession();
            }

        }
        public boolean checkItem_name(String item_name) {

            try {
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                Query query = session.createQuery("from Menu where item_name= :item_name");
                query.setParameter("item_name", item_name);
                Menu menu = (Menu) query.uniqueResult();
                transaction.commit();
                if(menu == null) return false;
                return true;
            } catch (HibernateException | IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                HibernateUtil.closeSession();
            }
        }
    }

