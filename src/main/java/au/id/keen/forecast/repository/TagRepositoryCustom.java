package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.Tag;
import au.id.keen.forecast.entity.param.TagDaoParam;

import java.util.List;

public interface TagRepositoryCustom {

    List<Tag> find(TagDaoParam pParam);

}
