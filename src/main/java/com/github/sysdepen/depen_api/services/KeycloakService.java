package com.github.sysdepen.depen_api.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
public class KeycloakService {

    private final String serverUrl = "https://backend.local.sysdepen.com.br:8443/";
    private final String realm = "projeto-mensal";
    private final String adminUsername = "admin";
    private final String adminPassword = "root";
    private final String clientId = "backend-depen";
    private final String clientSecret = "RJPm5lBOmA2q86G4eliBzRiv1MgBAsbj";

    private static final Logger log = LoggerFactory.getLogger(KeycloakService.class);


    public String criarUsuarioNoKeycloak(Usuario request) {
        log.info("Iniciando criação usuario key", request.getDocument());
        String token = getAdminToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> user = new HashMap<>();
        user.put("enabled", true);
        user.put("username", request.getDocument());
        user.put("firstName", request.getName());

        Map<String, String> credential = new HashMap<>();
        credential.put("type", "password");
//        credential.put("value", request.getPassword());
        credential.put("temporary", "false");

        log.info("usuario no keycloak:", user);
        user.put("credentials", List.of(credential));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(user, headers);

        try {
            log.info("Enviando requisição para criar usuário...");
            ResponseEntity<Void> response = new RestTemplate().postForEntity(
                    serverUrl + "/admin/realms/" + realm + "/users", entity, Void.class);
            log.info("Resposta ao criar usuário: {}", response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful()) {
                String userId = buscarUserIdPorUsername(request.getDocument(), token);
                atribuirRoleAoUsuario(userId, request.getRole(), token);
                return userId;
            } else {
                throw new RuntimeException("Erro ao criar usuário no Keycloak: " + response.getStatusCode());
            }
        } catch (RestClientException ex) {
            log.error("Erro na requisição de criação de usuário", ex);
            throw new RuntimeException("Erro ao criar usuário no Keycloak", ex);
        }
    }

    public void atualizarUsuarioNoKeycloak(String userId, Usuario request) {
        log.info("Atualizando usuário ID: {}", userId);

        String token = getAdminToken();
        String url = serverUrl + "/admin/realms/" + realm + "/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("firstName", request.getName());
        userUpdate.put("username", request.getDocument());

        Map<String, String> credential = new HashMap<>();
        credential.put("type", "password");
//        credential.put("value", request.getPassword());
        credential.put("temporary", "false");

        userUpdate.put("credentials", List.of(credential));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userUpdate, headers);

        try {
            new RestTemplate().exchange(url, HttpMethod.PUT, entity, Void.class);
            atribuirRoleAoUsuario(userId, request.getRole(), token);
            log.info("Usuário atualizado com sucesso: {}", userId);
        } catch (RestClientException ex) {
            log.error("Erro ao atualizar usuário", ex);
            throw new RuntimeException("Erro ao atualizar usuário no Keycloak", ex);
        }
    }



    private String getAdminToken() {
        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("username", adminUsername);
        form.add("password", adminPassword);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        try {
            ResponseEntity<Map> response = new RestTemplate().postForEntity(tokenUrl, request, Map.class);
            return (String) response.getBody().get("access_token");
        } catch (RestClientException ex) {
            throw new RuntimeException("Erro ao obter token do Keycloak", ex);
        }
    }

    private String buscarUserIdPorUsername(String username, String token) {
        String url = serverUrl + "/admin/realms/" + realm + "/users?username=" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<UserRepresentation[]> response = new RestTemplate().exchange(
                url, HttpMethod.GET, entity, UserRepresentation[].class);

        if (response.getBody() != null && response.getBody().length > 0) {
            return response.getBody()[0].getId();
        }
        throw new RuntimeException("Usuário criado, mas não encontrado via busca.");
    }

    public void atribuirRoleAoUsuario(String userId, String roleName, String token) {
        log.info("Atribuindo role '{}' ao usuário ID: {}", roleName, userId);

        String keycloakRole = "";


        if(roleName.equals("GESTOR")) {
            keycloakRole = "admin";
        }else if (roleName.equals("FUNCIONARIO")) {
            keycloakRole = "user-default";
        }

        String roleUrl = serverUrl + "/admin/realms/" + realm + "/roles/" + keycloakRole;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> roleRequest = new HttpEntity<>(headers);
        ResponseEntity<Map> roleResponse = new RestTemplate().exchange(roleUrl, HttpMethod.GET, roleRequest, Map.class);

        Map<String, Object> roleRepresentation = roleResponse.getBody();

        if (roleRepresentation == null) {
            throw new RuntimeException("Role '" + roleName + "' não encontrada.");
        }

        String assignUrl = serverUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Map<String, Object>>> assignRequest = new HttpEntity<>(List.of(roleRepresentation), headers);

        new RestTemplate().postForEntity(assignUrl, assignRequest, Void.class);
        log.info("Role '{}' atribuída com sucesso ao usuário {}", keycloakRole, userId);
    }





    static class UserRepresentation {
        private String id;
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }
}