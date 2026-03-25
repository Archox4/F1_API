package com.example.spring1.services;

import com.example.spring1.repositories.MeetingRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

    private final SyncService syncService;
    private final SyncProcessor syncProcessor;

    public SyncController(SyncService syncService, SyncProcessor syncProcessor) {
        this.syncService = syncService;
        this.syncProcessor = syncProcessor;
    }

    @GetMapping("/{year}")
    public String syncYear(@PathVariable int year) {
        //syncService.syncFullSeason(year);
        syncProcessor.syncFullSeasonExp(year);
        return "Sync completed for year " + year;
    }
//
//    @GetMapping("/meetingsOnly/{year}")
//    public String syncMeetingsByYear(@PathVariable short year) {
//        syncService.syncMeetingsByYear(year);
//        return "Sync completed for meetings of " + year;
//    }
//
//    @GetMapping("/meetingData/{meeting_key}")
//    public String syncMeeting(@PathVariable short meeting_key) {
//        syncService.saveDataForMeeting(meeting_key);
//        return "Sync completed for meeting " + meeting_key;
//    }
//
//    @GetMapping("/laps/meeting/{meeting_key}")
//    public String syncLapsOfMeeting(@PathVariable short meeting_key){
//        syncService.syncLapsByMeeting(meeting_key);
//        return "Sync completed for key " + meeting_key;
//    }

    @GetMapping("/stints/year/{year}")
    public String syncStintsByYear(@PathVariable Short year){
        syncService.syncOnlyStintData(year);
        return "Sync completed for stints " + year;
    }

}
