package com.revature.aroma.order;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_o")
public class Order {
    @Id
    private String id;
    @Column(name = "menu_item", length = 50, nullable = false)
    private String menu_item;
    @Column(name = "comment_t")
    private String Comment_t;
    @Column(name = "isfavorite",  columnDefinition = "tinyint default 1") // default false
    private boolean isfavorite;
    @Column(name = "order_date", length = 15, nullable = false)
    private String orderDate;
    @Column(name = "customer_username", length = 25, nullable = false)
    private String customer_username;

    public Order(String id, String menu_item, String Comment_t, boolean isfavorite, String orderDate, String customer_username) {
        this.id = id;
        this.menu_item = menu_item;
        this.Comment_t = Comment_t;
        this.isfavorite = isfavorite;
        this.orderDate = orderDate;
        this.customer_username = customer_username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return isfavorite == order.isfavorite && Objects.equals(id, order.id) && Objects.equals(menu_item, order.menu_item) && Objects.equals(Comment_t, order.Comment_t) && Objects.equals(orderDate, order.orderDate) && Objects.equals(customer_username, order.customer_username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menu_item, Comment_t, isfavorite, orderDate, customer_username);
    }

    public Order() {};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu_item() {
        return menu_item;
    }

    public void setMenu_item(String menu_item) {
        this.menu_item = menu_item;
    }

    public String getComment_t() {
        return Comment_t;
    }

    public void setComment_t(String comment_t) {
        this.Comment_t = comment_t;
    }

    public boolean getIsFavorite() {
        return isfavorite;
    }

    public void setIsfavorite(boolean isfavorite) {
        this.isfavorite = this.isfavorite;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomer_username() {
        return customer_username;
    }

    public void setCustomer_username(String customer_username) {
        this.customer_username = customer_username;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", menu_item='" + menu_item + '\'' +
                ", comment='" + Comment_t + '\'' +
                ", isFavorite=" + isfavorite +
                ", orderDate='" + orderDate + '\'' +
                ", customer_username='" + customer_username + '\'' +
                '}';
    }
}
