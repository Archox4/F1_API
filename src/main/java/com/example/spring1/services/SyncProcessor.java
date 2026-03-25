package com.example.spring1.services;

import com.example.spring1.entities.Meeting;
import com.example.spring1.entities.Session;
import com.example.spring1.repositories.MeetingRepository;
import com.example.spring1.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SyncProcessor {

    final SessionRepository sessionRepository;
    final MeetingRepository meetingRepository;
    final SyncService syncService;

    public SyncProcessor(SessionRepository sessionRepository, MeetingRepository meetingRepository, SyncService syncService) {
        this.sessionRepository = sessionRepository;
        this.meetingRepository = meetingRepository;
        this.syncService = syncService;
    }

    public void syncFullSeasonExp(int year) {
        meetingRepository.saveAll(syncService.getMeetings(year));
        List<Session> sessions = syncService.getSessions(year);
        sessionRepository.saveAll(sessions);

        for (Session session : sessions) {
            try {
                syncService.syncFullSessionData(session);
            } catch (Exception e) {
                log.error("Error on session " + session.getId(), e);
            }
        }
    }

}
