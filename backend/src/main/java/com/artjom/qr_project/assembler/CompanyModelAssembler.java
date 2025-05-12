package com.artjom.qr_project.assembler;

import com.artjom.qr_project.DTO.CompanyDTO;
import com.artjom.qr_project.controller.CompanyController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CompanyModelAssembler implements RepresentationModelAssembler<CompanyDTO, EntityModel<CompanyDTO>> {

    @Override
    public EntityModel<CompanyDTO> toModel(CompanyDTO company) {
        return EntityModel.of(company,
                linkTo(methodOn(CompanyController.class).one(company.id())).withSelfRel(),
                linkTo(methodOn(CompanyController.class).all()).withRel("companies"));
    }
}
