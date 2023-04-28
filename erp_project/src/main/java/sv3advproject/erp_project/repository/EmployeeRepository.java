package sv3advproject.erp_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv3advproject.erp_project.models.Employee;
import sv3advproject.erp_project.models.EmployeeQualification;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("select e from Employee e where (:nameFragment is null or e.name like %:nameFragment%) and (:qualification is null or e.qualification = :qualification) ")
    List<Employee> findByNameAndQualification(@Param("nameFragment") Optional<String> nameFragment,@Param("qualification") Optional<EmployeeQualification> qualification);
    @Query("select distinct e from Employee e left join fetch e.canWorkOn where e.id = :employeeId")
    Optional<Employee> findByIdWithMachines(@Param("employeeId") long employeeId);
}
