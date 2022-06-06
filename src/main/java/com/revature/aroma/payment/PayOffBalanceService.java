package com.revature.aroma.payment;

import com.revature.aroma.customer.Customer;
import com.revature.aroma.customer.CustomerDao;
import com.revature.aroma.util.logging.Logger;

public class PayOffBalanceService {

    private PayOffBalanceDao payOffBalanceDao;

    private Logger logger = Logger.getLogger();

   public PayOffBalanceService(PayOffBalanceDao payOffBalanceDao) {
        this.payOffBalanceDao = payOffBalanceDao;
    }

    public double withdraw(double cost, double balance)  {
       boolean  newBalance;
        if(balance < cost) {
            System.out.println();
        } else {
            balance -= cost;
          // boolean newBalance = payOffBalanceDao.update(balance);
        }
        return 1;
    }
}
