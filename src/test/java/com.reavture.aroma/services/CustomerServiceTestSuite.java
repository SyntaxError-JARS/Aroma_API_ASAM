package com.reavture.aroma.services;

import com.revature.aroma.customer.Customer;
import com.revature.aroma.customer.CustomerDao;
import com.revature.aroma.customer.CustomerServices;
import com.revature.aroma.util.exceptions.AuthenticationException;
import com.revature.aroma.util.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


public class CustomerServiceTestSuite {

    CustomerServices sut;
    CustomerDao mockCustomerDao;

   @BeforeEach
    public void testPrep(){
        // in order to specify and mock a dao
        mockCustomerDao = mock(CustomerDao.class);
        sut = new CustomerServices(mockCustomerDao);
    }

    @Test
    public void test_validateInput_givenValidCustomer_returnTrue(){

        // Arrange
        Customer customer = new Customer("Marklwise", "Mark", "luise","mlwise",0,false);

        // Act
        boolean actualResult = sut.validateInput(customer);

        // Assert
        Assertions.assertTrue(actualResult);

    }

    @Test
    public void test_create_givenValidUser_returnsCustomer(){
        // Arrange
        Customer customer = new Customer("Jhonsonp", "Jhonson", "Jester","jjester",1000,false);
        // THe below code ensures that the services can continue execution and get expected results from the dao without any issues
        when(mockCustomerDao.create(customer)).thenReturn(customer);

        // Act
        Customer actualCustomer = sut.create(customer);

        // Assert
        Assertions.assertEquals("Jhonsonp", actualCustomer.getUsername());
        Assertions.assertEquals("Jhonson", actualCustomer.getFname());
        Assertions.assertEquals("Jester", actualCustomer.getLname());
        Assertions.assertEquals("jjester", actualCustomer.getPassword());
        Assertions.assertEquals(1000, actualCustomer.getBalance());
        Assertions.assertEquals(false, actualCustomer.isAdmin());


        // Mockito is verifying that the creation method was executed only once!
        verify(mockCustomerDao, times(1)).create(customer);
    }
    @Test
    public void test_create_givenInvalidUser_throwsInvalidRequestException(){
        // Arrange
        Customer customer = new Customer(" "," ", "Jester","jjester",100,false);
        when(mockCustomerDao.create(customer)).thenReturn(customer);


        // Assert
        Assertions.assertThrows(InvalidRequestException.class, () -> { sut.create(customer); });
        verify(mockCustomerDao, times(1)).create(customer);
    }

    @Test
    public void test_create_givenRepeatedUserInformation_throwsInvalidRequestException(){
        Customer customer = new Customer("Jhonsonp", "Jhonson", "Jester","jjester",1000,false);
        when(mockCustomerDao.checkUsername(customer.getUsername())).thenReturn(true);


        // Assert
        Assertions.assertThrows(InvalidRequestException.class, () -> { sut.create(customer);});
        verify(mockCustomerDao, times(0)).create(customer);
    }

    @Test
    public void test_authenticateCustomer_givenInvalidInformation_throwsAuthenticationException(){
        Customer customer = new Customer("Jhonsonp", "Jhonson", "Jester","jjester",1000,false);
        when(mockCustomerDao.authenticateCustomer(customer.getUsername(), customer.getPassword())).thenReturn(null);


        Assertions.assertThrows(AuthenticationException.class, () -> { sut.authenticateCustomer(customer.getUsername(), customer.getPassword());});
        verify(mockCustomerDao, times(1)).authenticateCustomer(customer.getUsername(), customer.getPassword());
    }

}
