package fit.se2.springboot1.controller;


import fit.se2.springboot1.model.Company;
import fit.se2.springboot1.model.Employee;
import fit.se2.springboot1.repository.CompanyRepository;
import fit.se2.springboot1.repository.EmployeeDao;
import fit.se2.springboot1.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmployeeDao employeeDao;

    @RequestMapping(value = "/list")
    public String getAllEmployee(
            @RequestParam(value = "company", required = false, defaultValue = "0") Long comId,
            @RequestParam(value = "gender", required = false, defaultValue = "0") int gender,
            @RequestParam(value = "sort", required = false, defaultValue = "0") int sortMode,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            Model model) {
        final int pageSize = 5;
        model.addAttribute("comId", comId);
        model.addAttribute("gender", gender);
        model.addAttribute("sortMode", sortMode);
        Page<Employee> employees = employeeDao.filterAndSortEmployees(
                comId, gender, sortMode,
                PageRequest.of(page, pageSize)
        );
        model.addAttribute("page", page);
        model.addAttribute("pages", employees.getTotalPages());
        model.addAttribute("employees", employees.get());
        model.addAttribute("employees", employees);
        model.addAttribute("companies", companyRepository.findAll());
        return "employeeList";

    }

    @RequestMapping(value = "/detail/{id}")
    public String getEmployeeById(@PathVariable(value = "id") Long id, Model model) {
        Employee employee = employeeRepository.getById(id);
        model.addAttribute("employee", employee);
        return "employeeDetail";
    }

    @RequestMapping(value = "/update/{id}")
    public String updateEmployee(@PathVariable(value = "id") Long id, Model model) {
        Employee employee = employeeRepository.getById(id);
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);
        model.addAttribute(employee);
        return "employeeUpdate";

    }

    @RequestMapping(value = "/save")
    public String saveUpdate(Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/employee/list";
    }


    @RequestMapping(value = "/add")
    public String addEmployee(Model model) {
        Employee employee = new Employee();
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);
        model.addAttribute("employee", employee);
        return "employeeAdd";
    }

    @RequestMapping(value = "/insert")
    public String insertEmployee(Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/employee/detail/" + employee.getId();
    }

    @RequestMapping(value = "/delete/{id}")
    public String deleteEmployee(@PathVariable(value = "id") Long id) {
        if (employeeRepository.findById(id).isPresent()) {
            Employee employee = employeeRepository.findById(id).get();
            employeeRepository.delete(employee);
        }
        return "redirect:/employee/list";
    }

}
