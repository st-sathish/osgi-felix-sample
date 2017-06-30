package com.sathish.util.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MultiMap<A, B> {
  private final Map<A, List<B>> map;

  public MultiMap(Map<A, List<B>> map) {
    this.map = map;
  }

  public static <A, B> MultiMap<A, B> multiHashMapWithArrayList() {
    return new MultiMap<A, B>(new HashMap<A, List<B>>()) {
      @Override
      protected List<B> newList() {
        return new ArrayList<B>();
      }
    };
  }

  public Map<A, List<B>> value() {
    return map;
  }

  public MultiMap<A, B> put(A key, B value) {
    List<B> current = map.get(key);
    if (current == null) {
      current = newList();
      map.put(key, current);
    }
    current.add(value);
    return this;
  }

  public MultiMap<A, B> putAll(A key, List<B> values) {
    List<B> current = map.get(key);
    if (current == null) {
      current = newList();
      map.put(key, current);
    }
    current.addAll(values);
    return this;
  }

  protected abstract List<B> newList();
}
