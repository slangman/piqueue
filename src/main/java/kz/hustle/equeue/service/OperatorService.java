package kz.hustle.equeue.service;

import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.repository.OperatorRepository;
import kz.hustle.equeue.service.tts.GoogleTTSProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OperatorService {

    private final OperatorRepository operatorRepository;
    private final HustleQueueService queueService;
    private static final Logger logger = LoggerFactory.getLogger(OperatorService.class);

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
            logger.info("Client with number {} has been called to the operator {}",
                    next,
                    operator.getUser().getDisplayName());
            saveOperator(operator);
        } else {
            logger.info("Operator called the next client but there are no new clients in the queue.");
        }
    }

    public void callCurrentClient(Operator operator) {
        if (operator.getCurrent() == null) {
            logger.info("Operator called the current client but the queue is empty.");
        } else {
            logger.info("Client with number {} has been repeatedly called to the operator {}",
                    operator.getCurrent(),
                    operator.getUser().getDisplayName());
        }
    }
}
