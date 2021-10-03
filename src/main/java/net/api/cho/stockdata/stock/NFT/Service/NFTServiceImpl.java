package net.api.cho.stockdata.stock.NFT.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.api.cho.stockdata.stock.Feign.FeignController;
import net.api.cho.stockdata.stock.NFT.Domain.*;
import net.api.cho.stockdata.stock.NFT.Repository.NFTRepository;
import net.api.cho.stockdata.stock.Wallet.Api.WalletApi;
import net.api.cho.stockdata.stock.NFT.Api.NFTapi;
import net.api.cho.stockdata.stock.Price.Priceapi;
import net.api.cho.stockdata.stock.AWSS3.S3Service.S3uploader;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class NFTServiceImpl implements NFTService {
    private final Priceapi priceapi;
    private final WalletApi wallet;
    private final NFTRepository nftRepository;
    private final NFTapi NFTapi;
    private final S3uploader s3uploader;
    private final FeignController feignController;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public HashMap<String,String> makeNFT(MakeNFTdto makeNFTdto, MultipartFile file) throws IOException, ParseException{
        String imagepath = s3uploader.upload(file,"static");
        MakeNFT insertNFT = MakeNFT.builder().description(makeNFTdto.getDescription()).image(makeNFTdto.getImage())
                        .owner(makeNFTdto.getOwner()).name(makeNFTdto.getName()).imagepath(imagepath).build();
        nftRepository.save(insertNFT);
        HashMap<String,String> result = new HashMap<>();
        String set = NFTapi.makeNFT(insertNFT);
        if(set.equals("Submitted")){
            result.put("status","OK");
        }
        else
            result.put("status","Fail");
        return result;
    }
    @Override
    public List<HashMap<String,String>> checkNFT(String id) throws IOException{
        List<HashMap<String,Object>> obj = feignController.findNft(id);
        HashMap<String,String> result = new HashMap<>();
        List<HashMap<String,String>> output = new ArrayList<>();
        for(int i=0; i<obj.size();i++)
        {
            Object in = obj.get(i).get("userId");
            Map userId = mapper.convertValue(in,Map.class);
            result.put("id",obj.get(i).get("id").toString());
            result.put("name",obj.get(i).get("name").toString());
            result.put("description",obj.get(i).get("description").toString());
            result.put("image",obj.get(i).get("image").toString());
            result.put("imagepath",obj.get(i).get("imagepath").toString());
            result.put("email",userId.get("email").toString());
            result.put("username",userId.get("name").toString());
            output.add(result);
        }
        return output;
    }
    @Override
    public HashMap<String,String> sendNFT(NFTdto NFTdto) throws ParseException{
        NFTdto nft = new NFTdto();
        HashMap<String,String> fromAddress = feignController.getaddressByUserId(NFTdto.getFrom());
        HashMap<String,String> toAddress = feignController.getaddressByUserId(NFTdto.getTo());
        nft.setId(NFTdto.getId());
        nft.setFrom(fromAddress.get("address"));
        nft.setTo(toAddress.get("address"));
        String set = NFTapi.sendNFT(nft);
        HashMap<String,String> result = new HashMap<>();
        if(set.equals("Submitted")){
            result = feignController.moveNft(NFTdto);
        }
        else
            result.put("status","Fail");
        return result;
    }

    @Override
    public HashMap<String,String> findByid(String id) {
        List<HashMap<String,Object>> nft = feignController.findNftByNftId(id);
        HashMap<String,String> result = new HashMap<>();
        Object in = nft.get(0).get("userId");
        Map userId = mapper.convertValue(in,Map.class);
        result.put("id",nft.get(0).get("id").toString());
        result.put("name",nft.get(0).get("name").toString());
        result.put("description",nft.get(0).get("description").toString());
        result.put("image",nft.get(0).get("image").toString());
        result.put("imagepath",nft.get(0).get("imagepath").toString());
        result.put("email",userId.get("email").toString());
        result.put("username",userId.get("name").toString());
        return result;
    }
    @Override
    public List<HashMap<String,String>> allNFT() throws IOException{
        List<HashMap<String,Object>> nfts = feignController.findNftAll();
        List<HashMap<String,String>> output = new ArrayList<>();
        for(int i=0; i<nfts.size();i++)
        {
            HashMap<String,String> result = new HashMap<>();
            Object in = nfts.get(i).get("userId");
            Map userId = mapper.convertValue(in,Map.class);
            result.put("id",nfts.get(i).get("id").toString());
            result.put("name",nfts.get(i).get("name").toString());
            result.put("description",nfts.get(i).get("description").toString());
            result.put("image",nfts.get(i).get("image").toString());
            result.put("imagepath",nfts.get(i).get("imagepath").toString());
            result.put("email",userId.get("email").toString());
            result.put("username",userId.get("name").toString());
            output.add(result);
            System.out.println(result);
        }
        return output;

    }
    @Override
    public HashMap<String,String> likeNFT(Likedto likedto){
        HashMap<String,String> result = feignController.likenft(likedto);
        return result;
    }
    @Override
    public List<HashMap<String,String>> likelist(String user) throws IOException{
        List<HashMap<String,Object>> likelist = feignController.likelist(user);
        HashMap<String,String> result = new HashMap<>();
        List<HashMap<String,String>> output = new ArrayList<>();
        for(int i=0; i<likelist.size();i++)
        {
            Object userinfo = likelist.get(i).get("userId");
            Map userId = mapper.convertValue(userinfo, Map.class);
            Object nftinfo = likelist.get(i).get("no");
            Map nft = mapper.convertValue(nftinfo, Map.class);
            Object nftownerinfo = nft.get("userId");
            Map ownerinfo = mapper.convertValue(nftownerinfo,Map.class);
            result.put("userId",userId.get("userId").toString());
            result.put("nft_id",nft.get("id").toString());
            result.put("image_path",nft.get("imagepath").toString());
            result.put("ownerId",ownerinfo.get("name").toString());
            output.add(result);
        }
        return output;
    }

    @Override
    public HashMap<String,String> deletelike(Likedto likedto) {
        HashMap<String,String> result = feignController.deletelikenft(likedto);
        return result;
    }

    @Override
    public HashMap<String,Integer> countlike(String nft) {
        HashMap<String,Integer> result = new HashMap<>();
        result = feignController.countlike(nft);
        return result;
    }

    @Override
    public HashMap<String,String> deleteNFT(Deletedto deletedto) throws ParseException{
        String nft = deletedto.getId();
        HashMap<String,String> address = feignController.getaddressByUserId(deletedto.getFrom());
        String from = address.get("address");
        HashMap<String,String> result = new HashMap<>();
        String status = NFTapi.deleteNFT(from,nft);
        if(status.toString().equals("OK"))
        {
            feignController.deleteNft(deletedto);
            result.put("status","OK");
        }
        else {
            result.put("status","Fail");
            return result;
        }
        return result;
    }

}
