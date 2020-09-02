package au.id.keen.forecast.service;

import au.id.keen.forecast.entity.dto.TagDTO;
import au.id.keen.forecast.entity.param.TagDaoParam;
import au.id.keen.forecast.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagDTO> findDTO(TagDaoParam pParam) {
        return tagRepository.find(pParam).stream().map(TagDTO::new).collect(Collectors.toList());
    }

}