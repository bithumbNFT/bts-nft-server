package net.api.cho.stockdata.stock.Wallet.Repository;

import net.api.cho.stockdata.stock.Wallet.Domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Wallet findByemail(String email);
    Wallet findByaddress(String address);
    boolean existsByemail(String email);
}
