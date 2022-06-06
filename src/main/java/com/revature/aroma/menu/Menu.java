package com.revature.aroma.menu;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "menu")

public class Menu {
    @Id
    @Column(name = "item_name", length = 50)
    private String item_name;

    @Column(name = "cost", nullable = false)
    private double cost;

    @Column(name = "protein")
    private String protein;

    @Column(name = "is_substituable") // default false
    private boolean is_substitutable;

    public Menu(){};
    public Menu (String item_name, double cost, String protein, boolean is_substituable){
        super();
        this.item_name = item_name;
        this.cost = cost;
        this.protein = protein;
        this.is_substitutable = is_substituable;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Double.compare(menu.cost, cost) == 0 && is_substitutable == menu.is_substitutable && Objects.equals(item_name, menu.item_name) && Objects.equals(protein, menu.protein);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item_name, cost, protein, is_substitutable);
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public boolean isIs_substitutable() {
        return is_substitutable;
    }

    public void setIs_substitutable(boolean is_substitutable) {
        this.is_substitutable = is_substitutable;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "item_name='" + item_name + '\'' +
                ", cost=" + cost +
                ", protein='" + protein + '\'' +
                ", is_substituable=" + is_substitutable +
                '}';
    }
}
