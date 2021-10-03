package net.api.cho.stockdata.stock.NFT.Repository;

import net.api.cho.stockdata.stock.NFT.Domain.MakeNFT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NFTRepository extends JpaRepository<MakeNFT,Long> {
    MakeNFT findById(String id);
    Optional<MakeNFT> findById(Integer id);
    Optional<MakeNFT> findByNO(Integer no);
    List<MakeNFT> findTop1ByOrderByIdAsc();
    List<MakeNFT> findByowner(String owner);
    void deleteByNO(Integer no);
    Optional<MakeNFT> findByid(String id);
    @Transactional
    void deleteByid(String id);

}
