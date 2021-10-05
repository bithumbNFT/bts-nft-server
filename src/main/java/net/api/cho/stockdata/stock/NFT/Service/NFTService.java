package net.api.cho.stockdata.stock.NFT.Service;

import net.api.cho.stockdata.stock.NFT.Domain.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
public interface NFTService {
    HashMap<String,String> makeNFT(MakeNFTdto makeNFT, MultipartFile file) throws IOException,ParseException;
    List<HashMap<String,String>> checkNFT(String id) throws IOException,ParseException;
    HashMap<String,String> sendNFT(NFTdto NFTdto) throws ParseException;
    HashMap<String,String> findByid(String id);
    List<HashMap<String,String>> allNFT();
    HashMap<String,String> likeNFT(Likedto likedto);
    List<HashMap<String,String>> likelist(String user) throws IOException;
    HashMap<String,String> deletelike(Likedto likedto);
    HashMap<String,Integer> countlike(String nft);
    HashMap<String,String> deleteNFT(Deletedto deletedto) throws ParseException;
    List<HashMap<String,String>> findNFT(String keyword);
}