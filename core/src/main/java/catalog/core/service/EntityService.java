package catalog.core.service;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface EntityService <T> {
    List<T> getAllEntities();

    T saveEntity(T entity);

    T updateEntity(T entity);

    void deleteEntity(Long id);
}