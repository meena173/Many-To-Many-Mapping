package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController 
{
    @Autowired
    ProjectRepo er;

    @Autowired
    EmployeeRepo pr;

    @GetMapping
    public Page<Employee> getAll(@PageableDefault(sort = "first", direction = Sort.Direction.ASC) Pageable page) 
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

   
    @GetMapping("/page/{pageNo}/{pageSize}")
    public List<Employee> getPaginated(@PathVariable int pageNo, @PathVariable int pageSize)
    {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Employee> pagedResult = pr.findAll(pageable);

        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<Employee>();
    }

  
    @GetMapping("/sort")
    public List<Employee> getAllSorted(@RequestParam String field, @RequestParam String direction) 
    {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return pr.findAll(Sort.by(sortDirection, field));
    }

    
    @GetMapping("/page/{pageNo}/{pageSize}/sort")
    public List<Employee> getPaginatedAndSorted(@PathVariable int pageNo, @PathVariable int pageSize,
                                                @RequestParam String sortField, @RequestParam String sortDir) 
    {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                    Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Employee> pagedResult = pr.findAll(pageable);

        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<Employee>();
    }
}
