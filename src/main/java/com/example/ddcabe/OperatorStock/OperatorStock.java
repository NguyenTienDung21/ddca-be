package com.example.ddcabe.OperatorStock;

import com.example.ddcabe.Stock.Stock;
import com.example.ddcabe.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/*
This class represents the entity OperatorStock, which maps to the database table "operators_stocks".
It is annotated with @Entity to indicate that it is a persistent entity in the database.
The @Table annotation specifies the name of the database table to which this entity is mapped.
The @Data annotation is provided by Lombok and automatically generates getters, setters, equals, hashCode, and toString methods.
*/
@Entity
@Table(name = "operators_stocks")
@Data
@NoArgsConstructor
public class  OperatorStock {
    /*
     * The id field represents the primary key of the OperatorStock entity.
     * It is annotated with @Id to indicate that it is the primary key.
     * The @GeneratedValue annotation specifies the generation strategy for the primary key.
     * In this case, it uses the "uuid2" generator which generates a UUID value.
     * The @GenericGenerator annotation is used to specify the name and strategy of the generator.
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    /*
     * The operator field represents the association with the User entity.
     * It is annotated with @ManyToOne to indicate a many-to-one relationship with the User entity.
     * The @JoinColumn annotation specifies the name of the foreign key column in the database table that references the User entity.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User operator;

    /*
     * The stock field represents the association with the Stock entity.
     * It is annotated with @ManyToOne to indicate a one-to-one relationship with the Stock entity.
     * The @JoinColumn annotation specifies the name of the foreign key column in the database table that references the Stock entity.
     */
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "stock_id")
    @JsonBackReference
    private Stock stock;

    private int quantityOperator; // Quantity of the stock scanned

    private Date createdAt; // Date and time of creation of the OperatorStock entity

    private int scan_lot; // Lot Scanned by the operator.

    public OperatorStock(Optional<User> operator, Optional<Stock> stock, int quantity) {
        this.operator = operator.orElse(null);
        this.stock = stock.orElse(null);
        this.quantityOperator = quantity;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
