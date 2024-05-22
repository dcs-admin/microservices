package com.anji.customer.controller;
import com.anji.customer.service.ExportHandlerService;
import lombok.extern.slf4j.Slf4j; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ExportController {

    @Autowired
    private ExportHandlerService exportHandlerService;

    @GetMapping("/bulk-export")
    public String exportData(@RequestParam("redirect_url") String redirectUrl) { 
        exportHandlerService.handleExportAndCallback(redirectUrl); 
        log.info("ExportController::exportData Rquested:: redirectUrl:"+redirectUrl);
        return "HTTP request started in a separate thread";
    } 
 
}
