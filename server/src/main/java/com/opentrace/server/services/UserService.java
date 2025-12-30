package com.opentrace.server.services;

import com.opentrace.server.mappers.UserMapper;
import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserDTO saveOrUpdate(GoogleUserDTO googleUser) {

        UserEntity user = userRepository.findByGoogleSub(googleUser.getSub())
                .map(existingUser -> {
                    existingUser.setName(googleUser.getName());
                    existingUser.setEmail(googleUser.getEmail());
                    existingUser.setAvatarUrl(googleUser.getPicture());
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    UserEntity newUser = userMapper.toEntity(googleUser);
                    newUser.setTokenVersion(1);
                    newUser.setPriority(0);

                    UserEntity savedUser = userRepository.save(newUser);

                    roleService.assignDefaultRoles(savedUser);

                    return savedUser;
                });

        return userMapper.toDto(user);
    }


}
