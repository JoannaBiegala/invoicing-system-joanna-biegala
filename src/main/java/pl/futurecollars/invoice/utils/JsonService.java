package pl.futurecollars.invoice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

  private final ObjectMapper mapper;

  {
    mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public String toJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to convert string to JSON", e);
    }
  }

  public <T> T toObject(String json, Class<T> objectClass) {
    try {
      return mapper.readValue(json, objectClass);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to parse JSON", e);
    }
  }

}
