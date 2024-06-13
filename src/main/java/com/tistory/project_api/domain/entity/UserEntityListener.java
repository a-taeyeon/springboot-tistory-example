package com.tistory.project_api.domain.entity;

import com.tistory.framework.jpa.component.IdGenerator;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEntityListener {
    @Autowired
    private static IdGenerator idGenerator;

    @Autowired
    public void setUserIdGenerator(IdGenerator idGenerator) {
        UserEntityListener.idGenerator = idGenerator;
    }

    @PrePersist
    public void prePersist(UserEntity userEntity) {
        if (userEntity.getId() == null) {
            String generatedId = idGenerator.generateId("user");
            userEntity.setId(generatedId);
        }
    }
}
