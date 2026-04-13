package net.armaments.api.data.mapped_data;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface DataMapper {
    Map<DataKey<?>, Object> dataMap();

    @Nullable @SuppressWarnings("unchecked")
    default <T> T getData(DataKey<T> key) {
        return (T) this.dataMap().get(key);
    }

    @SuppressWarnings("unchecked")
    default <T> T getDataOrDefault(DataKey<T> key, T defaultData) {
        return (T) this.dataMap().getOrDefault(key, defaultData);
    }

    default <T> void setData(DataKey<T> key, T data) {
        this.dataMap().put(key, data);
    }

    default void clearData() {
        this.dataMap().clear();
    }
}
