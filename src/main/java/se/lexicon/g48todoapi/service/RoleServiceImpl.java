package se.lexicon.g48todoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.g48todoapi.converter.RoleConverter;
import se.lexicon.g48todoapi.domain.dto.RoleDTOView;
import se.lexicon.g48todoapi.domain.entity.Role;
import se.lexicon.g48todoapi.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleConverter roleConverter) {
        this.roleRepository = roleRepository;
        this.roleConverter = roleConverter;
    }

    @Override
    public List<RoleDTOView> getAll() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDTOView> roleDTOViews = new ArrayList<>();

        for (Role entity : roles) {
            roleDTOViews.add(roleConverter.toRoleDTOView(entity));
        }

        return roleDTOViews;
    }
}
