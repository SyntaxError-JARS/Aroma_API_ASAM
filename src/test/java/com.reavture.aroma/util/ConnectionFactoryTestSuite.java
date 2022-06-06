package com.reavture.aroma.util;

import com.revature.aroma.util.ConnectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class ConnectionFactoryTestSuite {

    @Test
    public void test_getConnection_givenProvidedCredentials_returnValidConnection(){
        // Arrange & Acting
        try(Connection conn = ConnectionFactory.getInstance().getConnection();){
            System.out.println(conn);

            // Assert
            Assertions.assertNotNull(conn);
        } catch (Exception e){
            e.printStackTrace();
        }

    }



}
