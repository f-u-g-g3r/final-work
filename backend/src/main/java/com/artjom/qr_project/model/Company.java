package com.artjom.qr_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies")
public class Company {
	private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id Long id;

	@NotBlank(message = "Name is mandatory")
	private String name;

	@Email
	@NotBlank(message = "Company email is mandatory")
	private String companyEmail;

	@Email
	@NotBlank(message = "Manager email is mandatory")
	private String managerEmail;

	@NotBlank(message = "Registration code is mandatory")
	private String registrationCode;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
	private List<User> users = new ArrayList<>();

	@OneToMany(mappedBy = "initiator")
	private List<Partnership> initiatedPartnerships;

	@OneToMany(mappedBy = "partner")
	private List<Partnership> partnerPartnerships;

	@OneToMany(mappedBy = "initiator")
	private List<Campaign> initiatedCampaigns;

	@NotNull
	private LocalDateTime createdAt;

	private boolean isEnabled = false;

	public Company(String name, String companyEmail, String managerEmail, String registrationCode) {
		this.name = name;
		this.companyEmail = companyEmail;
		this.managerEmail = managerEmail;
		this.registrationCode = registrationCode;
	}


	@Override
	public String toString() {
		return "Company{" +
				"id=" + id +
				", name='" + name + '\'' +
				", companyEmail='" + companyEmail + '\'' +
				", managerEmail='" + managerEmail + '\'' +
				", registrationCode='" + registrationCode + '\'' +
				", users=" + users +
				", createdAt=" + createdAt +
				", isEnabled=" + isEnabled +
				'}';
	}

	public void addUser(User user) {
		users.add(user);
	}

}
