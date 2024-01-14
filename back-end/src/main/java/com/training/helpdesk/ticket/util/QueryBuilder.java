package com.training.helpdesk.ticket.util;

import java.util.List;
import java.util.stream.Collectors;

import com.training.helpdesk.ticket.domain.Urgency;
import com.training.helpdesk.ticket.repository.QueryMetadata;
import com.training.helpdesk.user.domain.Role;

public final class QueryBuilder {

    private static final String PLAIN_QUERY_SELECT =
            "from Ticket t join fetch t.owner join fetch t.category ";

    private static final String COUNT_QUERY_SELECT =
            "select count(t.id) from Ticket t join t.owner ";

    private static final String FIND_ALL_EMPLOYEE_CLAUSE = " where t.owner.id = :id ";

    private static final String FIND_ALL_MANAGER_CLAUSE =
            " where (t.owner.id = :id  or (t.owner.role = 0 and t.state = 'NEW')  or (t.approver.id"
                    + " = :id and t.state in ('APPROVED', 'DECLINED', 'IN_PROGRESS', 'DONE',"
                    + " 'CANCELED')))";

    private static final String FIND_MY_MANAGER_CLAUSE =
            " where (t.owner.id = :id or (t.approver.id = :id and t.state = 'APPROVED'))";

    private static final String FIND_ALL_ENGINEER_CLAUSE =
            " where (t.state = 'APPROVED' "
                    + " or (t.assignee.id = :id and t.state in ('IN_PROGRESS', 'DONE')))";

    private static final String FIND_MY_ENGINEER_CLAUSE = " where t.assignee.id = :id ";

    private static final String TICKET_ID_CLAUSE = " and t.id = :ticketId ";

    private QueryBuilder() {}

    public static String buildCheckAccessQuery(Role role, QueryMetadata qm) {
        return PLAIN_QUERY_SELECT + createWhereClause(role, qm) + TICKET_ID_CLAUSE;
    }

    public static String buildPlainQuery(Role role, QueryMetadata qm) {
        return PLAIN_QUERY_SELECT
                + createWhereClause(role, qm)
                + createFilterClause(qm)
                + createOrderByClause(qm);
    }

    public static String buildCountQuery(Role role, QueryMetadata qm) {
        return COUNT_QUERY_SELECT + createWhereClause(role, qm) + createFilterClause(qm);
    }

    private static String createWhereClause(Role role, QueryMetadata qm) {

        String whereClause;
        switch (role) {
            case ROLE_EMPLOYEE:
                whereClause = FIND_ALL_EMPLOYEE_CLAUSE;
                break;
            case ROLE_MANAGER:
                whereClause = qm.isMyFilter() ? FIND_MY_MANAGER_CLAUSE : FIND_ALL_MANAGER_CLAUSE;
                break;
            default:
                whereClause = qm.isMyFilter() ? FIND_MY_ENGINEER_CLAUSE : FIND_ALL_ENGINEER_CLAUSE;
                break;
        }

        return whereClause;
    }

    private static String createFilterClause(QueryMetadata qm) {
        if (!qm.getSearchParams().isBlank()) {
            String[] searchParams = qm.getSearchParams().split("==");
            String searchColumn = searchParams[0];
            String searchString = searchParams[1];

            if ("t.urgency".equals(searchColumn)) {
                List<Integer> ordinalsOfSubstring = Urgency.ofSubstring(searchString);
                String urgencies =
                        ordinalsOfSubstring.stream()
                                .map(o -> o.toString())
                                .collect(Collectors.joining(","));

                return " and t.urgency in (" + urgencies + ")";
            }

            String escapedSearchPattern =
                    searchString.replaceAll("([%_])", "\\\\$1").replace("'", "''");
            return String.format(
                    " and lower(str(%s)) like lower('%%%s%%') ",
                    searchColumn, escapedSearchPattern);
        } else {
            return "";
        }
    }

    private static String createOrderByClause(QueryMetadata qm) {
        String orderByClause = " order by ";
        if ("default".equals(qm.getOrderBy())) {
            orderByClause += "t.urgency asc, t.desiredResolutionDate desc, t.id desc";
        } else if ("t.id".equals(qm.getOrderBy())) {
            orderByClause += qm.getOrderBy() + " " + qm.getOrder();
        } else {
            orderByClause += qm.getOrderBy() + " " + qm.getOrder() + ", t.id desc";
        }

        return orderByClause;
    }
}
