package net.api.cho.stockdata.stock.Price;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;
import reactor.core.publisher.Flux;

@Service
public class Priceapi {
    public HashMap<String,Object> callApi() throws IOException,ParseException {
        StringBuilder result = new StringBuilder();

        String urlstr = "https://api.bithumb.com/public/ticker/klay_KRW";  // 기준시간 - 시가 - 종가 - 고가 - 저가 - 거래량
        URL url = new URL(urlstr);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br;

        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        Flux<String> flux = Flux.just(br.readLine());

        flux.subscribe(result::append);

        urlConnection.disconnect();

        String jText = result.toString();

        JSONParser parser = new JSONParser();

        JSONObject obj = (JSONObject) parser.parse(jText);

        System.out.println(obj);

        Object status = obj.get("status");
        String nowprice = nowprice();
        System.out.println(status.toString());

        Object parseResponse = obj.get("data");
        Map<String,Object> data = (Map<String, Object>) parseResponse;
        System.out.println(data);
        List<Map<String,Object>> currentdata = new ArrayList<>();
        HashMap<String,Object> request = new JSONObject();
        HashMap<String,Object> addrequest = new HashMap<>();
        request.put("now_price",nowprice);
        request.put("time_stamp",data.get("date"));
        request.put("start_price",data.get("opening_price"));
        request.put("end_price",data.get("closing_price"));
        request.put("highest_price",data.get("max_price"));
        request.put("lowest_price",data.get("min_price"));
        request.put("trading_volume",data.get("units_traded"));
        request.put("fluctate_24H",data.get("fluctate_24H"));
        request.put("fluctate_rate_24H",data.get("fluctate_rate_24H"));

        addrequest.put("data",request);


        return addrequest;
    }

    public String nowprice() throws ParseException,IOException{
        StringBuilder result = new StringBuilder();
        String urlstr = "https://api.bithumb.com/public/transaction_history/klay_KRW";
        URL url = new URL(urlstr);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        Flux<String> flux = Flux.just(br.readLine());
        flux.subscribe(result::append);
        urlConnection.disconnect();
        String jText = result.toString();

        JSONParser parser = new JSONParser();

        JSONObject obj = (JSONObject) parser.parse(jText);

        JSONArray parseResponse = (JSONArray) obj.get("data");

        Map<String,Object> data = (Map<String, Object>) parseResponse.get(parseResponse.size()-1);

        String nowprice = data.get("price").toString();

        System.out.println(nowprice);

        return nowprice;

    }
}
