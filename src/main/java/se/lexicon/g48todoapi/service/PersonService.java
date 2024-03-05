package se.lexicon.g48todoapi.service;

import se.lexicon.g48todoapi.domain.dto.PersonDTOForm;
import se.lexicon.g48todoapi.domain.dto.PersonDTOView;

import java.util.List;

public interface PersonService {
    PersonDTOView create(PersonDTOForm personDtoForm);
    PersonDTOView findById(Long id);
    List<PersonDTOView> findAll();
    PersonDTOView update(PersonDTOForm personDtoForm);
    void delete(Long id);
}
