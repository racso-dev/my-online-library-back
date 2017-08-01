package com.marketpay.persistence.repository;

import com.marketpay.persistence.entity.Shop;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 * Created by etienne on 28/07/17.
 */
@Repository
@Transactional(readOnly = true)
public class ShopRepositoryImpl implements ShopRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Shop> findByContractNumber(String contractNumber) {

        Query query = entityManager.createNativeQuery("SELECT s.* FROM shop s LEFT JOIN shop_contract_number scn on scn.id_shop = s.id WHERE scn.contract_number = ?", Shop.class);
        query.setParameter(1, contractNumber);
        List<Shop> resultList = query.getResultList();

        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

}
