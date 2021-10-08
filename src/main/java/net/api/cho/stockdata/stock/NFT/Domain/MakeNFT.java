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
    private String id;
    private String name;
    private String description;
    private String image;
    private String owner;
    private String date;
    private Auction auction;
    @Column(name = "startprice")
    private String price;
    private String imagepath;
    private Integer term;
    @Builder
    public MakeNFT( String id, String name, String image,String description, String owner, String imagepath,Auction auction, String price, Integer term)
    {
        this.id= id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.owner = owner;
        this.imagepath = imagepath;
        this.auction = auction;
        this.price = price;
        this.term = term;
    }

}
