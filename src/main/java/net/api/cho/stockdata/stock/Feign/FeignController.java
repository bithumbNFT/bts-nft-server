package net.api.cho.stockdata.stock.Feign;

import net.api.cho.stockdata.stock.NFT.Domain.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "team", url = "http://3.35.78.48:8080")
public interface FeignController {
    @GetMapping("team/{userid}/")
    HashMap<String,String> getaddressByUserId(@PathVariable("userid") String userid);
    @GetMapping("team/email/{useremail}/")
    HashMap<String,String> getaddressByUserEmail(@PathVariable("useremail") String email);
    @PostMapping(value="team/wallet", produces = "application/json")
    Object sendWalletaddress(@RequestBody Object wallet);
    @PostMapping(value="team/NFT", produces = "application/json")
    Object saveNFT(@RequestBody Object NFT);
    @GetMapping("team/nft/{userid}/")
    List findNft(@PathVariable("userid") String userid);
    @GetMapping("team/nftinfo/{NFTid}/")
    List findNftByNftId(@PathVariable("NFTid") String NFTid);
    @GetMapping("team/allnft")
    List findNftAll();
    @PostMapping("team/likenft")
    HashMap<String,String> likenft(@RequestBody Likedto likedto);
    @GetMapping("team/userlikelist/{user}")
    List likelist(@PathVariable String user);
    @DeleteMapping("team/likenft")
    HashMap<String,String> deletelikenft(@RequestBody Likedto likedto);
    @GetMapping("team/countlike/{Nftid}/")
    HashMap<String,Integer> countlike(@PathVariable("Nftid") String Nftid);
    @PostMapping("team/moveNft")
    HashMap<String,String> moveNft(@RequestBody NFTdto nfTdto);
    @DeleteMapping("team/delete")
    HashMap<String,String> deleteNft(@RequestBody Deletedto deletedto);
    @PostMapping("team/start")
    List auctionstart(@RequestBody StartDto startDto);
    @PostMapping("team/finish")
    HashMap<String,String> auctionfinish(@RequestBody FinishDto FinishDto);
}
