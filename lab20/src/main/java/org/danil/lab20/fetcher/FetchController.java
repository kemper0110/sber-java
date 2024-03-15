package org.danil.lab20.fetcher;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/fetch")
public class FetchController {
    private final RestTemplate restTemplate;
    public FetchController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    @GetMapping
    ResponseEntity<String> fetch(@RequestParam String url) {
        final var re = restTemplate.getForEntity(url, String.class);
        // Копируем, потому что исходный re.getHeaders() иммутабельный
        final var headers = new HttpHeaders();
        headers.putAll(re.getHeaders());
        // Заголовок Set-Cookie от сторонних сайтов конфликтует с выставляемым сервером заголовком Set-Cookie
        headers.remove("Set-Cookie");
        return new ResponseEntity<>(re.getBody(), headers, re.getStatusCode());
    }
    @PostMapping("/gc")
    void runGc() {
        System.gc();
    }
}
