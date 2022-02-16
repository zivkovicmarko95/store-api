package com.store.storeanalyticsapi.controllers;

import java.util.List;

import com.store.storeanalyticsapi.mappers.AnalyticsMapper;
import com.store.storeanalyticsapi.services.AnalyticsService;
import com.store.storeanalyticsapi.transferobjects.AnalyticsTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private AnalyticsService analyticsService;

    public AnalyticsController(final AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }    
    
    @GetMapping
    public ResponseEntity<List<AnalyticsTO>> analyticsGet() {

        return new ResponseEntity<>(
                AnalyticsMapper.mapReposToAnalyticsTOs(
                    this.analyticsService.findAll()
                ),
                HttpStatus.OK
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AnalyticsTO> analyticsAnalyticsIdGet(@PathVariable final String id) {

        return new ResponseEntity<>(
                AnalyticsMapper.mapRepoToAnalyticsTO(
                    this.analyticsService.findById(id)
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> analyticsAnalyticsIdDelete(@PathVariable final String id) {

        this.analyticsService.removeById(id);
        
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

}
