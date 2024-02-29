package se.lexicon.g48todoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.lexicon.g48todoapi.domain.dto.RoleDTOView;
import se.lexicon.g48todoapi.domain.dto.UserDTOForm;
import se.lexicon.g48todoapi.domain.dto.UserDTOView;
import se.lexicon.g48todoapi.domain.entity.Role;
import se.lexicon.g48todoapi.domain.entity.User;
import se.lexicon.g48todoapi.exception.DataDuplicateException;
import se.lexicon.g48todoapi.exception.DataNotFoundException;
import se.lexicon.g48todoapi.repository.RoleRepository;
import se.lexicon.g48todoapi.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    //todo: inject the necessary classes as dependency
    @Override
    public UserDTOView register(UserDTOForm userDTOForm) {

        //Check params
        if (userDTOForm == null) throw new IllegalArgumentException("User form is null.");

        //check if email already exists
        boolean isExistEmail = userRepository.existsByEmail(userDTOForm.getEmail());
        if (isExistEmail) throw new DataDuplicateException("Email already exists.");

        //Retrieve and validate roles
        Set<Role> roleList = userDTOForm.getRoles()
                .stream()
                .map(
                        roleDTOForm -> roleRepository.findById(roleDTOForm.getId())
                                .orElseThrow(() -> new DataNotFoundException("This Role is not valid.")))
                .collect(Collectors.toSet());
        //Convert dto to entity
        //Hash password
        User user = new User(userDTOForm.getEmail(), passwordEncoder.encode(userDTOForm.getPassword()));
        for (Role role : roleList) {
            user.addRole(role);
        }

        //Create a new User entity and save it to database
        User savedUser = userRepository.save(user);
        //Convert saved user (entity) to dto view
        Set<RoleDTOView> roleDTOViews = savedUser.getRoles()
                .stream()
                .map(
                        role -> RoleDTOView.builder()
                                .id(role.getId())
                                .name(role.getName())
                                .build())
                .collect(Collectors.toSet());


        return UserDTOView.builder()
                .email(savedUser.getEmail())
                .roles(roleDTOViews)
                .build();
    }


    @Override
    public UserDTOView getByEmail(String email) {

        User user = userRepository.findById(email).orElseThrow(() -> new DataNotFoundException("Email does not exist."));

        //When we found the user we get roles and convert it to RoleDTOView
        Set<RoleDTOView> roleDTOViews = user.getRoles()
                .stream()
                .map(
                        role -> RoleDTOView.builder()
                                .id(role.getId())
                                .name(role.getName())
                                .build())
                .collect(Collectors.toSet());
        //Build UserDTOView out of user
        return UserDTOView.builder()
                .email(user.getEmail())
                .roles(roleDTOViews)
                .build();
    }

    @Override
    public void disableByEmail(String email) {
        if (!userRepository.existsByEmail(email)) throw new DataNotFoundException("Email does not exist.");
        userRepository.updateExpiredByEmail(email, true);

    }

    @Override
    public void enableByEmail(String email) {
        if (!userRepository.existsByEmail(email)) throw new DataNotFoundException("Email does not exist.");
        userRepository.updateExpiredByEmail(email, false);
    }
}
