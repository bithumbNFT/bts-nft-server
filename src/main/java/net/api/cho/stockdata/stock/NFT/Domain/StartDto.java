package net.api.cho.stockdata.stock.NFT.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StartDto {
    private String nftid;
    private Auction auction;

    public StartDto(String nftid, Auction auction){
        this.nftid = nftid;
        this.auction=auction;
    }
}
