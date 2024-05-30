package fit.se2.springboot1.repository;

import fit.se2.springboot1.model.Company;
import fit.se2.springboot1.model.Employee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByCompany(Company company, Sort sort);
}
