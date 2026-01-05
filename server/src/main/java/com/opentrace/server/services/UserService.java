package com.opentrace.server.services;

import com.opentrace.server.utils.mappers.UserMapper;
import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final RolePermissionService rolePermissionService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getByGoogleSub(String sub) {
        return userRepository.findByGoogleSub(sub)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with sub: " + sub));
    }

    public UserDTO getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public UserDTO saveOrUpdate(GoogleUserDTO googleUser) {

        UserEntity user = userRepository.findByGoogleSub(googleUser.getSub())
                .map(existingUser -> {
                    existingUser.setName(googleUser.getName());
                    existingUser.setEmail(googleUser.getEmail());
                    existingUser.setAvatarUrl(googleUser.getPicture());
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    UserEntity newUserEntity = userMapper.toEntity(googleUser);
                    newUserEntity.setTokenVersion(1);
                    newUserEntity.setPriority(0);

                    UserEntity savedUser = userRepository.save(newUserEntity);

                    UserDTO savedUserDTO = userMapper.toDto(savedUser);

                    rolePermissionService.assignRolesPermission(savedUserDTO);

                    return savedUser;
                });

        return userMapper.toDto(user);
    }


}
