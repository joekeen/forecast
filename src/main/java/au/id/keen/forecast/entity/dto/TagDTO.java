package au.id.keen.forecast.entity.dto;

import au.id.keen.forecast.entity.Tag;
import lombok.Data;

@Data
public class TagDTO implements Comparable<TagDTO> {

    private Integer id;
    private String name;

    public TagDTO(Tag pTag) {
        this.id = pTag.getId();
        this.name = pTag.getName();
    }

    @Override
    public int compareTo(TagDTO tagDTO) {
        return name.compareToIgnoreCase(tagDTO.getName());
    }
}