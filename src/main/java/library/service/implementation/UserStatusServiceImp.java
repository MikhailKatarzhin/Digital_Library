package library.service.implementation;

import library.model.UserStatus;
import library.repository.UserStatusRepository;
import library.service.interfaces.UserStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserStatusServiceImp implements UserStatusService {

    private final static Logger logger = LoggerFactory.getLogger(UserStatusServiceImp.class);
    private final String defaultName = "ACTIVE";
    private final UserStatusRepository userStatusRepository;

    public UserStatusServiceImp(UserStatusRepository userStatusRepository) {
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UserStatus getByName(String userStatusName) {
        UserStatus userStatus = userStatusRepository.findByName(userStatusName);
        logger.info("Requested 'User_status' by [Name:{}]. Response: {}", userStatusName, userStatus.toString());
        return userStatus;
    }

    @Override
    public UserStatus getDefault() {
        return userStatusRepository.findByName(defaultName);
    }
}
