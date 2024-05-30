package fit.se2.springboot1.controller;

import fit.se2.springboot1.model.Company;
import fit.se2.springboot1.model.Employee;
import fit.se2.springboot1.repository.CompanyRepository;
import fit.se2.springboot1.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @RequestMapping("/list")
    public String getAllCompany(Model model) {
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);
        return "companyList";
    }

    @RequestMapping("/add")
    public String addCompany(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("company", new Company());
        return "companyAdd";
    }

    @RequestMapping("/insert")
    public String insertCompany(Company company) {
        companyRepository.save(company);
        return "redirect:/company/detail/" + company.getId();
    }

    @RequestMapping("/detail/{id}")
    public String getCompanyById(@PathVariable Long id, Model model) {
        Company company = companyRepository.getById(id);
        model.addAttribute("company", company);
        model.addAttribute("employees", company.getEmployees());
        return "companyDetail";
    }

    @RequestMapping("/update/{id}")
    public String updateCompany(@PathVariable Long id, Model model) {
        Company company = companyRepository.getById(id);
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("company", company);
        model.addAttribute("id", id);
        return "companyUpdate";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCompany(@PathVariable("id") Long id, Model model) {
        try {
        companyRepository.deleteById(id);
        return "redirect:/company/list";
        } catch (DataIntegrityViolationException e) {
            // Handle the exception
            model.addAttribute("errorMessage", "Cannot delete company because it has associated employees.");
            return "errorPage"; // Thymeleaf template for displaying error messages
        }
    }
    @RequestMapping(value = "/save")
    public String saveUpdate(Company company) {
        companyRepository.save(company);
        return "redirect:/company/list";
    }
}
