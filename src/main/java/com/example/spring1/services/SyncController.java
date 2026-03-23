package com.example.spring1.services;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @GetMapping("/{year}")
    public String startSync(@PathVariable int year) {
        syncService.syncFullSeason(year);
        return "Sync completed for year " + year;
    }

    @GetMapping("/laps/{session_key}")
    public String sycLapsOfSession(@PathVariable int session_key){
        syncService.syncLaps(session_key);
        return "Sync completed for key " + session_key;
    }

    @GetMapping("/laps/meeting/{meeting_key}")
    public String syncLapsOfMeeting(@PathVariable short meeting_key){
        syncService.syncLapsByMeeting(meeting_key);
        return "Sync completed for key " + meeting_key;
    }

    @GetMapping("/driver/{meeting_key}")
    public String syncDriversByMeeting(@PathVariable short meeting_key){
        syncService.syncDriversByMeeting(meeting_key);
        return "Sync completed for key " + meeting_key;
    }
}
