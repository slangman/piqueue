package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.HustleQueueEntity;
import kz.hustle.equeue.repository.HustleQueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class HustleQueueService {

    private final HustleQueueRepository repository;
    //private final AtomicInteger current;

    public HustleQueueService(HustleQueueRepository repository) {
        this.repository = repository;
        //this.current = new AtomicInteger(0);
    }

    @Transactional
    public int addToQueue() {
        int position = 1;

        // Find the current max position from the database
        List<HustleQueueEntity> queue = repository.findAll();
        if (!queue.isEmpty()) {
            position = queue.get(queue.size() - 1).getPosition() + 1;
        }

        // Save the new position to the database
        HustleQueueEntity entity = new HustleQueueEntity();
        entity.setPosition(position);
        repository.save(entity);

        return position;
    }

    public Integer getNext() {
        // Get the current state of the queue
        Integer result = null;
        List<HustleQueueEntity> queue = repository.findAll();
        if (!queue.isEmpty()) {
            result = queue.get(0).getPosition();
            repository.delete(queue.get(0));
        }
        return result;
    }

    /*
    public Integer getNext() {
        // Get the current state of the queue
        List<HustleQueueEntity> queue = repository.findAll();

        if (queue.size() > current.get()) {
            return queue.get(current.getAndIncrement()).getPosition();
        } else {
            return null; // No more elements
        }
    }
     */

    //TODO: Remove current client for all operators
    public void resetQueue() {
        repository.deleteAll();
        //current.set(0);
    }
}
