package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OperatorService {

    private final OperatorRepository operatorRepository;
    private final HustleQueueService queueService;

    public OperatorService(OperatorRepository operatorRepository, HustleQueueService queueService) {
        this.operatorRepository = operatorRepository;
        this.queueService = queueService;
    }

    public Operator saveOperator(Operator operator) {
        return operatorRepository.save(operator);
    }

    public Operator getOperator(Long id) {
        return operatorRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Optional<Operator> getOperatorByUserId(Long userId) {
        return operatorRepository.findByUserId(userId);
    }

    public void callNextClient(Operator operator) {
        Integer next = queueService.getNext();
        if (next != null) {
            operator.setCurrentClientNumber(next);
            System.out.println("Client with number " + next + " please proceed to operator " + operator.getUser().getDisplayName());
            saveOperator(operator);
        } else {
            System.out.println("No new clients in the queue");
        }
    }

    public void callCurrentClient(Operator operator) {
        if (operator.getCurrent() == null) {
            System.out.println("Client queue is empty");
        } else {
            System.out.println("Repeat: Client with number " + operator.getCurrent() + " please proceed to operator " + operator.getUser().getDisplayName());
        }
    }
}
