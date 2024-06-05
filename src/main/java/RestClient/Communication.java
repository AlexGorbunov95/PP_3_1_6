package RestClient;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class Communication {

    private final String URL = "http://94.198.50.185:7081/api/users";

    public List<String> cookies = new ArrayList<>();

    public StringBuilder result = new StringBuilder();

    private final RestTemplate restTemplate;


    public Communication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getAllUsers() {
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        cookies = responseEntity.getHeaders().get("Set-Cookie");
        return responseEntity.getBody();
    }

    public void saveUser(User user) {
        Long id = user.getId();
        HttpHeaders headers = new HttpHeaders();
        if (id == 3) {
            headers.put(HttpHeaders.COOKIE, cookies);
            HttpEntity<User> entity = new HttpEntity<>(user, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, entity, String.class);
            result.append(responseEntity.getBody());
        }
    }

    public void updateUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);
        restTemplate.put(URL, entity);
        result.append(responseEntity.getBody());
    }

    public void deleteUser(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<User> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, entity, String.class);
        result.append(responseEntity.getBody());
        System.out.println(result + " = " + result.length());
    }
}
