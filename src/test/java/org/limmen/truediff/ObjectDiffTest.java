package org.limmen.truediff;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectDiffTest {

  private final ObjectDiff fixture = new ObjectDiff();

  @Test
  public void testAddAndDelete() {
    Simple base = new Simple();
    base.setFirstName("Joe");

    Simple target = new Simple();
    target.setFirstName(null);
    target.setLastName("Smith");

    List<Diff> result = fixture.compare(base, target);

    Diff expected1 = Diff.builder()
            .type(DiffType.DELETE)
            .newValue(null)
            .oldValue("Joe")
            .propertyPath("/")
            .propertyName("firstName")
            .build();
    Diff expected2 = Diff.builder()
            .type(DiffType.ADD)
            .newValue("Smith")
            .oldValue(null)
            .propertyPath("/")
            .propertyName("lastName")
            .build();

    assertEquals(2, result.size());
    assertEquals(expected1, result.get(0));
    assertEquals(expected2, result.get(1));
  }

  @Test
  public void testUpdate() {
    Simple base = new Simple();
    base.setFirstName("Joe");
    base.setLastName("SMith");

    Simple target = new Simple();
    target.setFirstName("Joe");
    target.setLastName("Smith");

    List<Diff> result = fixture.compare(base, target);

    Diff expected = Diff.builder()
            .type(DiffType.UPDATE)
            .newValue("Smith")
            .oldValue("SMith")
            .propertyPath("/")
            .propertyName("lastName")
            .build();

    assertEquals(1, result.size());
    assertEquals(expected, result.get(0));
  }
}
