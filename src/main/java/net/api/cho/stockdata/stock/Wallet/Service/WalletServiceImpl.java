package net.api.cho.stockdata.stock.Wallet.Service;

import lombok.RequiredArgsConstructor;
import net.api.cho.stockdata.stock.Feign.FeignController;
import net.api.cho.stockdata.stock.Wallet.Api.SendKlay;
import net.api.cho.stockdata.stock.Wallet.Api.WalletApi;
import net.api.cho.stockdata.stock.Wallet.Domain.Wallet;
import net.api.cho.stockdata.stock.Wallet.Dto.KlayDto;
import net.api.cho.stockdata.stock.Wallet.Dto.UserDto;
import net.api.cho.stockdata.stock.Wallet.Dto.WalletDto;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService{
    private final WalletApi wallet;
    private final SendKlay sendKlay;
    private final FeignController feignController;

    @Override
    public Object CheckWallet() throws IOException {
        return wallet.CheckWallet();
    }

    @Override
    public HashMap<String, String> CreateWallet(String userid) throws IOException {
        WalletDto walletinfo = wallet.CreateWallet();
        Wallet wallet = new Wallet();
        wallet.setAddress(walletinfo.getAddress());
        wallet.setName(userid);
        HashMap<String,String> result = new HashMap<>();
        result.put("address",walletinfo.getAddress());
        result.put("id",userid);
        feignController.sendWalletaddress(result);
        return result;
    }

    @Override
    public HashMap<String, Optional<Double>> muchWallet(String account) throws IOException, ParseException {
        Optional<Double> klay = wallet.muchWallet(account);
        HashMap<String,Optional<Double>> hashMap = new HashMap<>();
        hashMap.put("klay", klay);
        return hashMap;
    }

    @Override
    public HashMap<String, String> send(KlayDto klayDto) throws ParseException{
        String to = feignController.getaddressByUserId(klayDto.getTo()).get("address");
        String from = feignController.getaddressByUserId(klayDto.getFrom()).get("address");
        KlayDto exchange = KlayDto.builder().to(to).from(from).value(klayDto.getValue()).build();
        return sendKlay.send(exchange);
    }

    @Override
    public HashMap<String, Boolean> findwallet(String userid) {
        HashMap<String,String> check = feignController.getaddressByUserId(userid);
        boolean enter;
        if(check.get("address") == "")
        {
            enter = false;
        }
        else
            enter = true;
        HashMap<String,Boolean> result = new HashMap<>();
        result.put("status",enter);
        return result;
    }
}
