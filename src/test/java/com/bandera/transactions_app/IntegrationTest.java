package com.bandera.transactions_app;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebIntegrationTest
public class IntegrationTest {
	
	private RestTemplate template = new TestRestTemplate();
	
	private static final String TRANSACTION_URI = "http://localhost:8080/transactionservice/transaction/{id}";
	private static final String TYPES_URI = "http://localhost:8080/transactionservice/types/{type}";
	private static final String SUM_URI = "http://localhost:8080/transactionservice/sum/{ancestorId}";
	
	@Test
	public void testPutWithNoBody() {
		String requestBody = "";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, headers);
		
		ResponseEntity<String> response = template.exchange(TRANSACTION_URI, HttpMethod.PUT, requestEntity, String.class, "10");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPutWithNoValues() {
		String requestBody = "{ }";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, headers);
		
		ResponseEntity<String> response = template.exchange(TRANSACTION_URI, HttpMethod.PUT, requestEntity, String.class, "10");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPutWithNoType() {
		String requestBody = "{ \"amount\": 400, \"parent_id\": 1 }";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, headers);
		
		ResponseEntity<String> response = template.exchange(TRANSACTION_URI, HttpMethod.PUT, requestEntity, String.class, "10");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testPutWithOnlyType() {
		String requestBody = "{ \"type\": \"mytype\" }";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestBody, headers);
		
		ResponseEntity<String> response = template.exchange(TRANSACTION_URI, HttpMethod.PUT, requestEntity, String.class, "10");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("{\"status\":\"ok\"}", response.getBody());
	}

	@Test
	public void testPutWithoutParentId() {
		Transaction t = new Transaction();
		t.setId(10);
		t.setType("mytype");
		t.setAmount(400);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Transaction> requestEntity = new HttpEntity<Transaction>(t, headers);
		
		ResponseEntity<String> response = template.exchange(TRANSACTION_URI, HttpMethod.PUT, requestEntity, String.class, t.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("{\"status\":\"ok\"}", response.getBody());
	}

	@Test
	public void testPutWithNotExistingParentId() {
		Transaction t = new Transaction();
		t.setId(10);
		t.setType("mytype");
		t.setAmount(400);
		t.setParentId(new Long(40));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Transaction> requestEntity = new HttpEntity<Transaction>(t, headers);
		
		ResponseEntity<String> response = template.exchange(TRANSACTION_URI, HttpMethod.PUT, requestEntity, String.class, t.getId());
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	public void testPutWithExistingParentId() {
		Transaction t = new Transaction();
		t.setId(10);
		t.setType("mytype");
		t.setAmount(400);
		t.setParentId(new Long(1));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Transaction> requestEntity = new HttpEntity<Transaction>(t, headers);
		
		ResponseEntity<String> response = template.exchange(TRANSACTION_URI, HttpMethod.PUT, requestEntity, String.class, t.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("{\"status\":\"ok\"}", response.getBody());
	}

	@Test
	public void testPutWithChildParentId() {
		Transaction t = new Transaction();
		t.setId(10);
		t.setType("mytype");
		t.setAmount(400);
		t.setParentId(new Long(2));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Transaction> requestEntity = new HttpEntity<Transaction>(t, headers);
		
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response = template.exchange(TRANSACTION_URI, HttpMethod.PUT, requestEntity, Map.class, t.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().containsKey("status"));
		assertEquals("ok", response.getBody().get("status"));
	}

	@Test
	public void testPutWithNegativeAmount() {
		Transaction t = new Transaction();
		t.setId(10);
		t.setType("mytype");
		t.setAmount(-400);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Transaction> requestEntity = new HttpEntity<Transaction>(t, headers);
		
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response = template.exchange(TRANSACTION_URI, HttpMethod.PUT, requestEntity, Map.class, t.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().containsKey("status"));
		assertEquals("ok", response.getBody().get("status"));
	}

	@Test
	public void testPutWith0Amount() {
		Transaction t = new Transaction();
		t.setId(10);
		t.setType("mytype");
		t.setAmount(0);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Transaction> requestEntity = new HttpEntity<Transaction>(t, headers);
		
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response = template.exchange(TRANSACTION_URI, HttpMethod.PUT, requestEntity, Map.class, t.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().containsKey("status"));
		assertEquals("ok", response.getBody().get("status"));
	}

	@Test
	public void testGetWithNotExistingId() {
		ResponseEntity<Transaction> response = template.getForEntity(TRANSACTION_URI, Transaction.class, "4000");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void testGet() {
		Transaction response = template.getForObject(TRANSACTION_URI, Transaction.class, "1");
		assertEquals("deposit", response.getType());
		assertEquals(1000, response.getAmount(), 0);
		assertNull(response.getParentId());
	}
	
	@Test
	public void testFindByTypesWithNotExistingType() {
		@SuppressWarnings("unchecked")
		List<Long> response = template.getForObject(TYPES_URI, List.class, "foo");
		assertEquals(0, response.size());
	}

	@Test
	public void testFindByTypes() {
		@SuppressWarnings("unchecked")
		Set<Long> response = template.getForObject(TYPES_URI, Set.class, "transfer");
		assertEquals(3, response.size());
		assertTrue(response.contains(12));
		assertTrue(response.contains(4));
		assertTrue(response.contains(22));
	}
	
	@Test
	public void testSumDescendantsAmountWithNotExistingId() {
		ResponseEntity<String> response = template.getForEntity(SUM_URI, String.class, "400");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void testSumDescendantsAmountWithLeaf() {
		@SuppressWarnings("unchecked")
		Map<String, Double> response = template.getForObject(SUM_URI, Map.class, "12");
		assertEquals(805, response.get("sum"), 0);
	}

	@Test
	public void testSumDescendantsAmountWithChild() {
		@SuppressWarnings("unchecked")
		Map<String, Double> response = template.getForObject(SUM_URI, Map.class, "2");
		assertEquals(1573, response.get("sum"), 0);
	}

	@Test
	public void testSumDescendantsAmountWithRoot() {
		@SuppressWarnings("unchecked")
		Map<String, Double> response = template.getForObject(SUM_URI, Map.class, "1");
		assertEquals(2573, response.get("sum"), 0);
	}

	@Test
	public void testSumDescendantsAmountWithSingleNode() {
		@SuppressWarnings("unchecked")
		Map<String, Double> response = template.getForObject(SUM_URI, Map.class, "4");
		assertEquals(900, response.get("sum"), 0);
	}
}