package au.id.keen.forecast.repository;

import au.id.keen.forecast.entity.QTag;
import au.id.keen.forecast.entity.Tag;
import au.id.keen.forecast.entity.param.TagDaoParam;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class TagRepositoryCustomImpl extends QuerydslRepositorySupport implements TagRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public TagRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Tag.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Tag> find(TagDaoParam pParam) {
        QTag tag = QTag.tag;

        JPAQuery<Tag> query = jpaQueryFactory.selectFrom(tag);

        if (StringUtils.isNotBlank(pParam.getUserEmail())) {
            query.where(tag.user.email.eq(pParam.getUserEmail()));
        }
        if (CollectionUtils.isNotEmpty(pParam.getNames())) {
            query.where(tag.name.in(pParam.getNames()));
        }

        return query.fetch();

    }
}
