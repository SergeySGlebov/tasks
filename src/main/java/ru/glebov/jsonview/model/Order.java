package ru.glebov.jsonview.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import ru.glebov.jsonview.Views;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customer_order")
public class Order {
    @Id
    @GeneratedValue
    @JsonView(Views.UserDetails.class)
    private Integer id;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @JsonView(Views.UserDetails.class)
    private List<String> goods = new ArrayList<>();

    @JsonView(Views.UserDetails.class)
    private BigDecimal orderAmount;

    @Enumerated(EnumType.STRING)
    @JsonView(Views.UserDetails.class)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getGoods() {
        return goods;
    }

    public void setGoods(List<String> goods) {
        this.goods = goods;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Order order = (Order) object;
        return Objects.equals(id, order.id) && Objects.equals(goods, order.goods) && Objects.equals(orderAmount, order.orderAmount) && orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, goods, orderAmount, orderStatus);
    }
}
