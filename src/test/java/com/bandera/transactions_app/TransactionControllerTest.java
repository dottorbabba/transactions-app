package com.bandera.transactions_app;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class TransactionControllerTest {
	@Mock
	private TransactionRepository repository;
	
	@InjectMocks
	@Resource
	private TransactionController controller;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected=UnprocessableEntityException.class)
	public void testCreateWithNullTransaction() {
		controller.create(20, null);
	}

	@Test
	public void testCreateWithNullParentId() {
		Transaction t = new Transaction();
		t.setId(1);
		Map<String, String> response = controller.create(t.getId(), t);
		assertTrue(response.containsKey("status"));
		assertEquals("ok", response.get("status"));
		assertEquals("1/", t.getPath());
	}

	@Test(expected=UnprocessableEntityException.class)
	public void testCreateWithNotExistingParent() {
		Transaction t = new Transaction();
		t.setId(1);
		t.setParentId(new Long(200));
		Map<String, String> response = controller.create(t.getId(), t);
		assertTrue(response.containsKey("status"));
		assertEquals("ok", response.get("status"));
		assertEquals("1/", t.getPath());
	}

	@Test
	public void testCreateWithExistingParent() {		
		Transaction parent = new Transaction();
		parent.setId(2);
		parent.setPath("2/");
		when(repository.findOne(parent.getId())).thenReturn(parent);

		Transaction t = new Transaction();
		t.setId(1);
		t.setParentId(parent.getId());
		
		Map<String, String> response = controller.create(t.getId(), t);
		assertTrue(response.containsKey("status"));
		assertEquals("ok", response.get("status"));
		assertEquals("2/1/", t.getPath());
	}
	
	@Test(expected=ResourceNotFoundException.class)
	public void testGetWithNotExistingId() {
		controller.get(0);
	}

	@Test
	public void testGet() {
		Transaction t1 = new Transaction();
		t1.setId(1);
		when(repository.findOne(t1.getId())).thenReturn(t1);
		
		Transaction t2 = controller.get(t1.getId());
		assertEquals(t1.getId(), t2.getId());
	}
	
	@Test
	public void testFindByType() {
		Transaction t = new Transaction();
		t.setId(2);
		t.setType("deposit");
		
		when(repository.findByType(t.getType())).thenReturn(Arrays.asList(t.getId()));
		
		List<Long> response = controller.findByType(t.getType());
		assertEquals(1, response.size());
		assertEquals((Long) t.getId(), response.get(0));
	}
	
	@Test
	public void testFindByNotExistingType() {
		when(repository.findByType("withdrawal")).thenReturn(Collections.<Long> emptyList());
		
		List<Long> rv = controller.findByType("withdrawal");
		assertEquals(0, rv.size());
	}
	
	@Test(expected=ResourceNotFoundException.class)
	public void testSumDescendantsAmountOfNotExistingId() {
		when(repository.sumDescendantsAmount(0)).thenReturn(null);
		controller.sumDescendantsAmount(0);
	}

	@Test
	public void testSumDescendantsAmount() {
		Double sum = new Double(45);
		when(repository.sumDescendantsAmount(1)).thenReturn(sum);
		Map<String, Double> response = controller.sumDescendantsAmount(1);
		assertTrue(response.containsKey("sum"));
		assertEquals(sum, response.get("sum"));
	}
}