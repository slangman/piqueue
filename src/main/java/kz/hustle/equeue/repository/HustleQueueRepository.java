package kz.hustle.equeue.repository;

import kz.hustle.equeue.entity.HustleQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HustleQueueRepository extends JpaRepository<HustleQueue, Long> {
}
