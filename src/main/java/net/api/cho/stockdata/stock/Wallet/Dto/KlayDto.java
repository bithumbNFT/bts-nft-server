package net.api.cho.stockdata.stock.Wallet.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KlayDto {

    private String from;
    private String to;
    private double value;

    @Builder
    public KlayDto(String from,String to,double value){
        this.from = from;
        this.to = to;
        this.value = value;
    }
}
