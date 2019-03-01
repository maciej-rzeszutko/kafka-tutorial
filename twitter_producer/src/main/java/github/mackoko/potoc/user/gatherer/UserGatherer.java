package github.mackoko.potoc.user.gatherer;


import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserGatherer {

	private final Logger logger = LoggerFactory.getLogger(UserGatherer.class.getName());
	private final ObjectMapper objectMapper = new ObjectMapper();

	public List<String> getRandomUsers(int count) {
		ResponseEntity<String> response = new RestTemplate().exchange(
				buildQuery(count),
				HttpMethod.GET,
				new HttpEntity<>(buildHeaders()),
				String.class);

		Optional<JsonNode> nodeOpt;
		try {
			nodeOpt = Optional.ofNullable(objectMapper.readTree(response.getBody()));
		} catch (IOException e) {
			logger.error("Encountered error: ", e);
			nodeOpt = Optional.empty();
		}

		return nodeOpt
				.map(jsonNode -> jsonNode.findValues("results"))
				.orElseGet(Collections::emptyList)
				.stream()
				.map(JsonNode::toString)
				.collect(toList());
	}

	private HttpHeaders buildHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private String buildQuery(int count) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("https://randomuser.me/api/")
				.queryParam("results", count);

		return builder.toUriString() + "&noinfo";
	}
}