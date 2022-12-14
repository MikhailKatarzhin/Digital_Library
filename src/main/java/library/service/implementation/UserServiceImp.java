package library.service.implementation;

import library.config.WebSecurityConfig;
import library.model.Book;
import library.model.RoleOfUser;
import library.model.User;
import library.model.Wallet;
import library.repository.UserRepository;
import library.repository.WalletRepository;
import library.service.interfaces.RoleOfUserService;
import library.service.interfaces.UserService;
import library.service.interfaces.UserStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    private final BCryptPasswordEncoder passwordEncoder;
    private final HttpServletRequest httpServletRequest;
    private final RoleOfUserService roleOfUserService;
    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final WalletRepository walletRepository;

    public UserServiceImp(BCryptPasswordEncoder passwordEncoder, HttpServletRequest httpServletRequest,
                          RoleOfUserService roleOfUserService, UserRepository userRepository, UserStatusService userStatusService,
                          WalletRepository walletRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.httpServletRequest = httpServletRequest;
        this.roleOfUserService = roleOfUserService;
        this.userRepository = userRepository;
        this.userStatusService = userStatusService;
        this.walletRepository = walletRepository;
    }


    public User getRemoteUser() {
        return userRepository.findByUsername(httpServletRequest.getRemoteUser());
    }

    public Long getRemoteUserId() {
        return getRemoteUser().getId();
    }

    public String getRemoteUserEmail() {
        return getRemoteUser().getEmail();
    }

    public boolean checkRemoteUserPassword(String password) {
        return WebSecurityConfig.encoder().matches(password, getRemoteUser().getPassword());
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User signUp(User user, Set<RoleOfUser> roleSet) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleOfUsers(roleSet);
        user.setStatus(userStatusService.getDefault());
        user = userRepository.save(user);
        logger.info("Signed up new user [id:{}] with roles: {}", user.getId()
                , user.getRoleOfUsers().stream().map(RoleOfUser::getName).collect(Collectors.joining(", ")));

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0L);
        walletRepository.save(wallet);
        logger.info("Inserted new wallet for user [id:{}] with balance: {}", wallet.getId(), wallet.getBalance());
        return user;
    }

    public User grantRoleAuthor(){
        User user = getRemoteUser();
        if (1 == userRepository.countUserRoleByRoleNameAndUserId("AUTHOR", user.getId()))
            return user;
        Set<RoleOfUser> roleOfUsers = user.getRoleOfUsers();
        RoleOfUser roleOfUser = roleOfUserService.findByName("AUTHOR");
        roleOfUsers.add(roleOfUser);
        user.setRoleOfUsers(roleOfUsers);
        userRepository.save(user);
        logger.info("Granted role '{}'[id:{}] to user [id:{}]",roleOfUser.getName(), roleOfUser.getId(), user.getId());
        return user;
    }

    public boolean emailExists(String email) {
        return userRepository.countByEmail(email) != 0;
    }

    public User saveEmail(String email) {
        User user = getRemoteUser();
        String oldEmail = user.getEmail();
        user.setEmail(email);
        user =  userRepository.save(user);
        logger.info("User {}[id:{}] change email {} to {}", user.getUsername(), user.getId(), oldEmail, user.getEmail());
        return user;
    }

    @Override
    public User addPurchasedBook(Book book) {
        if (!purchaseBook(book))
            return null;
        User reader = getRemoteUser();
        Set<Book> purchasedBook = reader.getPurchasedBooks();
        purchasedBook.add(book);
        reader.setPurchasedBooks(purchasedBook);
        logger.info("User {}[id:{}] bought book[id:{}]", reader.getUsername(), reader.getId(), book.getId());
        return userRepository.save(reader);
    }

    public boolean purchaseBook(Book book){
        long cost = book.getCost();
        User user = getRemoteUser();
        Wallet wallet = user.getWallet();
        long balance = wallet.getBalance();
        if (cost > balance)
            return false;
        wallet.debitingFunds(cost);
        user.setWallet(wallet);
        userRepository.save(user);
        user = book.getCreator();
        wallet = user.getWallet();
        wallet.replenishmentFunds(book.getCost());
        user.setWallet(wallet);
        userRepository.save(user);
        return true;
    }

    @Override
    public User savePassword(String password) {
        User user = getRemoteUser();
        user.setPassword(WebSecurityConfig.encoder().encode(password));
        user = userRepository.save(user);
        logger.info("User {}[id:{}] change password to {}", user.getUsername(), user.getId(), user.getPassword());
        return user;
    }
}
