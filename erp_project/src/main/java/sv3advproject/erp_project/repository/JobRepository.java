package sv3advproject.erp_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv3advproject.erp_project.models.Job;
import sv3advproject.erp_project.models.JobStatus;
import sv3advproject.erp_project.models.Machine;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job,Long> {

    @Query("select j from Job j where (:customer is null or j.customer like %:customer%) and (:jobStatus is null or j.status = :jobStatus)")
    List<Job> findByCustomerAndStatus(@Param("customer") Optional<String> customer,@Param("jobStatus") Optional<JobStatus> jobStatus);
    @Query("select distinct m from Machine m left join fetch m.runningJob j where j.id = :jobId")
    List<Machine> findJobByIdMadeByEmployee(@Param("jobId") long jobId);
    @Query("select distinct j from Job j left join fetch j.machinedOn m where m.id = :machineId")
    List<Job> findJobByMachineId(@Param("machineId") long machineId);
    @Query("select sum(j.cost) from Job j where (:startDate is null or j.deadline >= :startDate) and (:endDate is null or j.deadline <= :endDate)")
    Optional<Double> getAllCostBetweenDate(@Param("startDate") Optional<LocalDate> startDate,@Param("endDate") Optional<LocalDate> endDate);
}
