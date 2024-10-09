package com.anji.customer.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
// import org.springframework.web.reactive.function.client.WebClient;
// import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;

import lombok.Data;
import lombok.Getter;
import reactor.core.publisher.Mono;


@Service
public class ExportHandlerService {

    
    @Autowired
    @Qualifier("externalRestTemplate") 
    private RestTemplate restTemplate;

    // private final WebClient webClient;

    // @Autowired
    // public ExportHandlerService(WebClient.Builder webClientBuilder) {
    //     this.webClient = webClientBuilder.baseUrl("http://localhost:2000").build();
    // }
    
    public void handleExportAndCallback(String redirectUrl){

      
        
        Thread thread = new Thread(new HttpRequestRunnable(redirectUrl));
        thread.start();  

    } 
       private class HttpRequestRunnable implements Runnable {

        private String redirectUrl;
        
        public HttpRequestRunnable(String redirectUrl){
            this.redirectUrl = redirectUrl;
        }

        @Override
        public void run() { 
            // System.out.println("Export Request Processing Started: ");
            // String url = "http://127.0.0.1:2000/api/v1/bulk-export-callback-webhook"; 
            // String response = restTemplate.getForObject(url, String.class);
            // System.out.println("Export Request Response: " + response);
 
            System.out.println("Export Request Processing Started: ");
            ExportInfo exportInfo = new ExportInfo();
            exportInfo.setAccountId("1222");
            exportInfo.setFilePath("/temp/1.mark");
            exportInfo.setDateStarted(new Date());

            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) { 
                e.printStackTrace();
            }
            
           
            exportInfo.setDateEnded(new Date());
            // Object obj = webClient.post()
            //     .uri("/api/v1/bulk-export-callback-webhook")
            //     .bodyValue(exportInfo)
            //     .retrieve()
            //     .bodyToMono(String.class);

            // System.out.println("Export Request Processing completed: "+obj.toString());
            // webClient.get().uri("/api/v1/bulk-export-callback-webhook").httpRequest(a ->{
            //     System.out.println( "Response Received : "+a);
            // });

            // Mono<String> response = webClient.get().uri("/bulk-export-callback-webhook").retrieve().bodyToMono(String.class);
            // response.doOnEach(a -> {
            //     System.out.println( "Response Received : "+a);
            // });
             


            // Set the URL and request body
        //String redirectUrl = "http://localhost:2000/api/v1/bulk-export-callback-webhook";
         
        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity with headers and body
        HttpEntity<ExportInfo> requestEntity = new HttpEntity<>(exportInfo, headers);

        // Make the POST request
        System.out.println("redirectUrl::"+redirectUrl);
        ResponseEntity<String> response = restTemplate.exchange(redirectUrl, HttpMethod.POST, requestEntity, String.class);

        // Handle the response
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            System.out.println("Export Request Processing completed: "+responseBody.toString());
        } else {
            System.out.println("Export Request Processing Failed: ");
        }
        }
    }

    @Data
    private class ExportInfo{
        String accountId;
        String filePath;
        Date dateStarted;
        Date dateEnded;


    }
    
}
