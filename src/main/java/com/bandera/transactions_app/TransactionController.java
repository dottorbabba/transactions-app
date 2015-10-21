package com.bandera.transactions_app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactionservice")
public class TransactionController {
	
	@Autowired
	private TransactionRepository repository;
	
	@RequestMapping(value="/transaction/{id}", method=RequestMethod.PUT)
	public Map<String, String> create(@PathVariable long id, @Valid @RequestBody Transaction transaction) {
		if(transaction == null) {
			throw new UnprocessableEntityException("Invalid transaction");
		}
		transaction.setId(id);
		String path = String.valueOf(transaction.getId()) + "/";
		if(transaction.getParentId() != null) {
			Transaction parent = repository.findOne(transaction.getParentId());
			if(parent == null) {
				throw new UnprocessableEntityException("parentId does not exists");
			}
			path = parent.getPath() + path;
		}
		transaction.setPath(path);
		repository.save(transaction);
		
		HashMap<String, String> rv = new HashMap<String, String>();
		rv.put("status", "ok");
		return rv;
	}
	
	@RequestMapping(value="/transaction/{id}", method=RequestMethod.GET)
	public Transaction get(@PathVariable long id) {
		Transaction t = repository.findOne(id);
		if(t == null) throw new ResourceNotFoundException();
		return t;
	}
	
	@RequestMapping(value="/types/{type}", method=RequestMethod.GET)
	public List<Long> findByType(@PathVariable String type) {
		return repository.findByType(type);
	}

	@RequestMapping(value="/sum/{id}", method=RequestMethod.GET)
	public Map<String, Double> sumDescendantsAmount(@PathVariable long id) {
		Double sum = repository.sumDescendantsAmount(id);
		if(sum == null) throw new ResourceNotFoundException();
		HashMap<String, Double> rv = new HashMap<String, Double>();
		rv.put("sum", sum);
		return rv;
		
	}
}
