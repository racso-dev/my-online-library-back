package com.marketpay.services.user;

import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.entity.Operation;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.repository.BusinessUnitRepository;
import com.marketpay.persistence.repository.OperationRepository;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.persistence.repository.UserRepository;
import com.marketpay.services.user.resource.ShopUserResource;
import com.marketpay.services.user.resource.UserInformationResource;
import com.marketpay.services.user.resource.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserService {

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Service de récupération d'un userInformation à partir d'un user
     * @param user
     * @return
     */
    public UserInformationResource getUserInformation(User user) throws EntityNotFoundException {
        UserInformationResource resource = new UserInformationResource();

        // On set le user profile
        resource.setProfile(user.getProfile());

        if(user.getIdBu() != null){
            //C'est un super user on récupère sa BU
            //On récupère la bu associée au user
            BusinessUnit businessUnit = businessUnitRepository.findOne(user.getIdBu()).orElseThrow(() ->
                new EntityNotFoundException(user.getIdBu(), "business_unit")
            );
            resource.setBusinessUnit(businessUnit);

            //On récupère les shop associés
            resource.setShopList(shopRepository.findByIdBu(businessUnit.getId()));

        } else if(user.getIdShop() != null) {
            //C'est un user simple
            //On récupère le shop associé au user
            Shop shop = shopRepository.findOne(user.getIdShop()).orElseThrow(() ->
                new EntityNotFoundException(user.getIdShop(), "shop")
            );
            List<Shop> shopList = new ArrayList<>();
            shopList.add(shop);
            resource.setShopList(shopList);

            //On récupère la BU associé au shop
            BusinessUnit businessUnit = businessUnitRepository.findOne(shop.getIdBu()).orElseThrow(() ->
                new EntityNotFoundException(shop.getIdBu(), "business_unit")
            );
            resource.setBusinessUnit(businessUnit);

        } else {
            //C'est un admin user donc pas de BU et de shop
            return resource;
        }

        //On récupère la dernière fundingDate des operations associé au user
        resource.setLastFundingDate(getLastFundingDateForShopList(resource.getShopList()));

        return resource;
    }

    /**
     * Service de récupération de la dernière fundingDate des operations des shop donnés
     * @param shopList
     * @return
     */
    private LocalDate getLastFundingDateForShopList(List<Shop> shopList) {
        //On construit une liste d'idShop
        List<Long> idShopList = shopList.stream().map(shop -> shop.getId()).collect(Collectors.toList());

        //On récupère l'operation avec la dernière fundindDate pour cette liste de shop
        Optional<Operation> operationOpt = operationRepository.findFirstByIdShopInOrderByFundingDateDesc(idShopList);

        if(operationOpt.isPresent()){
            return operationOpt.get().getFundingDate();
        }

        return LocalDate.now();
    }

    /**
     * Service de récupération des shop user pour une BU
     * @param idBu
     * @return une liste de shop avec les utilisateurs associé
     */
    public List<ShopUserResource> getShopUserList(long idBu) {
        // On initialise la liste qui sera retourné
        List<ShopUserResource> shopUserResourceList = new ArrayList<>();

        // On récupère tous les shops pour la BU donnée
        List<Shop> shopList = shopRepository.findByIdBu(idBu);

        for (Shop shop : shopList) {
            // On récupềre tous les utilisateurs du shop donné
            List<User> userList = userRepository.findByIdShop(shop.getId());

            // Puis on les convertit en utilisateur que l'on peut retourner (sans les mots de passe)
            List<UserResource> userResourceList = generateUserResourceListFromUserList(userList);

            // On créer et on remplit notre shopUserResource
            ShopUserResource shopUserResource = new ShopUserResource();
            shopUserResource.setIdShop(shop.getId());
            shopUserResource.setIdBu(shop.getIdBu());
            shopUserResource.setName(shop.getName());
            shopUserResource.setCodeAl(shop.getCodeAl());
            shopUserResource.setUserList(userResourceList);

            shopUserResourceList.add(shopUserResource);
        }

        return shopUserResourceList;
    }

    /**
     * Génère une liste de UserResource à partir d'une liste de User
     * @param userList
     * @return la liste géneré de UserResource
     */
    private List<UserResource> generateUserResourceListFromUserList(List<User> userList) {
        List<UserResource> userResourceList = new ArrayList<>();

        for (User user : userList) {
            UserResource userResource = new UserResource(user);
            userResourceList.add(userResource);
        }

        return userResourceList;
    }

}
