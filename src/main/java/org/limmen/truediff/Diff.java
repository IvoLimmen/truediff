package org.limmen.truediff;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class Diff {

  private final String newValue;
  private final String oldValue;
  private final String propertyName;
  private final String propertyPath;
  private final DiffType type;

  @Builder
  public Diff(DiffType type, String newValue, String oldValue, String propertyName, String propertyPath) {
    this.type = type;
    this.newValue = newValue;
    this.oldValue = oldValue;
    this.propertyName = propertyName;
    this.propertyPath = propertyPath;
  }

}
