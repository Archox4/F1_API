package com.example.spring1.services;

import com.example.spring1.clients.RaceDataClients;
import com.example.spring1.entities.*;
import com.example.spring1.repositories.*;
import com.example.spring1.util.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.client.HttpClientErrorException;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SyncService {
    private final RaceDataClients.MeetingClient meetingClient;
    private final RaceDataClients.SessionClient sessionClient;
    private final RaceDataClients.LapClient lapClient;
    private final RaceDataClients.DriverClient driverClient;
    private final RaceDataClients.SessionResultClient sessionResultClient;
    private final RaceDataClients.StintClient stintClient;


    private final MeetingRepository meetingRepository;
    private final SessionRepository sessionRepository;
    private final LapRepository lapRepository;
    private final DriverRepository driverRepository;
    private final SessionResultRepository sessionResultRepository;
    private final StintRepository stintRepository;

    public SyncService(RaceDataClients.MeetingClient meetingClient,
                       RaceDataClients.SessionClient sessionClient,
                       RaceDataClients.LapClient lapClient,
                       RaceDataClients.DriverClient driverClient,
                       RaceDataClients.SessionResultClient sessionResultClient,
                       RaceDataClients.StintClient stintClient,
                       MeetingRepository meetingRepository,
                       SessionRepository sessionRepository,
                       LapRepository lapRepository,
                       DriverRepository driverRepository,
                       StintRepository stintRepository,
                       SessionResultRepository sessionResultRepository) {
        this.meetingClient = meetingClient;
        this.sessionClient = sessionClient;
        this.lapClient = lapClient;
        this.driverClient = driverClient;
        this.sessionResultClient = sessionResultClient;
        this.stintClient = stintClient;

        this.meetingRepository = meetingRepository;
        this.sessionRepository = sessionRepository;
        this.lapRepository = lapRepository;
        this.driverRepository = driverRepository;
        this.sessionResultRepository = sessionResultRepository;
        this.stintRepository = stintRepository;
    }



    //***************
    // FULL/PART SYNC
    //***************

    @Transactional
    public void saveDataForMeeting(short meeting_key){
        try {
            Meeting meeting = meetingRepository.findById(meeting_key);
            syncSessionsOfMeeting(meeting);
            List<Session> sessions = sessionRepository.findByMeetingKey(meeting);
            System.out.println("size: " + sessions.size());
            saveDataForSessions(sessions);
        } catch (Exception e) {
            log.error("e: ", e);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveDataForSessions(List<Session> sessions) {
        for (Session session : sessions){
            syncFullSessionData(session);
        }
    }

    @Transactional
    public void syncFullSessionData(Session session){
        int maxRetries = 3;
        int attempt = 0;
        boolean success = false;

        log.info("==========================================");
        log.info("Sync for session: " + session.getSessionName());
        log.info("==========================================");

        while (attempt < maxRetries && !success) {
            try {
                log.info("syncing laps...");
                syncLapsBySessionKey(session);
                log.info("syncing results");
                syncResultsOfSession(session);
                log.info("syncing stints");
                syncStintsBySession(session);
                log.info("syncing drivers");
                syncDriversBySession(session);
                Thread.sleep(3000);

                success = true;
            } catch (HttpClientErrorException.TooManyRequests e) {
                attempt++;
                log.warn("Rate limit hit on attempt {}. Waiting 10s...", attempt);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                log.error("Permanent error on session {}: {}", session.getId(), e.getMessage());
                break;
            }
        }
    }


    public void syncOnlyStintData(short year){


        List<Meeting> meetings = meetingRepository.findByYear(year);

        try {
            for(Meeting meeting : meetings){
                int maxRetries = 3;
                int attempt = 0;
                boolean success = false;
                List<Session> sessions = sessionRepository.findByMeetingKey(meeting);
                for(Session session : sessions){
                    while (attempt < maxRetries && !success) {
                        try {
                            log.info("Syncing stint for session {}", session.getId());
                            syncStintsBySession(session);
                            Thread.sleep(1000);
                            success = true;
                        } catch (HttpClientErrorException.TooManyRequests e) {
                            attempt++;
                            log.warn("Rate limit hit on attempt {}. Waiting 10s...", attempt);
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                        } catch (Exception e) {
                            log.error("Permanent error on session {}", e.getMessage());
                            break;
                        }
                    }
                }
                Thread.sleep(3000);
            }
            log.info("Sync completed");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    //***************
    // LAPS
    //***************

    @Transactional
    public void syncLapsByMeeting(short meeting_key){
        Meeting meeting = meetingRepository.findById(meeting_key);
        if (meeting == null) return;
        saveLapsOfMeeting(meeting);
    }
    // saves laps to db based on session key
    private void syncLapsBySessionKey(Session session){
        //List<LapDTO> lapDTOS = lapClient.getLapsByMeetingKey(meeting.getId().intValue());
        List<LapDTO> lapDTOS = lapClient.getLapResults(session.getId());
        if(lapDTOS != null && !lapDTOS.isEmpty()){
            List<Lap> laps = lapDTOS.stream()
                    .map(this::mapToLapEntity)
                    .toList();
            int i = 0;
            for(Lap lap : laps){
                if(lapRepository.findByDriverNumberAndSessionKeyAndLapNumber(lap.getDriverNumber(),
                        session, lap.getLapNumber()).isEmpty()){
                    lapRepository.save(lap);
                    i++;
                }
            }
            System.out.println("Synced " + i + " laps.");
        }
    }
    private void saveLapsOfMeeting(Meeting meeting){
        List<Session> sessions = sessionRepository.findByMeetingKey(meeting);
        try {
            for (Session session : sessions){
                syncLapsBySessionKey(session);
                Thread.sleep(4000);
                System.out.println("Synced laps for session key " + session.getId());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            System.err.println("Syncing laps error: " + e);
        }
    }

    //***************
    // MEETINGS
    //***************

    // returns meetings list based on year
    public List<Meeting> getMeetings(int year){
        List<MeetingDTO> meetingDtos = meetingClient.getMeetings(year);
        List<Meeting> meetings = meetingDtos.stream()
                .filter(dto -> !dto.getMeetingName().equalsIgnoreCase("Pre-Season Testing"))
                .map(this::mapToMeetingEntity)
                .toList();

        System.out.println("Passed " + meetings.size() + " meetings.");

        return meetings;
    }

    @Transactional
    public void syncMeetingsByYear(short year){
        try {
            meetingRepository.saveAll(getMeetings(year));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //***************
    // SESSION RESULTS
    //***************
    private void syncResultsOfSession(Session session){
        List<SessionResultDTO> resultDTOS = sessionResultClient.getSessionResults(session.getId());
        if (resultDTOS != null && !resultDTOS.isEmpty()){
            List<SessionResult> results = resultDTOS.stream()
                    .map(this::mapToSessionResultEntity)
                    .toList();
            int i = 0;
            for(SessionResult result : results){
                 if(sessionResultRepository.findBySessionKeyIdAndMeetingKeyIdAndDriverNumber(session.getId(), session.getMeetingKey().getId(), result.getDriverNumber()).isEmpty()){
                    sessionResultRepository.save(result);
                    i++;
                 }
            }
            log.info("Added " + i + " new sessions");
        }
    }
    @Transactional
    public void test22(){
        Session s = sessionRepository.findAll().getFirst();
        syncResultsOfSession(s);
    }
    //***************
    // DRIVERS
    //***************
    @Transactional
    public void syncDriversBySession(Session session){
        List<DriverDTO> driverDTOS = driverClient.getDriverResults(session.getMeetingKey().getId(), session.getId());
        List<Driver> drivers = driverDTOS.stream()
                .map(this::mapToDriverEntity)
                .toList();
        int i = 0;
        for(Driver driver : drivers){
            if (driverRepository.findBySessionKeyAndDriverNumber(session, driver.getDriverNumber()).isEmpty()){
                driverRepository.save(driver);
                i++;
            }
        }
        log.info("Added " + i + " new drivers");
    }

    //***************
    // SESSIONS
    //***************
    public List<Session> getSessions(int year){
        List<SessionDTO> sessionDtos = sessionClient.getSessions(year);
        List<Session> sessions = sessionDtos.stream()
                .filter(this::isSessionAccepted)
                .map(this::mapToSessionEntity)
                .toList();

        System.out.println("Passed " + sessions.size() + " sessions.");

        return sessions;
    }

    // returns sessions list based on year
    public List<Session> getSessionsOfMeeting(Meeting meeting){
        return sessionRepository.findByMeetingKey(meeting);
    }
    @Transactional
    public void syncSessionsOfMeeting(Meeting meeting){
        List<SessionDTO> sessionDTOS = sessionClient.getSessionsByKey(meeting.getId());
        List<Session> sessions = sessionDTOS.stream()
                .filter(this::isSessionAccepted)
                .map(this::mapToSessionEntity)
                .toList();
        sessionRepository.saveAll(sessions);
    }

    //***************
    // STINTS
    //***************
    public void syncStintsBySession(Session session){
        List<StintDTO> stintDTOS = stintClient.getStintResults(session.getId());
        List<Stint> stints = stintDTOS.stream()
                .map(this::mapToStintEntity)
                .toList();
        int i = 0;
        for(Stint stint : stints){
            if(stintRepository.findByLapStartAndDriverNumberAndSessionKeyId(stint.getLapStart(),
                    stint.getDriverNumber(), session.getId()).isEmpty()){
                stintRepository.save(stint);
                i++;
            }
        }
        log.info("Added " + i + " new stints");
    }

    //***************
    // OTHER - mapping, etc.
    //***************
    private Meeting mapToMeetingEntity(MeetingDTO dto) {
        Meeting entity = new Meeting();
        entity.setId(dto.getId());
        entity.setMeetingName(dto.getMeetingName());
        entity.setMeetingOfficialName(dto.getMeetingOfficialName());
        entity.setLocation(dto.getLocation());
        entity.setCountryCode(dto.getCountryCode());
        entity.setCountryFlag(dto.getCountryFlag());
        entity.setCircuitShortName(dto.getCircuitShortName());
        entity.setDateStart(dto.getDateStart());
        entity.setDateEnd(dto.getDateEnd());
        entity.setYear((short) dto.getYear());
        return entity;
    }

    private Session mapToSessionEntity(SessionDTO dto) {
        Session entity = new Session();
        entity.setId(dto.getId());
        entity.setSessionType(dto.getSessionType());
        entity.setSessionName(dto.getSessionName());
        entity.setDateStart(dto.getDateStart());
        entity.setDateEnd(dto.getDateEnd());
        entity.setCircuitShortName(dto.getCircuitShortName());
        meetingRepository.findById(dto.getMeetingKey())
                .ifPresent(entity::setMeetingKey);

        return entity;
    }

    private Lap mapToLapEntity(LapDTO dto) {
        Lap entity = new Lap();
        entity.setDriverNumber(dto.getDriverNumber());
        entity.setLapNumber(dto.getLapNumber());
        entity.setDurationSector1(dto.getDurationSector1());
        entity.setDurationSector2(dto.getDurationSector2());
        entity.setDurationSector3(dto.getDurationSector3());
        entity.setIsPitOutLap(dto.getIsPitOutLap());
        entity.setLapDuration(dto.getLapDuration());

        entity.setMeetingKey(meetingRepository.getReferenceById(dto.getMeetingKey()));
        entity.setSessionKey(sessionRepository.getReferenceById(dto.getSessionKey()));

        return entity;
    }

    private Driver mapToDriverEntity(DriverDTO dto) {
        Driver entity = new Driver();
        entity.setBroadcastName(dto.getBroadcastName());
        entity.setDriverNumber(dto.getDriverNumber());
        entity.setFullName(dto.getFullName());
        entity.setHeadshotUrl(dto.getHeadshotUrl());
        entity.setNameAcronym(dto.getNameAcronym());
        entity.setTeamColour(dto.getTeamColour());
        entity.setTeamName(dto.getTeamName());

        entity.setMeetingKey(meetingRepository.getReferenceById(dto.getMeetingKey()));
        entity.setSessionKey(sessionRepository.getReferenceById(dto.getSessionKey()));

        return entity;
    }

    private Stint mapToStintEntity(StintDTO dto) {
        Stint entity = new Stint();
        entity.setCompound(dto.getCompound());
        entity.setLapEnd(dto.getLapEnd());
        entity.setLapStart(dto.getLapStart());
        entity.setStintNumber(dto.getStintNumber());
        entity.setTyreAgeAtStart(dto.getTyreAgeAtStart());
        entity.setDriverNumber(dto.getDriverNumber());

        entity.setMeetingKey(meetingRepository.getReferenceById(dto.getMeetingKey()));
        entity.setSessionKey(sessionRepository.getReferenceById(dto.getSessionKey()));

        return entity;
    }

    private SessionResult mapToSessionResultEntity(SessionResultDTO dto) {
        SessionResult entity = new SessionResult();
        entity.setDriverNumber(dto.getDriverNumber());
        entity.setNumberOfLaps(dto.getNumberOfLaps());
        entity.setPosition(dto.getPosition());
        entity.setDnf(dto.getDnf());
        entity.setDsq(dto.getDsq());
        entity.setDns(dto.getDns());

        JsonNode durationNode = dto.getDuration();
        if (durationNode == null || durationNode.isNull()){
            entity.setDuration(null);
        } else if (durationNode.isArray()) {
            Double[] temp_arr = new Double[durationNode.size()];
            for (int i = 0; i < durationNode.size(); i++) {
                JsonNode element = durationNode.get(i);
                temp_arr[i] = (element.isNull()) ? null : roundToThreeDecimals(element.asDouble());
            }
            entity.setDuration(temp_arr);
        } else {
            entity.setDuration(new Double[]{ durationNode.asDouble() });
        }

        entity.setMeetingKey(meetingRepository.getReferenceById(dto.getMeetingKey()));
        entity.setSessionKey(sessionRepository.getReferenceById(dto.getSessionKey()));

        return entity;
    }

    private boolean isSessionAccepted(SessionDTO dto) {
        String type = dto.getSessionType();
        if (type == null) return false;

        return type.equalsIgnoreCase("Race") || type.equalsIgnoreCase("Qualifying") || type.equalsIgnoreCase("Sprint");
    }

    private Double roundToThreeDecimals(Double value) {
        if (value == null) return null;
        return Math.round(value * 1000.0) / 1000.0;
    }

}
