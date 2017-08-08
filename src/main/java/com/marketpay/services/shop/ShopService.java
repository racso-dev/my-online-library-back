package com.marketpay.services.shop;

import com.marketpay.persistence.entity.Shop;
import com.marketpay.persistence.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by etienne on 07/08/17.
 */
@Component
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    /**
     * Service de récupération des shop d'une Bu
     * @param idBu
     * @return
     */
    public List<Shop> getShopListByIdBu(long idBu) {
        return shopRepository.findByIdBu(idBu);
    }
}
