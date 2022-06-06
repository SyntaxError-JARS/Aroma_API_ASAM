package com.revature.aroma.util.interfaces;

import java.io.IOException;
import java.util.List;

// This is another form of abstraction
public interface MenuCrudable<T> {

    //we call a constant variable because by default it's final and cannot change

    // Create
    T create(T newObject);

    // Read
    List<T> findAll() throws IOException;
    T findByMenuItem(String username);

    // Update
    public boolean update(T updatedObj);

    //Delete
    boolean delete(String username);

}
