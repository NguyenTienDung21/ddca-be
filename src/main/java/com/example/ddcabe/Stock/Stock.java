package com.example.ddcabe.Stock;

import com.example.ddcabe.OperatorStock.OperatorStock;
import com.example.ddcabe.Session.Session;
import com.example.ddcabe.User.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a stock item in the inventory.
 */
@Entity
@Table(name = "stocks", indexes = @Index(columnList = "user_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false, unique = false)
    @JsonProperty("Item ID") // Map the JSON property to the correct field
    @JsonAlias("ItemID")
    private String itemId; // Unique identifier for the stock item.

    @Column(nullable = false)
    @JsonProperty("Location") // Map the JSON property to the correct field
    @JsonAlias("Location")
    private String location; // Location of the stock item.

    @Column(nullable = false)
    @JsonProperty("Intel Lot") // Map the JSON property to the correct field
    @JsonAlias("IntelLot")
    private String lot; // Lot number associated with the stock item.

    @Column(nullable = true)
    @JsonProperty("Qty System") // Map the JSON property to the correct field
    @JsonAlias("Qty_System")
    private int quantity; // Quantity of the stock item.

    @JsonProperty("Qty Scanned") // Map the JSON property to the correct field
    @JsonAlias("Qty_Scanned")
    private int scan_quantity; // Quantity of the stock item.

    @JsonProperty("Scan_Lot") // Map the JSON property to the correct field
    private int scan_lot; // Lot Scanned by the operator.

    @Column(nullable = true)
    private Date createdAt; // Date and time when the stock item was created.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Session session; // The session associated with the stock item.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private User user; // The operator who is assigned to the stock item.

    @JsonManagedReference
    @OneToOne(mappedBy = "stock", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private OperatorStock operatorStock; // The operator stock associated with the stock item.

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        scan_quantity = 0;
        scan_lot = 0;
    }
}