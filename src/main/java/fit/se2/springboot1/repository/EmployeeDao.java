package fit.se2.springboot1.repository;

import fit.se2.springboot1.model.Company;
import fit.se2.springboot1.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDao {
    @Autowired
    EntityManager em;
    @Autowired
    CompanyRepository companyRepository;

    public Page<Employee> filterAndSortEmployees(Long comId, int gender, int sortMode, Pageable pageable) {
        HibernateCriteriaBuilder cb = em.unwrap(Session.class).getCriteriaBuilder();
        JpaCriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> employee = cq.from(Employee.class);
        Path<Object> sortColumn = employee.get("id");
        if (sortMode == 2 || sortMode == 3) {
            sortColumn = employee.get("name");
        }
        Order sortOrder = cb.desc(sortColumn);
        if (sortMode == 1 || sortMode == 2) {
            sortOrder = cb.asc(sortColumn);
        }
        List<Predicate> predicates = new ArrayList<>();
        Optional<Company> comp = companyRepository.findById(comId);
        if (comp.isPresent()) {
            predicates.add(cb.equal(employee.get("company"), comp.get()));
        }
        if (gender == 1) {
            predicates.add(cb.equal(employee.get("gender"), Employee.Gender.MALE));
        } else if (gender == 2) {
            predicates.add(cb.equal(employee.get("gender"), Employee.Gender.FEMALE));
        }
        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }
        cq.orderBy(sortOrder);

        TypedQuery<Employee> tq = em.createQuery(cq);
        tq.setFirstResult((int) pageable.getOffset());
        tq.setMaxResults(pageable.getPageSize());
        return PageableExecutionUtils.getPage(
                tq.getResultList(),
                pageable,
                () -> em.createQuery(cq.createCountQuery())
                        .getSingleResult()
        );
    }
}
