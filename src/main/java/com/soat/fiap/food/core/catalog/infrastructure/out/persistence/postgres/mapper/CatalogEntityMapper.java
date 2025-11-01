package com.soat.fiap.food.core.catalog.infrastructure.out.persistence.postgres.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.CatalogDTO;
import com.soat.fiap.food.core.catalog.infrastructure.out.persistence.postgres.entity.CatalogEntity;
import com.soat.fiap.food.core.catalog.infrastructure.out.persistence.postgres.mapper.shared.ImageURLMapper;
import com.soat.fiap.food.core.shared.infrastructure.common.mapper.AuditInfoMapper;
import com.soat.fiap.food.core.shared.infrastructure.common.mapper.CycleAvoidingMappingContext;
import com.soat.fiap.food.core.shared.infrastructure.common.mapper.DoIgnore;

/**
 * Mapper que converte entre a entidade JPA CatalogEntity e o DTO CatalogDTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ImageURLMapper.class,
		CategoryEntityMapper.class, AuditInfoMapper.class})
public interface CatalogEntityMapper {

	/**
	 * Converte uma entidade JPA para um DTO.
	 *
	 * @param entity
	 *            Entidade JPA
	 * @return DTO correspondente
	 */
	@Mapping(source = "auditInfo.createdAt", target = "createdAt")
	@Mapping(source = "auditInfo.updatedAt", target = "updatedAt")
	CatalogDTO toDTO(CatalogEntity entity);

	/**
	 * Converte uma lista de entidades JPA para uma lista de DTOs.
	 *
	 * @param entities
	 *            Lista de entidades JPA
	 * @return Lista de DTOs
	 */
	@Mapping(source = "auditInfo.createdAt", target = "createdAt")
	@Mapping(source = "auditInfo.updatedAt", target = "updatedAt")
	List<CatalogDTO> toDTOList(List<CatalogEntity> entities);

	/**
	 * Converte um DTO para uma entidade JPA.
	 *
	 * @param dto
	 *            DTO
	 * @return Entidade JPA correspondente
	 */
	@Mapping(target = "auditInfo", expression = "java(com.soat.fiap.food.core.shared.infrastructure.common.mapper.AuditInfoMapper.buildAuditInfo(dto.createdAt(), dto.updatedAt()))")
	CatalogEntity toEntity(CatalogDTO dto, @Context CycleAvoidingMappingContext context);

	@DoIgnore
	default CatalogEntity toEntity(CatalogDTO dto) {
		return toEntity(dto, new CycleAvoidingMappingContext());
	}
}
