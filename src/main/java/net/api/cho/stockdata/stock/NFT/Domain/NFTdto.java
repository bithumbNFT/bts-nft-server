package net.api.cho.stockdata.stock.NFT.Domain;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class NFTdto {

    private String id;
    private String from;
    private String to;

    public NFTdto(String id, String from, String to){
        this.id = id;
        this.from = from;
        this.to = to;
    }
}
