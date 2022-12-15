package library.service.interfaces;

import library.model.RoleOfUser;
import library.model.User;

import java.util.Set;


public interface UserService {

    User getRemoteUser();

    Long getRemoteUserId();

    String getRemoteUserEmail();

    boolean checkRemoteUserPassword(String password);

    User getById(Long id);

    User getByUsername(String username);

    User signUp(User user, Set<RoleOfUser> roleSet);

    boolean emailExists(String email);

    User saveEmail(String email);

    User savePassword(String password);
}