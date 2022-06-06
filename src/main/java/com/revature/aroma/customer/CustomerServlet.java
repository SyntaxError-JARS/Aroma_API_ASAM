package com.revature.aroma.customer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.aroma.util.exceptions.ResourcePersistanceException;
import com.revature.aroma.util.interfaces.AdminAuthable;
import com.revature.aroma.util.interfaces.Authable;
import com.revature.aroma.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// @WebServlet("/customers")
public class CustomerServlet extends HttpServlet implements Authable {

    private final CustomerServices customerServices;
    private final ObjectMapper mapper;
    private final Logger logger = Logger.getLogger();

    public CustomerServlet(CustomerServices customerServices, ObjectMapper mapper) {
        this.customerServices = customerServices;
        this.mapper = mapper;
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        //if (!Authable.checkAuth(req, resp)) return;

        // Handling the query params in the endpoint /customers?id=x
        if (req.getParameter("fname") != null) {
            Customer customer;
            try {
                customer = customerServices.readByUsername(req.getParameter("fname"));
            } catch (ResourcePersistanceException e) {
                logger.warn(e.getMessage());
                resp.setStatus(404);
                resp.getWriter().write(e.getMessage());
                return;
            }
            String payload = mapper.writeValueAsString(customer);
            resp.getWriter().write(payload);
            return;
        }

        List<Customer> customers = customerServices.readAll();
        String payload = mapper.writeValueAsString(customers);

        resp.getWriter().write(payload);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

    // this is for login, which consider you registered and authenticated user
        if (!Authable.checkAuth(req, resp)) return;

        Customer authCustomer = (Customer) req.getSession().getAttribute("authCustomer");

        Customer reqCustomer = mapper.readValue(req.getInputStream(), Customer.class);

        if (authCustomer.getUsername().equals(reqCustomer.getUsername())) {

            Customer updatedCustomer = customerServices.update(reqCustomer);

            String payload = mapper.writeValueAsString(updatedCustomer);
            resp.getWriter().write(payload);
        } else {
            resp.getWriter().write("username provided does not match the user currently logged in");
            resp.setStatus(403);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        //if (!Authable.checkAuth(req, resp)) return;

        //if (!AdminAuthable.checkAuth(req, resp)) return;

                try {
                    Customer customer = mapper.readValue(req.getInputStream(), Customer.class); // from JSON to Java Object (Pokemon)
                    Customer persistedCustomer = customerServices.create(customer);

                    String payload = mapper.writeValueAsString(persistedCustomer); // Mapping from Java Object (Customer) to JSON
                    resp.getWriter().write("Persisted the provided Customer as show below \n");
                    resp.getWriter().write(payload);
                    resp.setStatus(201);
                }catch(Exception e){
                    resp.getWriter().write("Customer is already registered. \n");
                }


    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if (!Authable.checkAuth(req, resp)) return;
        if (req.getParameter("username") == null) {
            resp.getWriter().write("In order to delete, please provide your user username as a verification into the url with ?username=your username");
            resp.setStatus(401);
            return;
        }

        String username = req.getParameter("username");
        Customer authCustomer = (Customer) req.getSession().getAttribute("authCustomer");
        System.out.println(authCustomer);

        if (!authCustomer.getUsername().equals(username)) {
            System.out.println(authCustomer.getUsername());
            System.out.println(username);


            resp.getWriter().write("Username provided does not match the user logged in, double check for confirmation of deletion");
            return;
        }

        try {
            customerServices.delete(username);
            String fname = req.getParameter("fname");
            String lname = req.getParameter("fname");
            resp.getWriter().write("Customer" + fname +" " + lname  +" is delete from the database");
            req.getSession().invalidate();
        } catch (ResourcePersistanceException e) {
            resp.getWriter().write(e.getMessage());
            resp.setStatus(404);
        } catch (Exception e) {
            resp.getWriter().write(e.getMessage());
            resp.setStatus(500);
        }
    }
}
