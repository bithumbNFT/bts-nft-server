package net.api.cho.stockdata.stock.Price;


import lombok.RequiredArgsConstructor;
import net.api.cho.stockdata.stock.NFT.Service.NFTService;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RequiredArgsConstructor
@RestController

public class PriceController {
    private final NFTService NFTService;
    private final PriceService priceService;

    @GetMapping("/klayapi")
    public ResponseEntity<HashMap<String,Object>> nowprice() throws IOException, ParseException {
        return ResponseEntity.ok(priceService.init());
    }

}
