package library.service.interfaces;

import library.model.UserStatus;

public interface UserStatusService {
    UserStatus getByName(String userStatusName);
    UserStatus getDefault();
}
