package com.example.demo;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController 
{
	
	@Autowired
	ProjectRepo pr;
	@Autowired
	EmployeeRepo er;
	
	@GetMapping
	public Page<Project>  getAll(@PageableDefault(sort="name" , direction=Sort.Direction.ASC)Pageable page)
	{
		return pr.findAll(page);
	}
	
	
	@GetMapping("/{id}")

	public Project getId(@PathVariable int id)
	{
		
		return pr.findById(id).orElse(null);
		
	}	
	
	@PostMapping
	public Project saveAll(@RequestBody Project p)
	{
		return pr.save(p);
	}
	
	@PutMapping("/{id}")
       public Project putId(@PathVariable int id, @RequestBody Project p)
       {
			Project pe = pr.findById(id).orElseThrow();
			pe.setStart(p.getStart());
			pe.setEnd(p.getEnd()); 
			pe.setEmployee(p.getEmployee());
			pe.setName(p.getName());
			
		
		return pr.save(pe);
       }
	
	@PutMapping("/{id}/{eid}")
	public Project put(@PathVariable int id,@PathVariable int eid)
	{
		Project p = pr.findById(id).orElseThrow();
		Employee e = er.findById(eid).orElseThrow();
		
		Set<Employee> set = new HashSet<>();
		
		set=p.getEmployee();
		
		set.add(e);
		
		p.setEmployee(set);
		
		return pr.save(p);
		
		
	}
	
	@DeleteMapping("/{id}")
	public void deleteId(@PathVariable int id)
	{
		pr.deleteById(id);
	}
	
}