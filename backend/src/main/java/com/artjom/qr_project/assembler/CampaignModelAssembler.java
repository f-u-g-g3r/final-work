package com.artjom.qr_project.assembler;

import com.artjom.qr_project.DTO.CampaignDTO;
import com.artjom.qr_project.controller.CampaignController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CampaignModelAssembler implements RepresentationModelAssembler<CampaignDTO, EntityModel<CampaignDTO>> {

    @Override
    public EntityModel<CampaignDTO> toModel(CampaignDTO campaign) {
        return EntityModel.of(campaign,
                linkTo(methodOn(CampaignController.class).one(campaign.id())).withSelfRel(),
                linkTo(methodOn(CampaignController.class).all()).withRel("campaigns"));
    }
}
