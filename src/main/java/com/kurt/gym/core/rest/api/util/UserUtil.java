package com.kurt.gym.core.rest.api.util;

import com.kurt.gym.auth.model.services.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    private static UserRepository userRepository;

    public static void initRepositories(UserRepository userRepository) {
        UserUtil.userRepository = userRepository;
    }

    UserUtil() {
    }

}
