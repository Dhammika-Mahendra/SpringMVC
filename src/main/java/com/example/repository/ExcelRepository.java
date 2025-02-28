package com.example.repository;

import com.example.model.BaseTableModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExcelRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public <T extends BaseTableModel> void fetchData(List<T> models) {
        try {
            for (T model : models) {
                entityManager.persist(model);
            }
            entityManager.flush();
        } catch (Exception e) {
            System.err.println("Database error while persisting data: " + e.getMessage());
        }
    }
}
