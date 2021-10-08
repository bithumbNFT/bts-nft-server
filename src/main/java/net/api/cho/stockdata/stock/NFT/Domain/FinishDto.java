package net.api.cho.stockdata.stock.NFT.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FinishDto {
    private String id;
    private Integer value;
    private String owner;
    private String user;
    private Auction auction;
}
