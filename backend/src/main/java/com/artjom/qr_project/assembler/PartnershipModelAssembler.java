package com.artjom.qr_project.assembler;

import com.artjom.qr_project.DTO.PartnershipDTO;
import com.artjom.qr_project.controller.PartnershipController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PartnershipModelAssembler implements RepresentationModelAssembler<PartnershipDTO, EntityModel<PartnershipDTO>> {

    @Override
    public EntityModel<PartnershipDTO> toModel(PartnershipDTO partnership) {
        return EntityModel.of(partnership,
                linkTo(methodOn(PartnershipController.class).one(partnership.id())).withSelfRel(),
                linkTo(methodOn(PartnershipController.class).all()).withRel("partnerships"));
    }
}
