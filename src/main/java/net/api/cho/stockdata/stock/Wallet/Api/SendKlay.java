package net.api.cho.stockdata.stock.Wallet.Api;

import lombok.RequiredArgsConstructor;
import net.api.cho.stockdata.stock.Wallet.Dto.KlayDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class SendKlay {
    private final RestTemplate restTemplates;
    private final String chain_id = "1001";
    private final String Auth = "Basic S0FTSzA0QTRYNEI1WVJBTE1EUDBUTzUzOnRTQ1VacVhoLUt4a0dnTklwdHZVQWV5aGtodnFLMmFHeVVKMHAtblg=";
    private final String URL = "https://wallet-api.klaytnapi.com/v2/tx/value";

    public HashMap<String, String> send(KlayDto klayDto) throws ParseException {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("x-chain-id", chain_id);
        headers.set("Authorization", Auth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject request = new JSONObject();
        boolean submit = TRUE;
        String value = changeNumber(klayDto.getValue()*Math.pow(10,18));
        request.put("from", klayDto.getFrom());
        request.put("value", value);
        request.put("to",klayDto.getTo());
        request.put("memo", "0x123");
        request.put("nonce",0);
        request.put("submit", submit);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplates.postForEntity(URL,entity,String.class);
        String jText = response.getBody().toString();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(jText);
        String status = obj.get("status").toString();
        HashMap<String, String> result = new HashMap<>();
        if(status.equals("Submitted")){
            result.put("status","OK");
        }
        else{
            result.put("Status","false");
        }
        return result;

    }
    public static String changeNumber(double num) {
        String answer = "";
        int remainder = 0;
        while(num != 0) {
            remainder = (int) (num%16);
            if(num%16 < 10) {
                answer = remainder + answer;
            } else {
                answer = (char)(remainder + 55) + answer;
            }
            num /= 16;
        }
        String resultdata = "";
        for (int i = 0; i<answer.length()-1;i++){
            if(answer.charAt(i) == '0'){
                continue;
            }
            else {
                System.out.println(i);
                resultdata = answer.substring(i,answer.length());
                break;
            }
        }
        System.out.println("return data =" + resultdata);
        return "0x"+resultdata;
    }
}
