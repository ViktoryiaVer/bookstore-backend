package com.somecompany.bookstore.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "book_price")
    private BigDecimal bookPrice;
    @Column(name = "book_quantity")
    private Integer bookQuantity;
}
