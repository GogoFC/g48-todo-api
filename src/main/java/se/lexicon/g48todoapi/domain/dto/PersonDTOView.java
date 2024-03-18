package se.lexicon.g48todoapi.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonDTOView {

  private Long id;
  private String name;

}