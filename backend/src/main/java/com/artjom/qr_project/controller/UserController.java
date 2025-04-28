package com.artjom.qr_project.controller;


import com.artjom.qr_project.DTO.FilterRequest;
import com.artjom.qr_project.DTO.UserDTO;
import com.artjom.qr_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/User")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
	private final UserService service;

	@GetMapping
	public CollectionModel<EntityModel<UserDTO>> all() {
		return service.all();
	}

	@PostMapping("/getUsers")
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<UserDTO> getUsers(@RequestBody FilterRequest filterRequest) {
		return service.findWithFilters(filterRequest);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER', 'MANAGER')")
	public EntityModel<UserDTO> one(@PathVariable Long id) {
		return service.one(id);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		service.deleteUser(id);
	}

	@PatchMapping("/activate/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER', 'MANAGER')")
	public ResponseEntity<?> activateUser(@PathVariable Long id) {
		return service.activateUser(id);
	}

	@PatchMapping("/deactivate/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER', 'MANAGER')")
	public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
		return service.deactivateUser(id);
	}

	@PatchMapping("/addUserToCompany")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER', 'MANAGER')")
	public ResponseEntity<?> addUserToCompany(@RequestParam Long userId, @RequestParam Long companyId) {
		return service.addUserToCompany(userId, companyId);
	}

	@GetMapping("/getUserDataMin/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER', 'MANAGER') or #id == authentication.principal.id")
	public ResponseEntity<UserDTO> getUserDataMin(@PathVariable("id") Long id) {
		return service.getUserDataMin(id);
	}

	@PostMapping("/getCompanyUsers/{companyId}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY_OWNER')")
	public Page<UserDTO> getCompanyUsers(@PathVariable Long companyId, @RequestBody FilterRequest filterRequest) {
		return service.getCompanyUsers(companyId, filterRequest);
	}
}
