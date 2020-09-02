package au.id.keen.forecast.service;

import au.id.keen.forecast.entity.dto.TagDTO;
import au.id.keen.forecast.entity.param.TagDaoParam;

import java.util.List;

public interface TagService {
    List<TagDTO> findDTO(TagDaoParam pParam);
}