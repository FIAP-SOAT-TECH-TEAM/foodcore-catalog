package com.soat.fiap.food.core.catalog.infrastructure.out.persistence.postgres.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.StockDTO;
import com.soat.fiap.food.core.catalog.infrastructure.out.persistence.postgres.entity.StockEntity;
import com.soat.fiap.food.core.shared.infrastructure.common.mapper.CycleAvoidingMappingContext;
import com.soat.fiap.food.core.shared.infrastructure.common.mapper.DoIgnore;

/**
 * Mapper que converte entre a entidade de domínio Stock e a entidade JPA
 * StockEntity
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockEntityMapper {

	/**
	 * Converte uma entidade de domínio para uma entidade JPA
	 *
	 * @param dto
	 *            DTO de estoque
	 * @return Entidade JPA
	 */
	@Mapping(target = "auditInfo", expression = "java(com.soat.fiap.food.core.shared.infrastructure.common.mapper.AuditInfoMapper.buildAuditInfo(dto.createdAt(), dto.updatedAt()))")
	StockEntity toEntity(StockDTO dto, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

	/**
	 * Converte uma entidade JPA para um DTO.
	 *
	 * @param entity
	 *            Entidade JPA
	 * @return DTO correspondente
	 */
	@Mapping(source = "auditInfo.createdAt", target = "createdAt")
	@Mapping(source = "auditInfo.updatedAt", target = "updatedAt")
	StockDTO toDTO(StockEntity entity);

	@DoIgnore
	default StockEntity toEntity(StockDTO dto) {
		return toEntity(dto, new CycleAvoidingMappingContext());
	}
}
