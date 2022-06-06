package com.revature.aroma.util.web.util;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.aroma.creditcard.CreditCardDao;
import com.revature.aroma.creditcard.CreditCardService;
import com.revature.aroma.creditcard.CreditCardServlet;
import com.revature.aroma.customer.CustomerDao;
import com.revature.aroma.customer.CustomerServices;
import com.revature.aroma.customer.CustomerServlet;
import com.revature.aroma.menu.MenuDao;
import com.revature.aroma.menu.MenuServices;
import com.revature.aroma.menu.MenuServlet;
import com.revature.aroma.order.OrderDao;
import com.revature.aroma.order.OrderServices;
import com.revature.aroma.order.OrderServlet;
import com.revature.aroma.payment.PayOffBalance;
import com.revature.aroma.payment.PayOffBalanceDao;
import com.revature.aroma.payment.PayOffBalanceService;
import com.revature.aroma.payment.PayOffBalanceServlet;
import com.revature.aroma.util.web.servlets.AuthAdminServlet;
import com.revature.aroma.util.web.servlets.AuthServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
// the class never been used only the method
public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Make our single ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        // Instantiate all Dos first
        CustomerDao customerDao = new CustomerDao();
        CreditCardDao creditCardDao = new CreditCardDao();
        OrderDao orderDao = new OrderDao();
        MenuDao menuDao = new MenuDao();
        PayOffBalanceDao payOffBalanceDao = new PayOffBalanceDao();

        // Instantiate and initialize the services with their dao dependency
        CustomerServices customerServices = new CustomerServices(customerDao);
        CreditCardService creditCardService = new CreditCardService(creditCardDao);
        OrderServices orderServices = new OrderServices(orderDao);
        MenuServices menuServices = new MenuServices(menuDao);
        PayOffBalanceService payOffBalanceServices = new PayOffBalanceService(payOffBalanceDao);


        AuthServlet authServlet = new AuthServlet(customerServices, mapper);
        AuthAdminServlet authAdminServlet = new AuthAdminServlet(customerServices, mapper);
        CustomerServlet customerServlet = new CustomerServlet(customerServices, mapper);
        CreditCardServlet creditCardServlet = new CreditCardServlet(creditCardService, mapper);
        OrderServlet orderServlet = new OrderServlet(orderServices, mapper);
        MenuServlet menuServlet = new MenuServlet(menuServices, mapper);
        PayOffBalanceServlet payOffBalanceServlet =new PayOffBalanceServlet(payOffBalanceServices, mapper);


        ServletContext context = sce.getServletContext();
        context.addServlet("AuthServlet", authServlet).addMapping("/auth");
        context.addServlet("AuthAdminServlet", authAdminServlet).addMapping("/authAdmin");
        context.addServlet("CustomerServlet", customerServlet).addMapping("/customers/*");
        context.addServlet("CreditCardServlet", creditCardServlet).addMapping("/creditCards/*");
        context.addServlet("OrderServlet", orderServlet).addMapping("/orders/*");
        context.addServlet("MenuServlet", menuServlet).addMapping("/menus/*");
        context.addServlet("PaymentServlet", payOffBalanceServlet ).addMapping("/payment/*");



    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}

