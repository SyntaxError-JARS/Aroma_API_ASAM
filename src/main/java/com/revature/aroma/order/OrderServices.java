package com.revature.aroma.order;

import com.revature.aroma.util.exceptions.AuthenticationException;
import com.revature.aroma.util.exceptions.InvalidRequestException;
import com.revature.aroma.util.exceptions.ResourcePersistanceException;
import com.revature.aroma.util.interfaces.MenuServiceable;
import com.revature.aroma.util.logging.Logger;

import java.util.List;

public class OrderServices implements MenuServiceable<Order> {


        private OrderDao  orderDao;
        private Logger logger = Logger.getLogger();

        public OrderServices(OrderDao orderDao) {
            this.orderDao = orderDao;
        }

        @Override
        public List<Order> readAll(){
            // TODO: orderDao intellisense
            List<Order> order = orderDao.findAll();
            return order;
        }

      @Override
        public Order readByMenuItem(String menuItem) throws ResourcePersistanceException {

            Order order = orderDao.findByMenuItem(menuItem);
            return order;
        }

        @Override
        public Order update(Order updatedOrder) {
            if (!orderDao.update(updatedOrder)){
                return null;
            }

            return updatedOrder;
        }

        @Override
        public boolean delete(String menuItem) {
            return orderDao.delete(menuItem);
        }

        public boolean validateIdNotUsed(String id){
            return orderDao.checkId(id);
        }

        public Order create(Order newOrder){
            logger.info("Order trying to be registered: " + newOrder);
            if(!validateInput(newOrder)){ // checking if false
                throw new InvalidRequestException("Order input was not validated, either empty String or null values");
            }

            // TODO: Will implement with JDBC (connecting to our database)
            if(validateIdNotUsed(newOrder.getId())){
                throw new InvalidRequestException("Order Id is already in use. Please try again with another Order or login into previous made account.");
            }

            Order persistedOrder = orderDao.create(newOrder);

            if(persistedOrder == null){
                throw new ResourcePersistanceException("order was not persisted to the database upon registration");
            }
            logger.info("Order has been persisted: " + newOrder);
            return persistedOrder;
        }

        @Override
        public boolean validateInput(Order newOrder) {
            logger.debug("Validating Order: " + newOrder);
            if(newOrder == null) return false;
            if(newOrder.getId() == null || newOrder.getId().trim().equals("")) return false;
            if(newOrder.getComment_t() == null || newOrder.getComment_t().trim().equals("")) return false;
            if(newOrder.getCustomer_username() == null || newOrder.getCustomer_username().trim().equals("")) return false;
            if(newOrder.getIsFavorite() == false && newOrder.getIsFavorite()== true) return false;
            if(newOrder.getMenu_item()== null || newOrder.getMenu_item() ==null) return false;
            return (newOrder.getOrderDate() != null || !newOrder.getOrderDate().trim().equals(""));
        }

        public Order authenticateOrder(String id, String menu_item){

            if(menu_item == null || menu_item.trim().equals("") || id == null || id.trim().equals("")) {
                throw new InvalidRequestException("Either menu_item or id is an invalid entry. Please try logging in again");
            }

            Order authenticatedOrder = orderDao.authenticateOrder(id, menu_item);

            if (authenticatedOrder == null){
                throw new AuthenticationException("Unauthenticated Order, information provided was not consistent with our database.");
            }

            return authenticatedOrder;

        }
    }


