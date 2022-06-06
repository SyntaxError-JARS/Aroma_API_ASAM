package com.revature.aroma.util.interfaces;

import java.util.List;


public interface Serviceable<T> {

    // Create
    T create(T newObject);

    // Read
    List<T> readAll();

    T readByUsername(String username);

    // Update
    T update(T updatedObject);

    // Delete
    boolean delete(String username);

    boolean validateInput(T object);
}
