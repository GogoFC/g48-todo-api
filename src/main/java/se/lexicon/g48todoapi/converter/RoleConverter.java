package se.lexicon.g48todoapi.converter;

import se.lexicon.g48todoapi.domain.dto.RoleDTOView;
import se.lexicon.g48todoapi.domain.entity.Role;


public interface RoleConverter {
    RoleDTOView toRoleDTOView(Role entity);

    Role toRoleEntity(RoleDTOView dtoView);
}
