package com.revature.aroma.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.aroma.util.exceptions.ResourcePersistanceException;
import com.revature.aroma.util.interfaces.Authable;
import com.revature.aroma.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MenuServlet extends HttpServlet implements Authable {


        private final MenuServices menuServices;
        private final ObjectMapper mapper;
        private final Logger logger = Logger.getLogger();

        public MenuServlet(MenuServices menuServices, ObjectMapper mapper) {
            this.menuServices = menuServices;
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
            super.doOptions(req, resp);
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
            resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

            //if(!Authable.checkAuth(req, resp)) return;

            if (req.getParameter("item_name") != null) {
                Menu menu;
                try {
                    menu = menuServices.readByUsername(req.getParameter("item_name"));
                } catch (ResourcePersistanceException e) {
                    logger.warn(e.getMessage());
                    resp.setStatus(404);
                    resp.getWriter().write(e.getMessage());
                    return;
                }
                String payload = mapper.writeValueAsString(menu);
                resp.getWriter().write(payload);
                return;
            }

            List<Menu> menus = menuServices.readAll();
            String payload = mapper.writeValueAsString(menus);

            resp.getWriter().write(payload);
        }
        @Override
        protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            super.doOptions(req, resp);
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
            resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

            //if(!Authable.checkAuth(req, resp)) return;
            //Menu authMenu = (Menu) req.getSession().getAttribute("authMenu");

            Menu reqMenu = mapper.readValue(req.getInputStream(), Menu.class);

            if(reqMenu.getItem_name()!= null) {

                Menu updatedMenu = menuServices.update(reqMenu);

                String payload = mapper.writeValueAsString(updatedMenu);
                resp.getWriter().write(payload);
            } else {
                resp.getWriter().write("user provided does not match the user currently logged in");
                resp.setStatus(403);
            }

        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            super.doOptions(req, resp);
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
            resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

            //if (!Authable.checkAuth(req, resp)) return;
                try {
                    Menu menu = mapper.readValue(req.getInputStream(), Menu.class); // from JSON to Java Object (Pokemon)
                    Menu persistedMenu = menuServices.create(menu);

                    String payload = mapper.writeValueAsString(persistedMenu); // Mapping from Java Object  to JSON

                    resp.getWriter().write("Persisted the provided menu as show below \n");
                    resp.getWriter().write(payload);
                    resp.setStatus(201);
                }catch(Exception e){
                    resp.getWriter().write("Menu you trying to add is already in the database. \n");
                }

        }

        @Override
        protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            super.doOptions(req, resp);
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE");
            resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

            //if(!Authable.checkAuth(req,resp)) return;
            if(req.getParameter("item_name") == null){
                resp.getWriter().write("In order to delete, please provide your user item_name as a verification into the url with ?item_name");
                resp.setStatus(401);
                return;
            }

            String itemName = req.getParameter("item_name");

            try {
                menuServices.delete(itemName);
                resp.getWriter().write("Menu " + itemName + " was deleted from the database.");
                req.getSession().invalidate();
            } catch (ResourcePersistanceException e){
                resp.getWriter().write(e.getMessage());
                resp.setStatus(404);
            } catch (Exception e){
                resp.getWriter().write(e.getMessage());
                resp.setStatus(500);
            }
        }
    }
