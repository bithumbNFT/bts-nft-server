package net.api.cho.stockdata.stock.NFT.Controller;

import lombok.RequiredArgsConstructor;
import net.api.cho.stockdata.stock.AWSS3.S3Service.S3uploader;
import net.api.cho.stockdata.stock.NFT.Domain.*;
import net.api.cho.stockdata.stock.NFT.Repository.NFTRepository;
import net.api.cho.stockdata.stock.NFT.Service.NFTService;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/NFT")
public class NFTController {
    private final NFTService nftService;
    private final NFTRepository nftRepository;
    private final S3uploader s3uploader;

    @PostMapping("/makeNFT")
    public ResponseEntity<HashMap<String,String>> makeNFT(@RequestPart(value = "NFTInfo") MakeNFTdto makeNFTdto,
                                  @RequestPart(value = "image") MultipartFile file) throws IOException, ParseException {
        return ResponseEntity.ok(nftService.makeNFT(makeNFTdto,file));
    }
    @GetMapping("/checkNftbyid/{id}")
    public ResponseEntity<List<HashMap<String,String>>> checkNFT(@PathVariable String id) throws IOException, ParseException{
        return ResponseEntity.ok(nftService.checkNFT(id));
    }
    @GetMapping("/checkNftbyNftid/{NFTid}")
    public ResponseEntity<HashMap<String,String>> checkbyNFTid(@PathVariable String NFTid){
        return ResponseEntity.ok(nftService.findByid(NFTid));
    }
    @PostMapping("/sendNFT")
    public ResponseEntity<HashMap<String,String>> sendNFT(@RequestBody NFTdto NFTdto) throws ParseException{
        return ResponseEntity.ok(nftService.sendNFT(NFTdto));
    }
    @GetMapping("/allNFT")
    public ResponseEntity<List<HashMap<String,String>>> allNFT() throws IOException,ParseException{
        return ResponseEntity.ok(nftService.allNFT());
    }
    @DeleteMapping("/deleteNFT")
    public ResponseEntity<HashMap<String,String>> deleteNFT(@RequestBody Deletedto deletedto) throws ParseException{
        return ResponseEntity.ok(nftService.deleteNFT(deletedto));
    }
    @PostMapping("/likeNFT")
    public ResponseEntity<HashMap<String,String>> likeNFT(@RequestBody Likedto likedto) throws ParseException{
        return ResponseEntity.ok(nftService.likeNFT(likedto));
    }
    @DeleteMapping("/likeNFT")
    public ResponseEntity<HashMap<String,String>> deletelike(@RequestBody Likedto likedto){
        return ResponseEntity.ok(nftService.deletelike(likedto));
    }
    @GetMapping("/userlikelist/{user}")
    public ResponseEntity<List<HashMap<String,String>>> likelist(@PathVariable String user) throws IOException, ParseException{
        return ResponseEntity.ok(nftService.likelist(user));
    }
    @GetMapping("/countlike/{Nft}")
    public ResponseEntity<HashMap<String,Integer>> likecount(@PathVariable String Nft){
        return ResponseEntity.ok(nftService.countlike(Nft));
    }
}
