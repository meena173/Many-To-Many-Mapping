package com.example.demo;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<Project> getAll(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable page) {
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
    public Project put(@PathVariable int id, @PathVariable int eid)
    {
        Project p = pr.findById(id).orElseThrow();
        Employee e = er.findById(eid).orElseThrow();

        Set<Employee> set = new HashSet<>(p.getEmployee());
        set.add(e);
        p.setEmployee(set);

        return pr.save(p);
    }

    @DeleteMapping("/{id}")
    public void deleteId(@PathVariable int id) 
    {
        pr.deleteById(id);
    }

    // New method for pagination
    @GetMapping("/page/{pageNo}/{pageSize}")
    public List<Project> getPaginated(@PathVariable int pageNo, @PathVariable int pageSize)
    {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Project> pagedResult = pr.findAll(pageable);

        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<Project>();
    }

    // New method for sorting
    @GetMapping("/sort")
    public List<Project> getAllSorted(@RequestParam String field, @RequestParam String direction) 
    {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return pr.findAll(Sort.by(sortDirection, field));
    }

    // New method for pagination and sorting combined
    @GetMapping("/page/{pageNo}/{pageSize}/sort")
    public List<Project> getPaginatedAndSorted(@PathVariable int pageNo, @PathVariable int pageSize,
                                               @RequestParam String sortField, @RequestParam String sortDir)
    {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                    Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Project> pagedResult = pr.findAll(pageable);

        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<Project>();
    }
}
