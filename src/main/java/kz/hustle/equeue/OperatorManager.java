package kz.hustle.equeue;

import kz.hustle.equeue.entity.Operator;

import java.util.HashMap;
import java.util.Map;

public class OperatorManager {
    private final Map<Long, Operator> operators = new HashMap<>();

    public void addOperator(Operator operator) {
        operators.put(operator.getUser().getId(), operator);
    }

    public Operator getOperator(Long id) {
        return operators.get(id);
    }
}
