package com.revature.aroma.menu;

import com.revature.aroma.util.exceptions.AuthenticationException;
import com.revature.aroma.util.exceptions.InvalidRequestException;
import com.revature.aroma.util.exceptions.ResourcePersistanceException;
import com.revature.aroma.util.interfaces.Serviceable;
import com.revature.aroma.util.logging.Logger;

import java.util.List;


public class MenuServices implements Serviceable<Menu> {


        private MenuDao menuDao;
        private Logger logger = Logger.getLogger();

        public MenuServices(MenuDao menuDao) {
            this.menuDao = menuDao;
        }

        @Override
        public List<Menu> readAll(){
            // TODO: What MenuDao intellisense telling me?
            List<Menu> Menus = menuDao.findAll();
            return Menus;
        }

        @Override
        public Menu readByUsername(String item_name) throws ResourcePersistanceException {

            Menu menu = menuDao.findByUsername(item_name);
            return menu;
        }

        @Override
        public Menu update(Menu updatedMenu) {
            if (!menuDao.update(updatedMenu)){
                return null;
            }

            return updatedMenu;
        }

        @Override
        public boolean delete(String item_name) {
            return menuDao.delete(item_name);
        }

        public boolean validateEmailNotUsed(String item_name){
            return menuDao.checkItem_name(item_name);
        }

        public Menu create(Menu newMenu){
            logger.info("Menu trying to be registered: " + newMenu);
            if(!validateInput(newMenu)){ // checking if false
                // TODO: throw
                throw new InvalidRequestException("User input was not validated, either empty String or null values");
            }

            // TODO: Will implement with JDBC (connecting to our database)
            if(validateEmailNotUsed(newMenu.getItem_name())){
                throw new InvalidRequestException("User item_name is already in use. Please try again with another item_name or login into previous made account.");
            }

            Menu persistedMenu = menuDao.create(newMenu);

            if(persistedMenu == null){
                throw new ResourcePersistanceException("Menu was not persisted to the database upon registration");
            }
            logger.info("Menu has been persisted: " + newMenu);
            return persistedMenu;
        }

        @Override
        public boolean validateInput(Menu newMenu) {
            logger.debug("Validating Menu: " + newMenu);
            if(newMenu == null) return false;
            if(newMenu.getItem_name() == null || newMenu.getItem_name().trim().equals("")) return false;
            if(newMenu.getCost() <= 0)  return false;
            if(newMenu.getProtein() == null || newMenu.getProtein().trim().equals("")) return false;
            return(newMenu.isIs_substitutable() || !newMenu.isIs_substitutable()) ;

        }

        public Menu authenticateMenu(String item_name, String protein){

            if(protein == null || protein.trim().equals("") || item_name == null || item_name.trim().equals("")) {
                throw new InvalidRequestException("item_name email or protein is an invalid entry. Please try logging in again");
            }

            Menu authenticatedMenu = menuDao.authenticateMenu(item_name, protein);

            if (authenticatedMenu == null){
                throw new AuthenticationException("Unauthenticated user, information provided was not consistent with our database.");
            }

            return authenticatedMenu;

        }
    }

