package net.api.cho.stockdata.stock.NFT.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MakeNFTdto {
    private String name;
    private String description;
    private String image;
    private String owner;
    private Auction auction;
    private String price;
}
