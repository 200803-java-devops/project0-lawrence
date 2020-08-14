package com.project0.lawrencedang;

public interface DAO<T> {
    public T get();
    public void put(T t);
}
