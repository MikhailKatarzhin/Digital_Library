package library.service.interfaces;

import library.model.RoleOfUser;

import java.util.List;
import java.util.Set;

public interface RoleOfUserService {

    List<RoleOfUser> findAllExceptName(String exceptingName);

    RoleOfUser findByName(String name);

    Set<RoleOfUser> getRoleSetByIds(Long[] ids);
}
