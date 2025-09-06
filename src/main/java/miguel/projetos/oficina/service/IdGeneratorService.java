package miguel.projetos.oficina.service;

import miguel.projetos.oficina.entity.IdSequence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class IdGeneratorService {
    @Autowired
    private EntityManager entityManager;

    /**
     * Obtém o próximo ID disponível para uma determinada entidade.
     * @param entityName O nome da entidade (ex: "cliente", "funcionario").
     * @return O próximo número de ID sequencial.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long getNextId(String entityName) {
        // Usa um bloqueio pessimista para garantir que nenhuma outra transação leia o valor ao mesmo tempo
        IdSequence sequence = entityManager.find(IdSequence.class, entityName, LockModeType.PESSIMISTIC_WRITE);

        if (sequence == null) {
            throw new IllegalStateException("Nenhuma sequência encontrada para a entidade: " + entityName);
        }

        // Incrementa o ID e atualiza no banco
        Long nextId = sequence.getLast_id() + 1;
        sequence.setLast_id(nextId);
        entityManager.merge(sequence);

        return nextId;
    }
}
