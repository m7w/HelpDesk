package com.training.helpdesk.ticket.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.ticket.repository.QueryMetadata;
import com.training.helpdesk.user.domain.Role;

public final class QueryBuilder {

    private static final String PLAIN_QUERY_START = "from Ticket t ";

    private static final String COUNT_QUERY_START = "select count(t.id) from Ticket t ";

    private static final String JOIN_FETCH_CLAUSE = " join fetch t.assignee join fetch t.owner "
        + " join fetch t.category join fetch t.approver ";

    private static final String FIND_ALL_EMPLOYEE_CLAUSE = " where owner_id = :id ";

    private static final String FIND_ALL_MANAGER_CLAUSE = " where (owner_id = :id "
        + " or (owner_id in (select id from User where role_id = 0) and t.state = 'NEW') "
        + " or (approver_id = :id and state_id in ('APPROVED', 'DECLINED', 'IN_PROGRESS', 'DONE', 'CANCELED')))";

    private static final String FIND_ALL_ENGINEER_CLAUSE = " where (t.state = 'APPROVED' "
        + " or (assignee_id = :id and state_id in ('IN_PROGRESS', 'DONE')))";

    private QueryBuilder() {};

    public static String buildPlainQuery(Role role, QueryMetadata qm) {
        String roughQuery = prepareRoughQuery(role, qm);

        return String.format(roughQuery, PLAIN_QUERY_START, JOIN_FETCH_CLAUSE)
            + createFilterClause(qm) + createOrderClause(qm);
    }

    public static String buildCountQuery(Role role, QueryMetadata qm) {
        String roughQuery = prepareRoughQuery(role, qm);

        return String.format(roughQuery, COUNT_QUERY_START, "") + createFilterClause(qm);
    }

    private static String prepareRoughQuery(Role role, QueryMetadata queryMetadata) {

        String roughQuery;
        switch (role) {
            case ROLE_EMPLOYEE:
                roughQuery = "%s %s " + FIND_ALL_EMPLOYEE_CLAUSE;
                break;
            case ROLE_MANAGER:
                roughQuery = "%s %s " + FIND_ALL_MANAGER_CLAUSE;
                break;
            default:
                roughQuery = "%s %s " + FIND_ALL_ENGINEER_CLAUSE;
                break;
        }

        return roughQuery;
    }

    private static String createFilterClause(QueryMetadata qm) {
        if (!qm.getSearchParams().isBlank()) {
            String[] searchParams = qm.getSearchParams().split("==");
            String searchColumn = searchParams[0];
            String searchPattern = searchParams[1];

            if ("t.urgency".equals(searchColumn)) {
                final String pattern = searchPattern;
                String urgencies = Stream.of(Urgency.values())
                        .filter(u -> u.toString().toLowerCase().contains(pattern.toLowerCase()))
                        .map(Urgency::ordinal)
                        .map(o -> o.toString())
                        .collect(Collectors.joining(","));

                return " and t.urgency in (" + urgencies + ")";
            }

            return String.format(" and lower(%s) like lower('%%%s%%') ", searchColumn, searchPattern);
        } else {
            return "";
        }
    }

    private static String createOrderClause(QueryMetadata qm) {
        String orderBy = " order by ";
        if ("default".equals(qm.getOrderBy())) {
            orderBy += "t.urgency asc, t.desiredResolutionDate desc";
        } else {
            orderBy += qm.getOrderBy() + " " + qm.getOrder();
        }

        return orderBy;
    }
}
