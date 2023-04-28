package sv3advproject.erp_project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderType {

    STANDARD(1),PRIORITY(1.3),DISCOUNTED(0.8),TEST_TYPE(1);

    private final double multiplier;

}
