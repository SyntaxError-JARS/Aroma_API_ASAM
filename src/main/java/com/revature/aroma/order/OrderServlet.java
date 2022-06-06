package com.revature.aroma.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.aroma.util.exceptions.ResourcePersistanceException;
import com.revature.aroma.util.interfaces.Authable;
import com.revature.aroma.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;



public class OrderServlet extends HttpServlet implements Authable {
    private final OrderServices orderServices;
    private final Logger logger = Logger.getLogger();
    private final ObjectMapper mapper;

    public OrderServlet(OrderServices orderServices, ObjectMapper mapper) {
        this.orderServices = orderServices;
        this.mapper = mapper;
    }
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        //if (!Authable.checkAuth(req, resp)) return;

        if (req.getParameter("menu_item") != null) {
            Order order;
            try {
                order = orderServices.readByMenuItem(req.getParameter("menu_item")); // EVERY PARAMETER RETURN FROM A URL IS A STRING
            } catch (ResourcePersistanceException e) {
                logger.warn(e.getMessage());
                resp.setStatus(404);
                resp.getWriter().write(e.getMessage());
                return;
            }
                String payload = mapper.writeValueAsString(order);
                resp.getWriter().write(payload);
                return;
        }

        List<Order> orders = orderServices.readAll();
        String payload = mapper.writeValueAsString(orders);

        resp.getWriter().write(payload);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if (!Authable.checkAuth(req, resp)) return;
           try {
               Order order = mapper.readValue(req.getInputStream(), Order.class); // from JSON to Java Object (Pokemon)
               Order persistedOrder = orderServices.create(order);

               String payload = mapper.writeValueAsString(persistedOrder); // Mapping from Java Object (Order) to JSON
               resp.getWriter().write("Persisted the provided menu_item as show below \n");
               resp.getWriter().write(payload);
               resp.setStatus(201);
           }catch(Exception e) {
        resp.getWriter().write("This order is already in the database if not, please check your data.\n");
    }


    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // this is for login, which consider you registered and authenticated user
        if (!Authable.checkAuth(req, resp)) return;

        Order authOrder = (Order) req.getSession().getAttribute("authOrder");

        Order reqOrder = mapper.readValue(req.getInputStream(), Order.class);

        if (authOrder.getMenu_item().equals(reqOrder.getMenu_item())) {

            Order updatedOrder = orderServices.update(reqOrder);

            String payload = mapper.writeValueAsString(updatedOrder);
            resp.getWriter().write(payload);
        } else {
            resp.getWriter().write("menu_item provided does not match the user currently logged in");
            resp.setStatus(403);
        }

    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if (!Authable.checkAuth(req, resp)) return;
        if (req.getParameter("menu_item") == null) {
            resp.getWriter().write("In order to delete, please provide your menu_item as a verification into the url with ?menu_item=your menu_item");
            resp.setStatus(401);
            return;
        }

        String menu_item = req.getParameter("menu_item");
        Order authOrder = (Order) req.getSession().getAttribute("authOrder");

        if (!authOrder.getMenu_item().equals(menu_item)) {
            System.out.println(authOrder.getMenu_item());
            System.out.println(menu_item);


            resp.getWriter().write("Order provided does not match the user logged in, double check for confirmation of deletion");
            return;
        }

        try {
            orderServices.delete(menu_item);
            resp.getWriter().write("Delete menu_item from the database");
                    req.getSession().invalidate();
        } catch (ResourcePersistanceException e) {
            resp.getWriter().write(e.getMessage());
            resp.setStatus(404);
        } catch (Exception e) {
            resp.getWriter().write(e.getMessage());
            resp.setStatus(500);
        }
    }

    protected boolean checkAuth(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession httpSession = req.getSession();
        if(httpSession.getAttribute("authCustomer") == null){
            resp.getWriter().write("Unauthorized request - not log in as registered user");
            resp.setStatus(401); // Unauthorized
            return false;
        }
        return true;
    }

}
