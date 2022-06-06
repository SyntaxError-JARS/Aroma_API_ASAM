package com.revature.aroma.util.interfaces;

import java.util.List;


public interface MenuServiceable<T> {

    // Create
    T create(T newObject);

    // Read
    List<T> readAll();

    T readByMenuItem(String username);

    // Update
    T update(T updatedObject);

    // Delete
    boolean delete(String username);

    boolean validateInput(T object);
}
