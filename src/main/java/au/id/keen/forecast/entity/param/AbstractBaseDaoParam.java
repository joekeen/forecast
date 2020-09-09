package au.id.keen.forecast.entity.param;

import lombok.Data;

@Data
public abstract class AbstractBaseDaoParam {

    private String userEmail;

    private Long offset;
    private Long limit;
}
