package net.api.cho.stockdata.stock.Wallet.Service;

import net.api.cho.stockdata.stock.Wallet.Dto.KlayDto;
import net.api.cho.stockdata.stock.Wallet.Dto.UserDto;
import net.api.cho.stockdata.stock.Wallet.Dto.WalletDto;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Component
public interface WalletService {

    Object CheckWallet() throws IOException;
    HashMap<String, String> CreateWallet(String userid) throws IOException;
    HashMap<String, Optional<Double>> muchWallet(String account) throws IOException, ParseException;
    HashMap<String, Boolean> send(KlayDto klayDto) throws ParseException;
    HashMap<String, Boolean> findwallet(String email);

}
