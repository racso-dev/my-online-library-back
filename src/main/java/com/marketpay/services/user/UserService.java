package com.marketpay.services.user;

import com.marketpay.api.user.request.EditMyUserRequest;
import com.marketpay.api.user.request.EditUserRequest;
import com.marketpay.exception.EntityNotFoundException;
import com.marketpay.exception.MarketPayException;
import com.marketpay.persistence.entity.BusinessUnit;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.persistence.entity.User;
import com.marketpay.persistence.repository.BusinessUnitRepository;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.persistence.repository.UserKeyPassRepository;
import com.marketpay.persistence.repository.UserRepository;
import com.marketpay.references.USER_PROFILE;
import com.marketpay.services.keypass.KeyPassService;
import com.marketpay.services.user.resource.ShopUserListResource;
import com.marketpay.services.user.resource.ShopUserResource;
import com.marketpay.services.user.resource.UserInformationResource;
import com.marketpay.services.user.resource.UserResource;
import com.marketpay.utils.MailUtils;
import com.marketpay.utils.PasswordUtils;
import com.marketpay.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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
    private UserRepository userRepository;

    @Autowired
    private KeyPassService keyPassService;

    @Autowired
    private UserKeyPassRepository userKeyPassRepository;

    /**
     * Service de récupération d'un userInformation à partir d'un user
     * @param user
     * @return
     */
    public UserInformationResource getUserInformation(User user) throws EntityNotFoundException {
        UserInformationResource resource = new UserInformationResource();

        // On set le user
        resource.setIdUser(user.getId());
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

    /**
     * Service de création d'un user, accessible uniquement par l'admin ou un userManager
     * @return
     * @throws MarketPayException
     */
    public Long createUser(USER_PROFILE userProfileRequester, UserResource userResource) throws MarketPayException {
        //On créé le user
        User user = new User();
        user.setProfile(userResource.getProfile());

        //En fonction du profile du requester on n'a pas les mêmes droits de création
        if(USER_PROFILE.ADMIN_USER.equals(userProfileRequester)){
            //On est admin donc on vérifie que la bu et le shop est renseigné en fonction du profile voulu
            if(USER_PROFILE.ADMIN_USER.getCode() == userResource.getProfile()){
                //On veut créer un admin donc on ne lui associe pas de BU et de SHOP
                if(userResource.getIdBu() != null || userResource.getIdShop() != null){
                    throw new MarketPayException(HttpStatus.BAD_REQUEST, "idBu ou idShop présent pour créer un user admin");
                }
            } else if(USER_PROFILE.SUPER_USER.getCode() == userResource.getProfile()) {
                //On veut créer un superUser donc on lui associe une bu mais pas de shop
                if(userResource.getIdBu() == null || userResource.getIdShop() != null){
                    throw new MarketPayException(HttpStatus.BAD_REQUEST, "idBu null ou idShop présent pour créer un user super");
                }

                //On vérifie que la bu existe bien
                businessUnitRepository.findOne(userResource.getIdBu()).orElseThrow(() ->
                    new EntityNotFoundException(userResource.getIdBu(), "businessUnit")
                );

                user.setIdBu(userResource.getIdBu());
            } else {
                //On veut créer un userManager ou un user donc on lui associe un shop
                if(userResource.getIdBu() != null ||userResource.getIdShop() == null){
                    throw new MarketPayException(HttpStatus.BAD_REQUEST, "idBu présent ou idShop null pour créer un user ou user manager");
                }

                //On vérifie que le shop existe bien
                shopRepository.findOne(userResource.getIdShop()).orElseThrow(() ->
                    new EntityNotFoundException(userResource.getIdShop(), "shop")
                );

                user.setIdShop(userResource.getIdShop());
            }
        } else if(USER_PROFILE.USER_MANAGER.equals(userProfileRequester)) {
            //On est userManager donc on set le bon idShop et on vérifie le profile du user à créer
            if(USER_PROFILE.ADMIN_USER.getCode() == userResource.getProfile() || USER_PROFILE.SUPER_USER.getCode() == userResource.getProfile()){
                throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Pas autorisé à créer un superUser ou un adminUser");
            }

            user.setIdShop(userResource.getIdShop());
        } else {
            throw new MarketPayException(HttpStatus.UNAUTHORIZED, "Pas autorisé à créer un user");
        }

        //On set le password avec une valeur aléatoire
        //Le mot de passe sera changé à la premier authentification
        user.setPassword(PasswordUtils.PASSWORD_ENCODER.encode(RandomUtils.getRandowString(40)));

        //On set les autres champs: email, login, firstName, lastName
        //Et si tout est ok on sauvegarde le user
        EditUserRequest request = new EditMyUserRequest();
        request.setEmail(userResource.getEmail());
        request.setLogin(userResource.getLogin());
        request.setLastName(userResource.getLastName());
        request.setFirstName(userResource.getFirstName());
        user = editUserEntity(user, request, false);

        //Tout est OK on sauvegarde le user
        user = userRepository.save(user);

        //On envoi le mail de création du user
        keyPassService.sendKeyPass(user.getEmail(), true);

        return user.getId();
    }

    /**
     * Service d'edition d'un user
     * @param idUser
     * @param request
     * @return
     */
    public UserResource editUser(long idUser, EditUserRequest request) throws MarketPayException {
        //On récupère le user
        User user = getUserEntity(idUser);

        //On met à jour le user
        user = editUserEntity(user, request, false);

        return getUserResource(user);
    }

    /**
     * Service de récupération d'un user
     * @param idUser
     * @return
     */
    public UserResource getUser(long idUser) throws EntityNotFoundException {
        //On récupère le user
        return getUserResource(getUserEntity(idUser));
    }

    /**
     * Method qui retourne un userResource à partir d'un user
     * @param user
     * @return
     * @throws EntityNotFoundException
     */
    private UserResource getUserResource(User user) throws EntityNotFoundException {
        UserResource resource = new UserResource(user);

        // On récupère la BU de l'user si l'user est rattaché à la BU
        if (user.getIdBu() != null) {
            BusinessUnit businessUnit = businessUnitRepository.findOne(user.getIdBu()).orElseThrow(() ->
                new EntityNotFoundException(user.getIdBu(), "businessUnit")
            );
            resource.setIdBu(user.getIdBu());
            resource.setNameBu(businessUnit.getName());
        }

        //On récupère la BU à laquelle est rattaché le user, si le user est uniquement rattaché à un shop
        if(user.getIdShop() != null){
            Shop shop = shopRepository.findOne(user.getIdShop()).orElseThrow(() ->
                new EntityNotFoundException(user.getIdShop(), "shop")
            );
            BusinessUnit businessUnit = businessUnitRepository.findOne(shop.getIdBu()).orElseThrow(() ->
                new EntityNotFoundException(shop.getIdBu(), "businessUnit")
            );
            resource.setIdBu(shop.getIdBu());
            resource.setNameShop(shop.getName());
            resource.setNameBu(businessUnit.getName());
        }

        return resource;
    }

    /**
     * Method de récupération userEntity donné
     * @param idUser
     * @return
     * @throws EntityNotFoundException
     */
    private User getUserEntity(long idUser) throws EntityNotFoundException {
        return userRepository.findOne(idUser).orElseThrow(() ->
                new EntityNotFoundException(idUser, "user")
        );
    }

    /**
     * Method qui met à jour un userEntity à partir d'une request
     * @param user
     * @param request
     * @param isFromAccount : Quand on modifie son compte on ne peut pas modifier le login
     * @return
     * @throws MarketPayException
     */
    private User editUserEntity(User user, EditUserRequest request, Boolean isFromAccount) throws MarketPayException {
        //On met à jour le user
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if(!isFromAccount) {
            //On vérifie que le login n'existe pas déjà
            Optional<User> uLogin = userRepository.findByLogin(request.getLogin());
            if(uLogin.isPresent() && (user.getId() == null || !user.getId().equals(uLogin.get().getId()))){
                throw new MarketPayException(HttpStatus.IM_USED, "Login déjà utilisé", "login");
            }
            user.setLogin(request.getLogin());
        }

        //On vérifie l'email
        if(!MailUtils.checkValidEmail(request.getEmail())){
            throw new MarketPayException(HttpStatus.BAD_REQUEST, "invalid email " + request.getEmail(), "email");
        }
        //On vérifie que l'email n'existe pas déjà
        Optional<User> uEmail = userRepository.findUserByEmail(request.getEmail());
        if(uEmail.isPresent() && (user.getId() == null || !user.getId().equals(uEmail.get().getId()))){
            throw new MarketPayException(HttpStatus.IM_USED, "Email déjà utilisé", "email");
        }
        user.setEmail(request.getEmail());

        //Tout est OK on sauvegarde le user
        return userRepository.save(user);
    }

    /**
     * Service de suppression d'un user
     * @param idUserToDelete
     * @param idUserRequester
     */
    public void deleteUser(long idUserToDelete, long idUserRequester) throws MarketPayException {
        //On ne peut pas supprimer son propre user
        if(idUserRequester == idUserToDelete){
            throw new MarketPayException(HttpStatus.BAD_REQUEST, "Impossible de supprimer son propre user");
        }

        //On récupère le user
        User user = getUserEntity(idUserToDelete);

        //On supprime le keyPass associé au user s'il existe
        userKeyPassRepository.findByIdUser(user.getId()).ifPresent(userKeyPass -> {
            userKeyPassRepository.delete(userKeyPass);
        });

        //On supprime le user
        userRepository.delete(user);
    }

    /**
     * Service d'edition du user connecté
     * @param idUser
     * @param request
     * @return
     */
    public UserResource editMyUser(long idUser, EditMyUserRequest request) throws MarketPayException {
        //On récupère le user
        User user = getUserEntity(idUser);

        //On change le password s'il est renseigné
        if(request.getPassword() != null){
            user.setPassword(PasswordUtils.PASSWORD_ENCODER.encode(request.getPassword()));
        }

        //On met à jour le user
        user = editUserEntity(user, request, true);

        return getUserResource(user);
    }
}
