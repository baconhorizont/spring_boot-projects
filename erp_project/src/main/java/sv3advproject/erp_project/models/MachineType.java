package sv3advproject.erp_project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MachineType {

    MILL(25_000),LATHE(18_000),GRINDER(15_000),TEST_TYPE(1);

    private final int overheadCharge;

}
