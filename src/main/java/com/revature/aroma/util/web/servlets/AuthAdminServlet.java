package com.revature.aroma.util.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.aroma.customer.Customer;
import com.revature.aroma.customer.CustomerServices;
import com.revature.aroma.util.exceptions.AuthenticationException;
import com.revature.aroma.util.exceptions.InvalidRequestException;
import com.revature.aroma.util.interfaces.Authable;
import com.revature.aroma.util.web.dto.LoginCreds;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthAdminServlet extends HttpServlet {

    private final CustomerServices customerServices;
    // ObjectMapper provided by jackson
    private final ObjectMapper mapper;

    public AuthAdminServlet(CustomerServices customerServices, ObjectMapper mapper){
        this.customerServices = customerServices;
        this.mapper = mapper;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        try {
            // The jackson library has the ObjectMapper with methods to readValues from the HTTPRequest body as an input stream and assign it to the class

            LoginCreds loginCreds = mapper.readValue(req.getInputStream(), LoginCreds.class);

            System.out.println(loginCreds.getIsAdmin());

            Customer authAdmin = customerServices.authenticateAdmin(loginCreds.getUsername(), loginCreds.getPassword(),loginCreds.getIsAdmin());

            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("authAdmin", authAdmin);

            resp.getWriter().write("You have successfully logged in!");
        } catch (AuthenticationException | InvalidRequestException e){
            resp.setStatus(404);
            resp.getWriter().write(e.getMessage());
        } catch (Exception e){
            resp.setStatus(500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.getWriter().write("Admin has logged out!");
    }
}
