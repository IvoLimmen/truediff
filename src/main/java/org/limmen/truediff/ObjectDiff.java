package org.limmen.truediff;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectDiff {

  public List<Diff> compare(Object base, Object target) {
    return compareObjects(base, target, new ArrayDeque<>());
  }

  private List<Diff> compareCollection(Collection<?> base, Collection<?> target, String propertyName, Deque<String> stack) {
    List<Diff> changes = new ArrayList<>();

    if (base == null && target == null) {
      // nothing changes
    } else if (base == null && target != null) {
      changes.addAll(target.stream()
              .map(item -> Diff.builder()
              .type(DiffType.ADD)
              .oldValue(null)
              .newValue(item.toString())
              .propertyName(propertyName)
              .propertyPath(flattenStack(stack))
              .build())
              .collect(Collectors.toList()));
    } else if (base != null && target == null) {
      changes.addAll(base.stream()
              .map(item -> Diff.builder()
              .type(DiffType.DELETE)
              .oldValue(item.toString())
              .newValue(null)
              .propertyName(propertyName)
              .propertyPath(flattenStack(stack))
              .build())
              .collect(Collectors.toList()));
    } else if (base != null && target != null) {

      changes.addAll(target.stream()
              .filter(item -> !base.contains(item))
              .map(item -> Diff.builder()
              .type(DiffType.ADD)
              .oldValue(null)
              .newValue(item.toString())
              .propertyName(propertyName)
              .propertyPath(flattenStack(stack))
              .build())
              .collect(Collectors.toList()));

      changes.addAll(base.stream()
              .filter(item -> !target.contains(item))
              .map(item -> Diff.builder()
              .type(DiffType.DELETE)
              .oldValue(item.toString())
              .newValue(null)
              .propertyName(propertyName)
              .propertyPath(flattenStack(stack))
              .build())
              .collect(Collectors.toList()));
    }

    return changes;
  }

  private List<Diff> compareObjects(Object base, Object target, Deque<String> stack) {
    List<Diff> changes = new ArrayList<>();

    for (Field field : base.getClass().getDeclaredFields()) {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      Object baseValue = getValue(field, base);
      Object targetValue = getValue(field, target);

      if (Collection.class.isAssignableFrom(field.getType())) {
        changes.addAll(compareCollection((Collection<?>) baseValue, (Collection<?>) targetValue, field.getName(), stack));
      } else {
        changes.addAll(comparePrimitiveField(baseValue, targetValue, field.getName(), stack));
      }
    }

    return changes;
  }

  private List<Diff> comparePrimitiveField(Object base, Object target, String propertyName, Deque<String> stack) {
    List<Diff> changes = new ArrayList<>();

    if (base == null && target == null) {
      // nothing changes
    } else if (base == null && target != null) {
      changes.add(Diff.builder()
              .type(DiffType.ADD)
              .oldValue(null)
              .newValue(target.toString())
              .propertyName(propertyName)
              .propertyPath(flattenStack(stack))
              .build());
    } else if (base != null && target == null) {
      changes.add(Diff.builder()
              .type(DiffType.DELETE)
              .oldValue(base.toString())
              .newValue(null)
              .propertyName(propertyName)
              .propertyPath(flattenStack(stack))
              .build());
    } else if (base != null && target != null) {
      if (!base.equals(target)) {
        changes.add(Diff.builder()
                .type(DiffType.UPDATE)
                .oldValue(base.toString())
                .newValue(target.toString())
                .propertyName(propertyName)
                .propertyPath(flattenStack(stack))
                .build());
      }
    }

    return changes;
  }

  private String flattenStack(Deque<String> stack) {
    return "/" + stack.stream().collect(Collectors.joining("/"));
  }

  private Object getValue(Field field, Object object) {
    try {
      return field.get(object);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      return null;
    }
  }

}
