package com.marketpay.services.user;

import com.marketpay.api.user.response.ShopUserListResponse;
import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.exception.MarketPayException;
import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.entity.Operation;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.repository.BusinessUnitRepository;
import com.marketpay.persistence.repository.OperationRepository;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.persistence.repository.UserRepository;
import com.marketpay.references.USER_PROFILE;
import com.marketpay.services.user.resource.ShopUserListResource;
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

        // On set le user
        resource.setLastName(user.getLastName());
        resource.setFirstName(user.getFirstName());
        resource.setProfile(user.getProfile());

        if(user.getIdBu() != null){
            //C'est un super user on récupère sa BU
            //On récupère la bu associée au user
            BusinessUnit businessUnit = businessUnitRepository.findOne(user.getIdBu()).orElseThrow(() ->
                new EntityNotFoundException(user.getIdBu(), "business_unit")
            );
            resource.setBusinessUnit(businessUnit);

        } else if(user.getIdShop() != null) {
            //C'est un user simple ou un manager
            //On récupère le shop associé au user
            Shop shop = shopRepository.findOne(user.getIdShop()).orElseThrow(() ->
                new EntityNotFoundException(user.getIdShop(), "shop")
            );
            resource.setShop(shop);

            //On récupère la BU associé au shop
            BusinessUnit businessUnit = businessUnitRepository.findOne(shop.getIdBu()).orElseThrow(() ->
                new EntityNotFoundException(shop.getIdBu(), "business_unit")
            );
            resource.setBusinessUnit(businessUnit);

        } else {
            //C'est un admin user donc pas de BU et de shop
            return resource;
        }

        return resource;
    }




    /**
     * Service de récupération des shop user pour une BU
     * @param idBu
     * @return une liste de shop avec les utilisateurs associé
     */
    public ShopUserListResource getShopUserList(Long idBu, Long idShop) throws MarketPayException {
        // On initialise les listes qui seront retournées
        ShopUserListResource resource = new ShopUserListResource();
        resource.setShopUserList(new ArrayList<>());

        List<Integer> profileList = new ArrayList<>();
        profileList.add(USER_PROFILE.USER.getCode());
        profileList.add(USER_PROFILE.USER_MANAGER.getCode());

        if (idBu != null) {
            // On récupère tous les shops pour la BU donnée
            shopRepository.findByIdBu(idBu).forEach(shop -> {
                // On récupère tous les utilisateurs du shop donné qui ont le profile user ou userManager
                List<User> userList = userRepository.findByIdShopAndProfileIn(shop.getId(), profileList);

                // On créer et on remplit notre shopUserResource
                resource.getShopUserList().add(new ShopUserResource(shop, generateUserResourceListFromUserList(userList)));
            });

            //On récupère les superUser de la BU
            List<User> superUserList = userRepository.findByIdBuAndProfile(idBu, USER_PROFILE.SUPER_USER.getCode());
            resource.setSuperUserList(generateUserResourceListFromUserList(superUserList));

        } else if (idShop != null) {
            Shop shop = shopRepository.findOne(idShop).orElseThrow(() ->
                new EntityNotFoundException(idShop, "shop")
            );
            // On récupère tous les utilisateurs du shop donné
            List<User> userList = userRepository.findByIdShopAndProfileIn(idShop, profileList);

            // On créer et on remplit notre shopUserResource
            resource.getShopUserList().add(new ShopUserResource(shop, generateUserResourceListFromUserList(userList)));
        }

        return resource;
    }

    /**
     * Génère une liste de UserResource à partir d'une liste de User
     * @param userList
     * @return la liste géneré de UserResource
     */
    private List<UserResource> generateUserResourceListFromUserList(List<User> userList) {
        return userList.stream()
            .map(UserResource::new)
            .collect(Collectors.toList());
    }

}
