package net.api.cho.stockdata.stock.NFT.Api;

import lombok.RequiredArgsConstructor;
import net.api.cho.stockdata.stock.Feign.FeignController;
import net.api.cho.stockdata.stock.NFT.Domain.MakeNFT;
import net.api.cho.stockdata.stock.NFT.Domain.NFTdto;
import net.api.cho.stockdata.stock.NFT.Repository.NFTRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NFTapi {
    private final NFTRepository nftRepository;
    private final RestTemplate restTemplates;
    private final FeignController feignController;
    private final String chain_id = "1001";
    private final String Auth = "Basic S0FTSzA0QTRYNEI1WVJBTE1EUDBUTzUzOnRTQ1VacVhoLUt4a0dnTklwdHZVQWV5aGtodnFLMmFHeVVKMHAtblg=";
    private final String address_alias = "kas-tutorial";
    private final String URL = "https://kip17-api.klaytnapi.com/v1/contract/"+address_alias+"/token";
    private final String CheckURL = "https://kip17-api.klaytnapi.com/v1/contract/"+address_alias+"/owner/";


    public HashMap<String, String> makeNFT(MakeNFT makeNFT) throws ParseException {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("x-chain-id", chain_id);
        headers.set("Authorization", Auth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String NFTid = changeNumber(makeNFT.getNO()*Math.pow(10,6));
        makeNFT.setId(NFTid);
        HashMap<String,String> findowner = feignController.getaddressByUserId(makeNFT.getOwner());
        JSONObject request = new JSONObject();
        request.put("to",findowner.get("address"));
        request.put("id",NFTid);
        request.put("uri","127.0.0.1:8080/NFT/checkNFTbyid/"+makeNFT.getId());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplates.postForEntity(URL,entity,String.class);
        String jText = response.getBody().toString();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(jText);
        HashMap<String,String> result = new HashMap<>();
        result.put("status",obj.get("status").toString());
        result.put("id",NFTid);
        result.put("image",makeNFT.getImage());
        result.put("imagepath",makeNFT.getImagepath());
        result.put("name",makeNFT.getName());
        result.put("description",makeNFT.getDescription());
        result.put("auction",makeNFT.getAuction().toString());
        result.put("price",makeNFT.getPrice());
        if(obj.get("status").equals("Submitted")){
            Optional<MakeNFT> update = nftRepository.findByNO(makeNFT.getNO());
            update.ifPresent(selectUser -> {
                selectUser.setNO(makeNFT.getNO());
                selectUser.setId(NFTid);
                selectUser.setDescription(makeNFT.getDescription());
                selectUser.setName(makeNFT.getName());
                selectUser.setImage(makeNFT.getImage());
                selectUser.setOwner(makeNFT.getOwner());
                selectUser.setImagepath(makeNFT.getImagepath());
                selectUser.setAuction(makeNFT.getAuction());
                selectUser.setPrice(makeNFT.getPrice());
                selectUser.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                feignController.saveNFT(selectUser);
            });
        }
        return result;
    }
    //    public Object checkNFT(String account) throws ParseException{
//        final HttpHeaders headers = new HttpHeaders();
//        headers.set("x-chain-id", chain_id);
//        headers.set("Authorization", Auth);
//        final HttpEntity<String> entity = new HttpEntity<>(headers);
//        String checkURL = CheckURL+account;
//        JSONObject response = restTemplates.exchange(checkURL, HttpMethod.GET,entity, JSONObject.class).getBody();
//        String jText = response.toString();
//        System.out.println(jText);
//        return response.toString();
//    }
    public String deleteNFT(String from, String nft) throws ParseException{
        String SendURL = "https://kip17-api.klaytnapi.com/v1/contract/"+address_alias+"/token/"+nft;
        final HttpHeaders headers = new HttpHeaders();
        headers.set("x-chain-id", chain_id);
        headers.set("Authorization", Auth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject request = new JSONObject();
        request.put("from",from);
        System.out.println(request);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = ResponseEntity.ok(restTemplates.exchange(SendURL,HttpMethod.DELETE,entity,String.class).getBody());
        String[] status = response.getStatusCode().toString().split(" ");
        HashMap<String,String> result = new HashMap<>();
        System.out.println(status[1]);
        return status[1];

    }
    public String sendNFT(NFTdto NFTdto) throws ParseException{
        String SendURL = "https://kip17-api.klaytnapi.com/v1/contract/"+address_alias+"/token/";
        final HttpHeaders headers = new HttpHeaders();
        headers.set("x-chain-id", chain_id);
        headers.set("Authorization", Auth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject request = new JSONObject();
        request.put("sender", NFTdto.getFrom());
        request.put("owner", NFTdto.getFrom());
        request.put("to", NFTdto.getTo());
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        System.out.println(request.toString());
        SendURL = SendURL + NFTdto.getId();
        System.out.println("SendURL = " + SendURL);
        ResponseEntity<String> response = restTemplates.postForEntity(SendURL,entity,String.class);
        String jText = response.getBody().toString();
        System.out.println(jText);

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(jText);
        String status = obj.get("status").toString();
        return status;
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
                resultdata = answer.substring(i,answer.length());
                break;
            }
        }
        System.out.println("return data =" + resultdata);
        return "0x"+resultdata;
    }
}
