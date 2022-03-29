package com.training.helpdesk.history.repository;

import java.util.List;

import com.training.helpdesk.history.domain.History;

public interface HistoryRepository {

    List<History> findByTicketId(Long id);

    Long save(History history);
}
