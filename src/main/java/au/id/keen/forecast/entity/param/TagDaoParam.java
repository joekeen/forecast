package au.id.keen.forecast.entity.param;

import lombok.Data;

import java.util.Set;

@Data
public class TagDaoParam extends AbstractBaseDaoParam {

    private Set<String> names;
}
