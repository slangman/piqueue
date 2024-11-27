package kz.hustle.equeue.repository;

import kz.hustle.equeue.entity.HustleQueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HustleQueueRepository extends JpaRepository<HustleQueueEntity, Long> {
}
