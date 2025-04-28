package com.artjom.qr_project.service;

import com.artjom.qr_project.DTO.FilterRequest;
import com.artjom.qr_project.DTO.UserDTO;
import com.artjom.qr_project.DTO.mapper.UserMapper;
import com.artjom.qr_project.assembler.UserModelAssembler;
import com.artjom.qr_project.controller.UserController;
import com.artjom.qr_project.enums.Role;
import com.artjom.qr_project.exception.UserNotFoundException;
import com.artjom.qr_project.model.Company;
import com.artjom.qr_project.model.User;
import com.artjom.qr_project.repository.CompanyRepository;
import com.artjom.qr_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserModelAssembler userModelAssembler;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public CollectionModel<EntityModel<UserDTO>> all() {
        List<EntityModel<UserDTO>> users = repository.findAll().stream()
                .map(UserMapper::toDTO)
                .map(userModelAssembler::toModel)
                .toList();

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    public EntityModel<UserDTO> one(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userModelAssembler.toModel(UserMapper.toDTO(user));

    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    public ResponseEntity<?> activateUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(true);
        user.setRole(Role.EMPLOYEE);
        repository.save(user);
        return ResponseEntity.ok(userModelAssembler.toModel(UserMapper.toDTO(user)));
    }

    public ResponseEntity<?> deactivateUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(false);
        user.setRole(Role.DISABLED_USER);
        repository.save(user);
        return ResponseEntity.ok(userModelAssembler.toModel(UserMapper.toDTO(user)));
    }

    public Page<UserDTO> findWithFilters(FilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(
                filterRequest.getPagingOptions().page,
                filterRequest.getPagingOptions().pageSize,
                filterRequest.getSortOptions().direction == 0 ? Sort.by(filterRequest.getSortOptions().sortBy).ascending()
                        : Sort.by(filterRequest.getSortOptions().sortBy).descending());

        if (filterRequest.getFilterOptions() != null) {
            for (FilterRequest.FilterOptions filterOption : filterRequest.getFilterOptions()) {
                String filterBy = filterOption.filterBy;
                String filterValue = filterOption.filterValue;
                List<String> filterByValues = filterOption.filterValues;

                if ("isActive".equals(filterBy) && ("1".equals(filterValue) || "0".equals(filterValue))) {
                    return userRepository.findAllByIsActive("1".equals(filterValue), pageable).map(UserMapper::toDTO);
                } else if ("role".equals(filterBy) && filterByValues != null) {
                    List<Role> filterByRoles = filterByValues.stream()
                            .map(Role::valueOf)
                            .toList();
                    return userRepository.findAllByRoleIn(filterByRoles, pageable).map(UserMapper::toDTO);
                } else if ("company".equals(filterBy) && filterValue != null) {
                    Company company = new Company();
                    company.setName(filterValue);
                    return userRepository.findAllByCompanyId(company.getId(), pageable).map(UserMapper::toDTO);
                } else if ("generalSearch".equals(filterBy) && filterValue != null) {
                    return userRepository.findAllByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrUuidContainsIgnoreCase(
                            filterValue, filterValue, filterValue, pageable).map(UserMapper::toDTO);
                }
            }
        }
        return userRepository.findAll(pageable).map(UserMapper::toDTO);
    }

    public ResponseEntity<?> addUserToCompany(Long userId, Long companyId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new UserNotFoundException(companyId));

        user.setCompany(company);
        user.setRole(Role.EMPLOYEE);
        user.setActive(true);
        repository.save(user);
        return ResponseEntity.ok(userModelAssembler.toModel(UserMapper.toDTO(user)));
    }

    public ResponseEntity<UserDTO> getUserDataMin(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    public Page<UserDTO> getCompanyUsers(Long companyId, FilterRequest filterRequest) {
                    Pageable pageable = PageRequest.of(
                            filterRequest.getPagingOptions().page,
                            filterRequest.getPagingOptions().pageSize,
                            filterRequest.getSortOptions().direction == 0 ? Sort.by(filterRequest.getSortOptions().sortBy).ascending()
                                    : Sort.by(filterRequest.getSortOptions().sortBy).descending());

                    if (filterRequest.getFilterOptions() != null) {
                        for (FilterRequest.FilterOptions filterOption : filterRequest.getFilterOptions()) {
                            String filterBy = filterOption.filterBy;
                            String filterValue = filterOption.filterValue;
                            List<String> filterByValues = filterOption.filterValues;

                            if ("isActive".equals(filterBy) && ("1".equals(filterValue) || "0".equals(filterValue))) {
                                return repository.findAllByCompanyIdAndIsActive(companyId, "1".equals(filterValue), pageable)
                                        .map(UserMapper::toDTO);
                            } else if ("role".equals(filterBy) && filterByValues != null) {
                                List<Role> filterByRoles = filterByValues.stream()
                                        .map(Role::valueOf)
                                        .toList();
                                return repository.findAllByCompanyIdAndRoleIn(companyId, filterByRoles, pageable)
                                        .map(UserMapper::toDTO);
                            } else if ("generalSearch".equals(filterBy) && filterValue != null) {
                                return repository.findAllByCompanyIdAndNameContainingIgnoreCaseOrCompanyIdAndEmailContainingIgnoreCaseOrCompanyIdAndUuidContainsIgnoreCase(
                                        companyId, filterValue, companyId, filterValue, companyId, filterValue, pageable)
                                        .map(UserMapper::toDTO);
                            }
                        }
                    }

                    Page<User> users = repository.findAllByCompanyId(companyId, pageable);
                    return users.map(UserMapper::toDTO);
                }
}
