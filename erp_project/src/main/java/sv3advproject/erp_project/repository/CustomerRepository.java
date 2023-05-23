package sv3advproject.erp_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv3advproject.erp_project.models.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("select distinct c from Customer c left join fetch c.jobs where c.id = :customerId")
    Optional<Customer> findByIdWithJobs(@Param("customerId") long customerId);

    @Query("select distinct c from Customer c left join fetch c.jobs where c.name like %:customerName%")
    Optional<Customer> findByNameWithJobs(@Param("customerName") String customerName);
}
