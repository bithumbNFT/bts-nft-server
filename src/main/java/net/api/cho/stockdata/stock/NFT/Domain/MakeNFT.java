package net.api.cho.stockdata.stock.NFT.Domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "NFTLOG")
@NoArgsConstructor
public class MakeNFT {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NO")
    private Integer NO;
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "image")
    private String image;
    @Column(name = "owner")
    private String owner;
    @Column(name = "Date")
    private String date;
    @Column(name = "Auction")
    private Auction auction;
    @Column(name = "startprice")
    private String price;
    @Column(name = "imagepath")
    private String imagepath;

    @Builder
    public MakeNFT( String id, String name, String image,String description, String owner, String imagepath,Auction auction, String price)
    {
        this.id= id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.owner = owner;
        this.imagepath = imagepath;
        this.auction = auction;
        this.price = price;
    }

}
