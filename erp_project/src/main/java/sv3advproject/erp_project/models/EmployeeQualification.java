package sv3advproject.erp_project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmployeeQualification {

    MILLING("MILL"),TURNING("LATHE"),GRINDING("GRINDER"),TECHNOLOGIST("APPLICATION"),PROGRAMMER("CAM"),DESIGNER("CAD"),TEST_QUAL("TEST_QUAL");

    private final String requires;
}
