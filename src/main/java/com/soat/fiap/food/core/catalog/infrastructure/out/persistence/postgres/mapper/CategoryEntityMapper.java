package com.soat.fiap.food.core.catalog.infrastructure.out.persistence.postgres.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.CategoryDTO;
import com.soat.fiap.food.core.catalog.infrastructure.out.persistence.postgres.entity.CategoryEntity;
import com.soat.fiap.food.core.catalog.infrastructure.out.persistence.postgres.mapper.shared.ImageURLMapper;
import com.soat.fiap.food.core.shared.infrastructure.common.mapper.CycleAvoidingMappingContext;
import com.soat.fiap.food.core.shared.infrastructure.common.mapper.DoIgnore;

/**
 * Mapper que converte entre a entidade de domínio Category e a entidade JPA
 * CategoryEntity
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ProductEntityMapper.class,
		ImageURLMapper.class})
public interface CategoryEntityMapper {

	/**
	 * Converte uma entidade JPA para um DTO.
	 *
	 * @param entity
	 *            Entidade JPA
	 * @return DTO correspondente
	 */
	@Mapping(target = "imageUrl", source = "imageUrl", qualifiedByName = "mapImageUrlToString")
	@Mapping(source = "auditInfo.createdAt", target = "createdAt")
	@Mapping(source = "auditInfo.updatedAt", target = "updatedAt")
	CategoryDTO toDTO(CategoryEntity entity);

	/**
	 * Converte uma entidade de domínio para uma entidade JPA
	 *
	 * @param dto
	 *            DTO da categoria
	 * @return Entidade JPA
	 */
	@Mapping(target = "imageUrl", source = "imageUrl", qualifiedByName = "mapStringToImageUrl")
	@Mapping(target = "catalog", ignore = true)
	@Mapping(target = "auditInfo", expression = "java(com.soat.fiap.food.core.shared.infrastructure.common.mapper.AuditInfoMapper.buildAuditInfo(dto.createdAt(), dto.updatedAt()))")
	CategoryEntity toEntity(CategoryDTO dto, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

	@DoIgnore @Mapping(target = "imageUrl", source = "imageUrl", qualifiedByName = "mapStringToImageUrl")
	@Mapping(target = "catalog", ignore = true)
	default CategoryEntity toEntity(CategoryDTO dto) {
		return toEntity(dto, new CycleAvoidingMappingContext());
	}
}
