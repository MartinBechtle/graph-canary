package com.martinbechtle.graphcanary.rest;

import com.martinbechtle.graphcanary.canary.CanaryData;
import com.martinbechtle.graphcanary.canary.CanaryService;
import com.martinbechtle.graphcanary.graph.Graph;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes the {@link Graph} as json
 *
 * @author Martin Bechtle
 */
@RestController
@RequestMapping("/graph")
@CrossOrigin
public class CanaryController {

   private final CanaryService canaryService;

    public CanaryController(CanaryService canaryService) {

        this.canaryService = canaryService;
    }
    
    @GetMapping
    public CanaryData getCanaryData() {
        
        return canaryService.getCanaryData();
    }
}
