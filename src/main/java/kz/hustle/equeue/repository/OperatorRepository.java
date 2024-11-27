package kz.hustle.equeue.repository;

import kz.hustle.equeue.entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator, Long> {
    Optional<Operator> findByUserId(Long userId);
}
