package com.revature.aroma.creditcard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.aroma.customer.Customer;
import com.revature.aroma.util.exceptions.ResourcePersistanceException;
import com.revature.aroma.util.interfaces.Authable;
import com.revature.aroma.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CreditCardServlet extends HttpServlet implements Authable {

    private final CreditCardService creditcardService;
    private final ObjectMapper mapper;
    private final Logger logger = Logger.getLogger();

    public CreditCardServlet(CreditCardService creditCardService, ObjectMapper mapper) {
        this.creditcardService = creditCardService;
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

        if (!Authable.checkAuth(req, resp)) return;

        // Handling the query params in the endpoint /customers?id=x
        if (req.getParameter("cc_name") != null) {
            CreditCard creditCard;
            try {
                creditCard = creditcardService.readByUsername(req.getParameter("cc_name"));
            } catch (ResourcePersistanceException e) {
                logger.warn(e.getMessage());
                resp.setStatus(404);
                resp.getWriter().write(e.getMessage());
                return;
            }
            String payload = mapper.writeValueAsString(creditCard);
            resp.getWriter().write(payload);
            return;
        }
        List<CreditCard> creditCrads = creditcardService.readAll();
        String payload = mapper.writeValueAsString(creditCrads);

        resp.getWriter().write(payload);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if (!Authable.checkAuth(req, resp)) return;

        CreditCard reqCreditCard = mapper.readValue(req.getInputStream(), CreditCard.class);
        try {
            if (reqCreditCard.getCcNumber() != null) {
                CreditCard updatedCreditCard = creditcardService.update(reqCreditCard);

                String payload = mapper.writeValueAsString(updatedCreditCard);
                resp.getWriter().write(payload);
            } else {
                resp.getWriter().write("\"In order to update, please provide your user credit card number into the url with ? cc_number=your cc_number\"");
                resp.setStatus(403);
            }
        }catch(Exception e) {
            resp.getWriter().write("\"In order to update, please login with your credential or provide your username\"");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if (!Authable.checkAuth(req, resp)) return;

           try {
               CreditCard creditCard = mapper.readValue(req.getInputStream(), CreditCard.class); // from JSON to Java Object (Pokemon)
               CreditCard persistedCustomer = creditcardService.create(creditCard);

               String payload = mapper.writeValueAsString(persistedCustomer); // Mapping from Java Object (Customer) to JSON
               resp.getWriter().write("Persisted the provided CreditCard as show below \n");
               resp.getWriter().write(payload);
               resp.setStatus(201);
           }catch(Exception e) {
               resp.getWriter().write("Credit card is already registered ");
           }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if (!Authable.checkAuth(req, resp)) return;


            if (req.getParameter("cc_number") == null) {

                resp.getWriter().write("In order to delete, please provide your user credit card number as a verification into the url with ?cc_number=your cc_number");
                resp.setStatus(401);
                return;
            }

            String ccNumber = req.getParameter("cc_number");

            try {
                creditcardService.delete(ccNumber);
                resp.getWriter().write("Credit card was deleted from the database");
                req.getSession().invalidate();
            } catch (ResourcePersistanceException e) {
                resp.getWriter().write(e.getMessage());
                resp.setStatus(404);
            } catch (Exception e) {
                //resp.getWriter().write(e.getMessage());
                resp.getWriter().write("Credit card provided was already deleted. ");
                resp.setStatus(500);
            }

    }

 }