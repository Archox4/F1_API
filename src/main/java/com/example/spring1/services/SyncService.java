package com.example.spring1.services;

import com.example.spring1.clients.RaceDataClients;
import com.example.spring1.entities.Driver;
import com.example.spring1.entities.Lap;
import com.example.spring1.repositories.DriverRepository;
import com.example.spring1.repositories.LapRepository;
import com.example.spring1.repositories.MeetingRepository;
import com.example.spring1.repositories.SessionRepository;
import com.example.spring1.entities.Meeting;
import com.example.spring1.entities.Session;
import com.example.spring1.util.DriverDTO;
import com.example.spring1.util.LapDTO;
import com.example.spring1.util.MeetingDTO;
import com.example.spring1.util.SessionDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyncService {
    private final RaceDataClients.MeetingClient meetingClient;
    private final RaceDataClients.SessionClient sessionClient;
    private final RaceDataClients.LapClient lapClient;
    private final RaceDataClients.DriverClient driverClient;
    private final MeetingRepository meetingRepository;
    private final SessionRepository sessionRepository;
    private final LapRepository lapRepository;
    private final DriverRepository driverRepository;

    public SyncService(RaceDataClients.MeetingClient meetingClient,
                       RaceDataClients.SessionClient sessionClient,
                       RaceDataClients.LapClient lapClient,
                       RaceDataClients.DriverClient driverClient,
                       MeetingRepository meetingRepository,
                       SessionRepository sessionRepository,
                       LapRepository lapRepository,
                       DriverRepository driverRepository) {
        this.meetingClient = meetingClient;
        this.sessionClient = sessionClient;
        this.lapClient = lapClient;
        this.driverClient = driverClient;

        this.meetingRepository = meetingRepository;
        this.sessionRepository = sessionRepository;
        this.lapRepository = lapRepository;
        this.driverRepository = driverRepository;
    }

    @Transactional
    public void syncFullSeason(int year) {
        meetingRepository.saveAll(getMeetings(year));
        List<Session> sessions = getSessions(year);
        sessionRepository.saveAll(sessions);
    }
    @Transactional
    public void syncLaps(int sessionKey){
        syncLapsBySessionKey(sessionKey);

    }

    @Transactional
    public void syncLapsByMeeting(short meeting_key){
        Meeting meeting = meetingRepository.findById(meeting_key);
        if (meeting == null) return;
        getLapsOfMeeting(meeting);
    }

    @Transactional
    public void syncDriversByMeeting(short meeting_key){
        List<Session> sessions = getSessionsOfMeeting(meetingRepository.findById(meeting_key));
        for (Session session : sessions){
            try {
                syncDriversBySession(session);
                Thread.sleep(2000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    // adds laps of passed meeting (all sessions)
    private void getLapsOfMeeting(Meeting meeting){
        List<Session> sessions = sessionRepository.findByMeetingKey(meeting);

        try {
            for (Session session : sessions){
                syncLapsBySessionKey(session.getId());
                Thread.sleep(4000);
                System.out.println("Synced laps for session key " + session.getId());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            System.err.println("Syncing laps error: " + e);
        }

        System.out.println("Passed " + sessions.size() + " sessions.");
    }

    // saves laps to db based on session key
    private void syncLapsBySessionKey(int sessionKey){
        //List<LapDTO> lapDTOS = lapClient.getLapsByMeetingKey(meeting.getId().intValue());
        List<LapDTO> lapDTOS = lapClient.getLapResults(sessionKey);
        if(lapDTOS != null && !lapDTOS.isEmpty()){
            List<Lap> laps = lapDTOS.stream()
                    .map(this::mapToLapEntity)
                    .toList();
            lapRepository.saveAll(laps);
            System.out.println("Synced " + laps.size() + " laps.");
        }
    }

    private void syncDriversBySession(Session session){
        List<DriverDTO> driverDTOS = driverClient.getDriverResults(session.getMeetingKey().getId(), session.getId());
        List<Driver> drivers = driverDTOS.stream()
                .map(this::mapToDriverEntity)
                .toList();
        driverRepository.saveAll(drivers);
        System.out.println("Synced " + drivers.size() + " drivers.");
    }


    // returns meetings list based on year
    private List<Meeting> getMeetings(int year){
        List<MeetingDTO> meetingDtos = meetingClient.getMeetings(year);
        List<Meeting> meetings = meetingDtos.stream()
                .filter(dto -> !dto.getMeetingName().equalsIgnoreCase("Pre-Season Testing"))
                .map(this::mapToMeetingEntity)
                .toList();

        System.out.println("Passed " + meetings.size() + " meetings.");

        return meetings;
    }

    // returns sessions list based on year
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


    // mapping from DTO
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
        meetingRepository.findById(dto.getMeetingKey())
                .ifPresent(entity::setMeetingKey);
        sessionRepository.findById(dto.getSessionKey())
                .ifPresent((entity::setSessionKey));

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
        meetingRepository.findById(dto.getMeetingKey())
                .ifPresent(entity::setMeetingKey);
        sessionRepository.findById(dto.getSessionKey())
                .ifPresent((entity::setSessionKey));

        return entity;
    }

    private boolean isSessionAccepted(SessionDTO dto) {
        String type = dto.getSessionType();
        if (type == null) return false;

        return type.equalsIgnoreCase("Race") || type.equalsIgnoreCase("Qualifying") || type.equalsIgnoreCase("Sprint");
    }

}
