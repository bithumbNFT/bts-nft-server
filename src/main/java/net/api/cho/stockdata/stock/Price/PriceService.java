package net.api.cho.stockdata.stock.Price;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

public interface PriceService {
    HashMap<String,Object> init() throws IOException, ParseException;
}
