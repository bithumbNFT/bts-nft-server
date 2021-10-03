package net.api.cho.stockdata.stock.Wallet.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Data
@Table(name = "Wallet")
public class Wallet {
    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name="address")
    private String address;

}
