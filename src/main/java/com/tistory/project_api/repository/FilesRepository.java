package com.tistory.project_api.repository;

import com.tistory.project_api.domain.entity.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<FilesEntity, Integer> {
}
