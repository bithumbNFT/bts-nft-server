package net.api.cho.stockdata.stock.NFT.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.api.cho.stockdata.stock.Feign.FeignController;
import net.api.cho.stockdata.stock.NFT.Domain.*;
import net.api.cho.stockdata.stock.NFT.Repository.NFTRepository;
import net.api.cho.stockdata.stock.Wallet.Api.SendKlay;
import net.api.cho.stockdata.stock.Wallet.Api.WalletApi;
import net.api.cho.stockdata.stock.NFT.Api.NFTapi;
import net.api.cho.stockdata.stock.Price.Priceapi;
import net.api.cho.stockdata.stock.AWSS3.S3Service.S3uploader;
import net.api.cho.stockdata.stock.Wallet.Dto.KlayDto;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
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
    private final SendKlay sendKlay;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public HashMap<String,String> makeNFT(MakeNFTdto makeNFTdto, MultipartFile file) throws IOException, ParseException{
        String imagepath = s3uploader.upload(file,"static");
        MakeNFT insertNFT = MakeNFT.builder().description(makeNFTdto.getDescription()).image(makeNFTdto.getImage())
                .owner(makeNFTdto.getOwner()).name(makeNFTdto.getName()).imagepath(imagepath)
                .auction(makeNFTdto.getAuction()).price(makeNFTdto.getPrice()).term(makeNFTdto.getTerm()).build();
        nftRepository.save(insertNFT);
        HashMap<String,String> result = new HashMap<>();
        HashMap<String, String> set = NFTapi.makeNFT(insertNFT);
        if(set.get("status").equals("Submitted")){
            return set;
        }
        else {
            result.put("status", "Fail");
            return result;
        }
    }
    @Override
    public List<HashMap<String,String>> checkNFT(String id) throws IOException{
        List<HashMap<String,Object>> obj = feignController.findNft(id);
        List<HashMap<String,String>> output = new ArrayList<>();
        for(int i=0; i<obj.size();i++)
        {
            HashMap<String,String> result = new HashMap<>();
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
        result.put("auction",nft.get(0).get("auction").toString());
        result.put("price",nft.get(0).get("price").toString());
        result.put("term",nft.get(0).get("term").toString());
        return result;
    }
    @Override
    public List<HashMap<String,String>> allNFT(){
        Optional<List<HashMap<String,Object>>> receive = Optional.ofNullable(feignController.findNftAll());
        List<HashMap<String,Object>> nfts = receive.get();
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
        List<HashMap<String,String>> output = new ArrayList<>();
        for(int i=0; i<likelist.size();i++)
        {
            HashMap<String,String> result = new HashMap<>();
            Object userinfo = likelist.get(i).get("userId");
            Map userId = mapper.convertValue(userinfo, Map.class);
            Object nftinfo = likelist.get(i).get("no");
            Map nft = mapper.convertValue(nftinfo, Map.class);
            Object nftownerinfo = nft.get("userId");
            Map ownerinfo = mapper.convertValue(nftownerinfo,Map.class);
            result.put("userId",userId.get("userId").toString());
            result.put("id",nft.get("id").toString());
            result.put("name",nft.get("name").toString());
            result.put("description",nft.get("description").toString());
            result.put("image",nft.get("image").toString());
            result.put("imagepath",nft.get("imagepath").toString());
            result.put("email",ownerinfo.get("email").toString());
            result.put("username",ownerinfo.get("name").toString());
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

    @Override
    public List<HashMap<String, String>> findNFT(String keyword) {
        Optional<List<HashMap<String,Object>>> receive = Optional.ofNullable(feignController.findNftAll());
        List<HashMap<String,Object>> nfts = receive.get();
        List<HashMap<String,String>> output = new ArrayList<>();
        for(int i=0; i<nfts.size();i++)
        {
            HashMap<String,String> result = new HashMap<>();
            Object in = nfts.get(i).get("userId");
            Map userId = mapper.convertValue(in,Map.class);
            if(nfts.get(i).get("name").toString().indexOf(keyword)!=-1)
            {
                result.put("id",nfts.get(i).get("id").toString());
                result.put("name",nfts.get(i).get("name").toString());
                result.put("description",nfts.get(i).get("description").toString());
                result.put("image",nfts.get(i).get("image").toString());
                result.put("imagepath",nfts.get(i).get("imagepath").toString());
                result.put("email",userId.get("email").toString());
                result.put("username",userId.get("name").toString());
            }
            else
            {
                continue;
            }
            output.add(result);
            System.out.println(result);
        }
        return output;
    }

    @Override
    public HashMap<String, String> auction(String id) {
        StartDto startDto = new StartDto(id,Auction.START);
        List<HashMap<String,Object>> receive = feignController.auctionstart(startDto);
        Object in = receive.get(0).get("userId");
        Map userId = mapper.convertValue(in,Map.class);
        HashMap<String,String> result = new HashMap<>();
        result.put("id",receive.get(0).get("id").toString());
        result.put("name",receive.get(0).get("name").toString());
        result.put("description",receive.get(0).get("description").toString());
        result.put("image",receive.get(0).get("image").toString());
        result.put("imagepath",receive.get(0).get("imagepath").toString());
        result.put("email",userId.get("email").toString());
        result.put("username",userId.get("name").toString());
        result.put("auction",receive.get(0).get("auction").toString());
        result.put("price",receive.get(0).get("price").toString());
        result.put("term",receive.get(0).get("term").toString());
        return result;
    }
    @Override
    public HashMap<String, String> exchange(FinishDto finishDto) throws ParseException {
        NFTdto nft = new NFTdto();
        HashMap<String,String> owneraddr = feignController.getaddressByUserEmail(finishDto.getOwner());
        HashMap<String,String> buyuseraddr = feignController.getaddressByUserEmail(finishDto.getUser());
        nft.setId(finishDto.getId());
        nft.setFrom(owneraddr.get("address"));
        nft.setTo(buyuseraddr.get("address"));
        String set = NFTapi.sendNFT(nft);
        HashMap<String,String> result = new HashMap<>();
        NFTdto moveinfo = new NFTdto();
        moveinfo.setId(finishDto.getId());
        moveinfo.setFrom(owneraddr.get("id"));
        moveinfo.setTo(buyuseraddr.get("id"));
        if(set.equals("Submitted")){
            result = feignController.moveNft(moveinfo);
            if(result.get("status").equals("OK")) {
                KlayDto exchange = KlayDto.builder().to(owneraddr.get("address"))
                        .from(buyuseraddr.get("address")).value(finishDto.getValue()).build();
                result = sendKlay.send(exchange);
                if(result.get("status").equals("OK")) {
                    result = feignController.auctionfinish(finishDto);
                }
            }
        }
        else
            result.put("status","Fail");

        return result;

    }
}
