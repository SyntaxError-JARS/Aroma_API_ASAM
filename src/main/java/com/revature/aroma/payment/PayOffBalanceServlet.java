package com.revature.aroma.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.aroma.creditcard.CreditCardService;
import com.revature.aroma.customer.Customer;
import com.revature.aroma.customer.CustomerServices;
import com.revature.aroma.menu.Menu;
import com.revature.aroma.menu.MenuServices;
import com.revature.aroma.order.OrderServices;
import com.revature.aroma.util.exceptions.ResourcePersistanceException;
import com.revature.aroma.util.interfaces.Authable;
import com.revature.aroma.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PayOffBalanceServlet extends HttpServlet implements Authable {

    private final PayOffBalanceService payOffBalanceService;

    private final ObjectMapper mapper;
    private final Logger logger = Logger.getLogger();

    public PayOffBalanceServlet(PayOffBalanceService payOffBalanceService, ObjectMapper mapper) {
        this.payOffBalanceService = payOffBalanceService;
        this.mapper = mapper;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");


        if (!Authable.checkAuth(req, resp)) return;

        Customer authCustomer = (Customer) req.getSession().getAttribute("authCustomer");
        Menu costMenu = (Menu) req.getSession().getAttribute("authCustomer");

        Customer reqCustomer = mapper.readValue(req.getInputStream(), Customer.class);

        if (authCustomer.getUsername().equals(reqCustomer.getUsername())) {

            double newBalance = payOffBalanceService.withdraw(costMenu.getCost(), authCustomer.getBalance());

        } else {
            resp.getWriter().write("username provided does not match the user currently logged in");
            resp.setStatus(403);
        }
    }
}
