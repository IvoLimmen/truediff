package org.limmen.truediff;

import java.util.ArrayList;
import java.util.Arrays;
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

  @Test
  public void lists() {
    Complex base = new Complex();
    Complex target = new Complex();

    base.setNames(Arrays.asList("one", "two"));
    target.setNames(Arrays.asList("three"));
    target.setNumber(2);

    List<Diff> result = fixture.compare(base, target);

    assertEquals(4, result.size());

    List<Diff> expected = new ArrayList<>();

    expected.add(Diff.builder()
            .type(DiffType.ADD)
            .newValue("three")
            .oldValue(null)
            .propertyPath("/")
            .propertyName("names")
            .build());
    expected.add(Diff.builder()
            .type(DiffType.DELETE)
            .newValue(null)
            .oldValue("one")
            .propertyPath("/")
            .propertyName("names")
            .build());
    expected.add(Diff.builder()
            .type(DiffType.DELETE)
            .newValue(null)
            .oldValue("two")
            .propertyPath("/")
            .propertyName("names")
            .build());
    expected.add(Diff.builder()
            .type(DiffType.ADD)
            .newValue("2")
            .oldValue(null)
            .propertyPath("/")
            .propertyName("number")
            .build());

    assertEquals(expected.get(0), result.get(0));
    assertEquals(expected.get(1), result.get(1));
    assertEquals(expected.get(2), result.get(2));
    assertEquals(expected.get(3), result.get(3));
  }
}
