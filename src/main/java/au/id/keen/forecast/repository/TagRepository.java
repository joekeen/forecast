package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.Tag;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends TagRepositoryCustom, CrudRepository<Tag, Integer>, QuerydslPredicateExecutor<Tag> {
}
