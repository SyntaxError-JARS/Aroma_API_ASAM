package com.revature.aroma.creditcard;

import com.revature.aroma.util.exceptions.InvalidRequestException;
import com.revature.aroma.util.exceptions.ResourcePersistanceException;
import com.revature.aroma.util.interfaces.Serviceable;
import com.revature.aroma.util.logging.Logger;

import java.text.NumberFormat;
import java.util.List;


public class CreditCardService implements Serviceable<CreditCard> {
    private final CreditCardDao creditCardDao;


    private Logger logger = Logger.getLogger();

    public CreditCardService(CreditCardDao creditCardDao) {
        this.creditCardDao = creditCardDao;
    }

    NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

    @Override
    public CreditCard create(CreditCard newCreditCard) {
        logger.info("Customer is trying to register his/her credit-card to purchase food item : " + newCreditCard);
        if (!validateInput(newCreditCard)) {

            throw new InvalidRequestException("Credit-card was not validated, please check with manager");
        }

/*        if (creditCardLimitIsOver(newCreditCard.getLimit())) {
            throw new InvalidRequestException("Insufficient balance ");
        }*/

        CreditCard persistedCreditCard = creditCardDao.create(newCreditCard);

        if (persistedCreditCard == null) {
            throw new ResourcePersistanceException("Credit-Card data were not persisted to the databased. ");
        }
        logger.info("credit-card data has been persisted : " + newCreditCard);
        return persistedCreditCard;
    }

    public boolean creditCardLimitIsOver(double limit){
        return creditCardDao.checkLimit(limit);
    }



    @Override
    public List<CreditCard> readAll() {
        List<CreditCard> creditCard = creditCardDao.findAll();
        return creditCard;
    }

    @Override
    public CreditCard readByUsername(String username) throws ResourcePersistanceException {
        CreditCard creditCard = creditCardDao.findByUsername(username);
        return creditCard;
    }

    @Override
    public CreditCard update(CreditCard updatedCreditCard) {
        if (!creditCardDao.update(updatedCreditCard)) {
            return null;
        }
        return updatedCreditCard;
    }

    @Override
    public boolean delete(String username) {
        return creditCardDao.delete(username);
    }

    @Override
    public boolean validateInput(CreditCard newCreditCard) {
        logger.debug("Validating CreditCard: " + newCreditCard);
        if(newCreditCard == null) return false;
        if(newCreditCard.getCcNumber()== null || newCreditCard.getCcNumber().trim().equals("")) return false;
        if(newCreditCard.getCcName() == null || newCreditCard.getCcName().trim().equals("")) return false;
        if(newCreditCard.getCcv() <= 0) return false;
        if(newCreditCard.getExpDate() == null || newCreditCard.getExpDate().trim().equals("")) return false;
        if (newCreditCard.getZip() <= 0 ) return false;
        if (newCreditCard.getCreditLimit() < 0 ) return false;
        return (newCreditCard.getCustomerUsername() != null || !newCreditCard.getCustomerUsername().trim().equals("")) ;
    }


    public CreditCard addToBalance (String limit, String ccNumber){
         CreditCard creditCard = new CreditCard();
        CreditCard updatedCreditCard = creditCardDao.addToBalance(limit, ccNumber);
        if(updatedCreditCard == null){
            throw new ResourcePersistanceException("Balance was not deposited in the database, please check with the manager ");
        }
        return creditCard;
    }

    public CreditCard subtractFromBalance (String limit, String ccNumber){
        CreditCard creditCard = new CreditCard();
        CreditCard updatedCreditCard = creditCardDao.subtractFromBalance(limit, ccNumber);
        if(updatedCreditCard == null){
            throw new ResourcePersistanceException("Amount was not withdrawn from the account, please check with the manager.");
        }
        return creditCard;
    }
}










