package ru.rybinskov.gb.springshop.shop.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rybinskov.gb.springshop.shop.domain.Bucket;
import ru.rybinskov.gb.springshop.shop.dto.BucketDto;

import java.util.List;

@Mapper
public interface BucketMapper {
    BucketMapper MAPPER = Mappers.getMapper(BucketMapper.class);

    Bucket toBucket(BucketDto dto);

    @InheritInverseConfiguration
    BucketDto fromBucket(Bucket bucket);

    List<Bucket> toBucketList(List<BucketDto> bucketDtos);

    List<BucketDto> fromBucketList(List<Bucket> buckets);

}
