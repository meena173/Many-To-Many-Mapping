package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController 
{
	
	@Autowired
	ProjectRepo er;
	@Autowired
	EmployeeRepo pr;
	
	@GetMapping
	public Page<Employee>  getAll(@PageableDefault(sort="first" , direction=Sort.Direction.ASC)Pageable page)
	{
		return pr.findAll(page);
	}
	
	
	@GetMapping("/{id}")

	public Employee getId(@PathVariable int id)
	{
		
		return pr.findById(id).orElse(null);
		
	}
	
	
	
	@PostMapping
	public Employee saveAll(@RequestBody Employee p)
	{
		return pr.save(p);
	}
	
	@PutMapping("/{id}")
       public Employee putId(@PathVariable int id, @RequestBody Employee p)
       {
		Employee pe = pr.findById(id).orElseThrow();
		pe.setFirst(p.getFirst());
		pe.setLast(p.getLast());
		pe.setProject(p.getProject());
		
		
		return pr.save(pe);
       }
	
	
	
	@DeleteMapping("/{id}")
	public void deleteId(@PathVariable int id)
	{
		pr.deleteById(id);
	}
	
	

}