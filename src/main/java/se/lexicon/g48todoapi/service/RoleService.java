package se.lexicon.g48todoapi.service;

import se.lexicon.g48todoapi.domain.dto.RoleDTOView;

import java.util.List;

public interface RoleService {

    List<RoleDTOView> getAll();
}
