package sv3advproject.erp_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv3advproject.erp_project.models.Machine;
import sv3advproject.erp_project.models.MachineType;

import java.util.List;
import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine,Long> {

    @Query("select m from Machine m where :machineType is null or m.type = :machineType")
    List<Machine> findByType(@Param("machineType") Optional<MachineType> machineType);
}
