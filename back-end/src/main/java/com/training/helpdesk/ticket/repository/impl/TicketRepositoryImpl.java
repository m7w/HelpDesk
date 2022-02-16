package com.training.helpdesk.ticket.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.repository.TicketRepository;
import com.training.helpdesk.user.domain.Role;

import org.springframework.stereotype.Repository;

@Repository
public class TicketRepositoryImpl implements TicketRepository {

    private static final String FIND_ALL_EMPLOYEE = "from Ticket t join fetch t.assignee"
        + " join fetch t.owner join fetch t.category join fetch t.approver where owner_id = :id";
        //+ " order by t.urgency, t.desiredResolutionDate desc";

    private static final String FIND_ALL_MANAGER = "from Ticket t join fetch t.assignee"
        + " join fetch t.owner join fetch t.category join fetch t.approver where owner_id = :id"
        + " or (owner_id in (select id from User where role_id = 0) and t.state = 1)"
        + " or (approver_id = :id and state_id in (2, 3, 4, 5, 6))";
        //+ " order by t.urgency, t.desiredResolutionDate desc";

    private static final String FIND_ALL_ENGINEER = "from Ticket t join fetch t.assignee"
        + " join fetch t.owner join fetch t.category join fetch t.approver where state_id = 2"
        + " or (assignee_id = :id and state_id in (4, 5))";
        //+ " order by t.urgency, t.desiredResolutionDate desc";
    
    private static final String ID = "id";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Ticket> findAllByUser(Long id, Role role, int pageNumber, int pageSize, String orderBy, String order) {

        String query;
        switch (role) {
            case ROLE_EMPLOYEE:
                query = FIND_ALL_EMPLOYEE;
                break;
            case ROLE_MANAGER:
                query = FIND_ALL_MANAGER;
                break;
            default:
                query = FIND_ALL_ENGINEER;
                break;
        }

        Map<String, String> orderByMap = new HashMap<>();
        orderByMap.put("id", "t.id");
        orderByMap.put("name", "t.name");
        orderByMap.put("resolutionDate", "t.desiredResolutionDate");
        orderByMap.put("urgency", "t.urgency");
        orderByMap.put("status", "t.state");

        return entityManager.createQuery(query + " order by " + orderByMap.get(orderBy) + " " + order, Ticket.class)
            .setParameter(ID, id)
            //.setFirstResult(pageNumber * pageSize)
            //.setMaxResults(pageSize)
            .getResultList();

/*
 *        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
 *        CriteriaQuery<Ticket> cq = cb.createQuery(Ticket.class);
 *        Root<Ticket> root = cq.from(Ticket.class);
 *        switch (role) {
 *            case ROLE_EMPLOYEE:
 *                Predicate employeeOwnerEqualsId = cb.equal(root.get("owner"), id);
 *                cq.select(root).where(employeeOwnerEqualsId);
 *                break;
 *            case ROLE_MANAGER:
 *                Predicate managerOwnerEqualsId = cb.equal(root.get("owner"), id);
 *                Predicate managerOwnerHaveRoleAndState = cb.and(root.get("owner").in(
 *        + " or (owner_id in (select id from User where role_id = 0) and t.state = 1)"
 *                cq.select(root).where(managerOwnerEqualsId);
 *                break;
 *            default:
 *                query = FIND_ALL_ENGINEER;
 *                break;
 *        }
 *
 *        List<Order> order = new ArrayList<>();
 *        order.add(cb.asc(root.get("urgency")));
 *        order.add(cb.desc(root.get("desiredResolutionDate")));
 *        cq.orderBy(order);
 *
 *        TypedQuery<Ticket> typedQuery = entityManager.createQuery(cq);
 *        return typedQuery.getResultList();
 */
    }
}
