package com.example.spring1.clients;

import com.example.spring1.entities.Session;
import com.example.spring1.entities.Stint;
import com.example.spring1.util.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface RaceDataClients {

    interface MeetingClient {
        @GetExchange("/meetings")
        List<MeetingDTO> getMeetings(@RequestParam("year") Integer year);

        @GetExchange("/meetings")
        MeetingDTO getMeetingByKey(@RequestParam("meeting_key") Integer meeting_key);
    }

    interface SessionClient {
        @GetExchange("/sessions")
        List<SessionDTO> getSessions(@RequestParam("year") Integer year);

        @GetExchange("/sessions")
        List<SessionDTO> getSessionsByKey(@RequestParam("meeting_key") Short meeting_key);
    }

    interface SessionResultClient {
        @GetExchange("/session_result")
        List<SessionResultDTO> getSessionResults(@RequestParam("session_key") Short session_key);
    }

    interface LapClient {
        @GetExchange("/laps")
        List<LapDTO> getLapResults(@RequestParam("session_key") Short session_key);

        @GetExchange("/laps")
        List<LapDTO> getLapsByMeetingKey(@RequestParam("meeting_key") Integer meeting_key);
    }

    interface DriverClient {
        @GetExchange("/drivers")
        List<DriverDTO> getDriverResults(@RequestParam("meeting_key") Short meeting_key,
                                         @RequestParam("session_key") Short session_key);
    }

    interface StintClient {
        @GetExchange("/stints")
        List<StintDTO> getStintResults(@RequestParam("session_key") Short session_key);
    }

}
