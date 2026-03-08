package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.SessionRequest;
import com.wtmsbackend.dto.response.SessionResponse;
import org.springframework.data.domain.Page;

public interface SessionService {
    Page<SessionResponse> getAllSessions(int page, int size);
    SessionResponse getSessionById(Integer id);
    SessionResponse createSession(SessionRequest request);
    SessionResponse updateSession(Integer id, SessionRequest request);
    void deleteSession(Integer id);
}