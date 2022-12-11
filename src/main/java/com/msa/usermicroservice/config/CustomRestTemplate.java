package com.msa.usermicroservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msa.usermicroservice.dto.ResponseComDto;
import com.msa.usermicroservice.exception.BadReqException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomRestTemplate {
    private final RestTemplate restTemplate;

    public Object requestGet(String uri, String path, Map<String, Object> params) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(uri)
                .path(path);

        for(String key: params.keySet()) {
            Object value = params.get(key);
            if (value instanceof List) {
                List tmp = (List) value;
                for (Object o : tmp) {
                    builder.queryParam(key, o);
                }
            } else {
                builder.queryParam(key, value);
            }
        }
        String url = builder.build().toString();
        ResponseEntity<ResponseComDto> result = restTemplate.exchange(url, HttpMethod.GET, null, ResponseComDto.class);
        return this.parseJsonObject(result);
    }

    public <T> Object requestPost(String uri, String path, T requestBody,
                                      MultiValueMap<String, String> header) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(uri)
                .path(path);

        if(header == null){
            header = this.getCommonHeader();
        }

        String url = builder.build().toString();
        HttpEntity<?> httpEntity = new HttpEntity<>(requestBody, header);
        ResponseEntity<ResponseComDto> result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ResponseComDto.class);
        return this.parseJsonObject(result);
    }

    private Object parseJsonObject(ResponseEntity<ResponseComDto> result) throws JsonProcessingException {
        HttpStatus status = result.getStatusCode();
        ResponseComDto res = result.getBody();
        if(ObjectUtils.isEmpty(res) || !status.is2xxSuccessful()){
            throw new BadReqException("잘못된 응답입니다.");
        }

        Object obj = res.getResultObj();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(obj);
        if(obj instanceof JSONArray){
            return new JSONObject(jsonStr);
        }
        return new JSONArray(jsonStr);
    }

    private MultiValueMap<String, String> getCommonHeader() throws JsonProcessingException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        return headers;
    }
}
