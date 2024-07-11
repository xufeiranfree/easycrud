package io.easycrud.example.repository;

import io.easycrud.example.pojo.ExampleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleJpaRepository extends JpaRepository<ExampleDO, String> {
}
