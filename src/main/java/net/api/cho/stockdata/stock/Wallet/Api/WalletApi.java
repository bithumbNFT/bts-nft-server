package net.api.cho.stockdata.stock.Wallet.Api;

import lombok.RequiredArgsConstructor;
import net.api.cho.stockdata.stock.Wallet.Dto.WalletDto;
import net.api.cho.stockdata.stock.Wallet.Service.WalletService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class WalletApi  {
    private final RestTemplate restTemplates;
    private final String chain_id = "1001";
    private final String Auth = "Basic S0FTSzA0QTRYNEI1WVJBTE1EUDBUTzUzOnRTQ1VacVhoLUt4a0dnTklwdHZVQWV5aGtodnFLMmFHeVVKMHAtblg=";
    private final String URL = "https://wallet-api.klaytnapi.com/v2/account";

    public Object CheckWallet() throws IOException{                                     // 지금 pool 안에 생성되어 있는 모든 지갑을 조회
        final HttpHeaders headers = new HttpHeaders();
        headers.set("x-chain-id",chain_id);
        headers.set("Authorization",Auth);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplates.exchange(URL,HttpMethod.GET,entity,String.class);
    }
    public WalletDto CreateWallet() throws IOException{                                 // 지금 pool 안에 지갑을 생성
        final HttpHeaders headers = new HttpHeaders();
        headers.set("x-chain-id",chain_id);
        headers.set("Authorization",Auth);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplates.exchange(URL,HttpMethod.POST,entity,WalletDto.class).getBody();
    }
    public Optional<Double> muchWallet(String account) throws IOException, ParseException {     // 지금 지갑안에 클레이가 얼마나 있는지 조회
        String url = "https://node-api.klaytnapi.com/v1/klaytn";
        final HttpHeaders headers = new HttpHeaders();
        headers.set("x-chain-id", chain_id);
        headers.set("Authorization", Auth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject request = new JSONObject();
        JSONArray array = new JSONArray();
        array.add(account);
        array.add("latest");
        request.put("id", 1);
        request.put("jsonrpc", "2.0");
        request.put("method","klay_getBalance");
        request.put("params", array);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        System.out.println(request.toString());
        ResponseEntity<String> response = restTemplates.postForEntity(url,entity,String.class);
        String jText = response.getBody().toString();
        System.out.println(jText);
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(jText);
        System.out.println(obj.get("result"));
        String klayhex =  obj.get("result").toString();
        Optional<Double> remainklay = Optional.ofNullable(HexCalculator(klayhex));
        return remainklay;
    }
    public double HexCalculator(String hex){                                   //조회되어 나온 클레이를 정수형으로 변형하는 함수
        double result = 0;
        for (int i=0; i<=hex.length()-1;i++)
        {
            if(hex.charAt(i)>='A' && hex.charAt(i)<='F')
                result = result * 16 + hex.charAt(i) - 'A' + 10;
            else if(hex.charAt(i)>='a' && hex.charAt(i) <= 'f')
                result = result * 16 + hex.charAt(i) -'a' + 10;
            else if (hex.charAt(i) >='0' && hex.charAt(i) <= '9')
                result = result * 16 + hex.charAt(i) - '0';

        }
        return (result/Math.pow(10,18));
    }

}
