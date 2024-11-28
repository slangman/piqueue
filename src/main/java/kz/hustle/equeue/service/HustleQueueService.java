package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.HustleQueue;
import kz.hustle.equeue.repository.HustleQueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HustleQueueService {

    private final HustleQueueRepository repository;

    public HustleQueueService(HustleQueueRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public int addToQueue() {
        int position = 1;

        // Find the current max position from the database
        List<HustleQueue> queue = repository.findAll();
        if (!queue.isEmpty()) {
            position = queue.get(queue.size() - 1).getPosition() + 1;
        }

        // Save the new position to the database
        HustleQueue entity = new HustleQueue();
        entity.setPosition(position);
        repository.save(entity);

        return position;
    }

    public Integer getNext() {
        // Get the current state of the queue
        Integer result = null;
        List<HustleQueue> queue = repository.findAll();
        if (!queue.isEmpty()) {
            result = queue.get(0).getPosition();
            repository.delete(queue.get(0));
        }
        return result;
    }

    //TODO: Remove current client for all operators
    public void resetQueue() {
        repository.deleteAll();
        //current.set(0);
    }
}
