package library.service.implementation;

import library.model.RoleOfUser;
import library.repository.RoleOfUserRepository;
import library.service.interfaces.RoleOfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleOfUserServiceImp implements RoleOfUserService {

    private final RoleOfUserRepository roleRepository;

    @Autowired
    public RoleOfUserServiceImp(RoleOfUserRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleOfUser> findAllExceptName(String exceptingName) {
        return roleRepository.findAllExceptName(exceptingName);
    }

    @Override
    public RoleOfUser findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Set<RoleOfUser> getRoleSetByIds(Long[] ids) {
        Set<RoleOfUser> roleSet = new LinkedHashSet<>();
        for (Long roleId : ids) {
            roleRepository.findById(roleId).ifPresent(roleSet::add);
        }
        return roleSet;
    }
}
