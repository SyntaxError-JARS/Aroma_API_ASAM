package com.revature.aroma.customer;

import com.revature.aroma.util.exceptions.AuthenticationException;
import com.revature.aroma.util.exceptions.InvalidRequestException;
import com.revature.aroma.util.interfaces.Serviceable;
import com.revature.aroma.util.exceptions.ResourcePersistanceException;
import com.revature.aroma.util.logging.Logger;

import java.util.List;

public class CustomerServices implements Serviceable<Customer> {

    private CustomerDao customerDao;
    private Logger logger = Logger.getLogger();

    public CustomerServices(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public List<Customer> readAll(){

        // TODO: What customerDao intellisense telling me?
        List<Customer> customer = customerDao.findAll();
        return customer;
    }

    @Override
    public Customer readByUsername(String username) throws ResourcePersistanceException {

        Customer customer = customerDao.findByUsername(username);
        return customer;
    }

    @Override
    public Customer update(Customer updatedCustomer) {
        if (!customerDao.update(updatedCustomer)){
            return null;
        }

        return updatedCustomer;
    }
    @Override
    public boolean delete(String username) {
        return customerDao.delete(username);
    }

    public boolean validateUsernameNotUsed(String username){
        return customerDao.checkUsername(username);
    }

    public Customer create(Customer newCustomer){
        logger.info("Customer trying to be registered: " + newCustomer);
        if(!validateInput(newCustomer)){ // checking  here if false
            // TODO: throw -important
            throw new InvalidRequestException("Username input was not validated, either empty String or null values");
        }

        // TODO: Will implement with JDBC (connecting to our database)
        if(validateUsernameNotUsed(newCustomer.getUsername())){
            throw new InvalidRequestException("Username is already in use. Please try again with another Username or login into previous made account.");
        }

        Customer persistedCustomer = customerDao.create(newCustomer);

        if(persistedCustomer == null){
            throw new ResourcePersistanceException("Customer was not persisted to the database upon registration");
        }
        logger.info("Customer has been persisted: " + newCustomer);
        return persistedCustomer;
    }



    @Override
    public boolean validateInput(Customer newCustomer) {
        logger.debug("Validating Customer: " + newCustomer);
        if(newCustomer == null) return false;
        if(newCustomer.getUsername() == null || newCustomer.getUsername().trim().equals("")) return false;
        if(newCustomer.getFname() == null || newCustomer.getFname().trim().equals("")) return false;
        if(newCustomer.getLname() == null || newCustomer.getLname().trim().equals("")) return false;
        if(newCustomer.getPassword() == null || newCustomer.getPassword().trim().equals("")) return false;
        if (newCustomer.getBalance() < 0 ) return false;
        return (newCustomer.isAdmin() == (false) || newCustomer.isAdmin() == (true)) ;
    }

    public Customer authenticateCustomer(String username, String password){

        if(password == null || password.trim().equals("") || username == null || username.trim().equals("")) {
            throw new InvalidRequestException("Either username or password is an invalid entry. Please try logging in again");
        }

        Customer authenticatedCustomer = customerDao.authenticateCustomer(username, password);

        if (authenticatedCustomer == null){
            throw new AuthenticationException("Unauthenticated user, information provided was not consistent with our database.");
        }

        return authenticatedCustomer;

    }

    public Customer authenticateAdmin(String username, String password, String isAdmin){

        if(password == null || password.trim().equals("") || username == null || username.trim().equals("") || isAdmin == null || isAdmin.trim().equals(""))  {
            throw new InvalidRequestException("Either username or password is an invalid entry. Please try logging in again");
        }

        Customer authenticatedAdmin = customerDao.authenticateAdmin(username, password, isAdmin);

        if (authenticatedAdmin == null){
            throw new AuthenticationException("Unauthenticated Admin, information provided was not consistent with our database.");
        }

        return authenticatedAdmin;

    }


}
