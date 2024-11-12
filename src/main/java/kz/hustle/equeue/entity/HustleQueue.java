package kz.hustle.equeue.entity;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HustleQueue {

    private final List<Integer> queue;
    private final AtomicInteger current;

    public HustleQueue() {
        this.queue = Collections.synchronizedList(new ArrayList<>());
        this.current = new AtomicInteger(0);
    }

    public int addToQueue() {
        int position = 1;
        if (!queue.isEmpty()) {
            position = queue.get(queue.size()-1) + 1;
        }
        queue.add(position);
        return position;
    }

    public Integer getNext() {
        if (queue.size() > current.get()) {
            return queue.get(current.getAndIncrement());
        } else {
            return null;
        }
    }
}