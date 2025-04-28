package com.artjom.qr_project.database;

import com.artjom.qr_project.enums.PartnershipStatus;
import com.artjom.qr_project.enums.Role;
import com.artjom.qr_project.model.Campaign;
import com.artjom.qr_project.model.Company;
import com.artjom.qr_project.model.Partnership;
import com.artjom.qr_project.model.User;
import com.artjom.qr_project.repository.CampaignRepository;
import com.artjom.qr_project.repository.CompanyRepository;
import com.artjom.qr_project.repository.PartnershipRepository;
import com.artjom.qr_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PartnershipRepository partnershipRepository;
    private final CampaignRepository campaignRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    @Value("${app.data.seeding.enabled:false}")
    private boolean isSeedingEnabled;

    @Override
    public void run(String... args) throws Exception {
        if (!isSeedingEnabled) {
            System.out.println("⚡ Data seeding is disabled. Skipping...");
            return;
        }

        System.out.println("Starting data seeding...");

        List<Company> companies = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Company company = new Company();
            company.setName("Company " + i);
            company.setCompanyEmail("company" + i + "@mail.com");
            company.setManagerEmail("manager" + i + "@mail.com");
            company.setRegistrationCode(UUID.randomUUID().toString().substring(0, 8));
            company.setCreatedAt(LocalDateTime.now());
            company.setEnabled(true);

            companies.add(company);
        }
        companyRepository.saveAll(companies);

        List<User> users = new ArrayList<>();
        for (Company company : companies) {
            // COMPANY_OWNER
            User owner = new User();
            owner.setName(company.getName() + " Owner");
            owner.setEmail(company.getManagerEmail());
            owner.setPassword(passwordEncoder.encode("password"));
            owner.setRole(Role.COMPANY_OWNER);
            owner.setCompany(company);
            owner.setUuid(UUID.randomUUID().toString());
            owner.setActive(true);
            users.add(owner);

            // EMPLOYEE
            for (int j = 1; j <= 5; j++) {
                User employee = new User();
                employee.setName(company.getName() + " Employee " + j);
                employee.setEmail(company.getName().toLowerCase().replace(" ", "") + ".employee" + j + "@mail.com");
                employee.setPassword(passwordEncoder.encode("password"));
                employee.setRole(Role.EMPLOYEE);
                employee.setCompany(company);
                employee.setUuid(UUID.randomUUID().toString());
                employee.setActive(true);
                users.add(employee);
            }
        }

        // Admin
        User admin = new User();
        admin.setName("Super Admin");
        admin.setEmail("admin@mail.com");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole(Role.ADMIN);
        admin.setUuid(UUID.randomUUID().toString());
        admin.setActive(true);
        users.add(admin);

        userRepository.saveAll(users);

        // Partnerships
        List<Partnership> partnerships = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Company initiator = companies.get(random.nextInt(companies.size()));
            Company partner = companies.get(random.nextInt(companies.size()));

            if (initiator.equals(partner)) continue;

            Partnership partnership = new Partnership();
            partnership.setInitiator(initiator);
            partnership.setPartner(partner);
            partnership.setCreatedAt(LocalDateTime.now());

            PartnershipStatus[] statuses = PartnershipStatus.values();
            partnership.setStatus(statuses[random.nextInt(statuses.length)]);

            partnerships.add(partnership);
        }
        partnershipRepository.saveAll(partnerships);


        // Campaigns
        List<Campaign> campaigns = new ArrayList<>();
        for (Company company : companies) {
            int campaignCount = random.nextInt(10) + 3;

            for (int i = 0; i < campaignCount; i++) {
                Campaign campaign = new Campaign();
                campaign.setTitle("Promo " + UUID.randomUUID().toString().substring(0, 5));
                campaign.setDescription("Description for " + campaign.getTitle());
                campaign.setDiscountPercentage(random.nextInt(30) + 5);
                campaign.setCreatedAt(LocalDateTime.now());
                campaign.setValidUntil(LocalDateTime.now().plusDays(random.nextInt(60) + 30));
                campaign.setInitiator(company);

                List<Partnership> randomPartnerships = new ArrayList<>();
                int partnershipsToSelect = random.nextInt(3) + 1;

                for (int j = 0; j < partnershipsToSelect; j++) {
                    Partnership randomPartnership = partnerships.get(random.nextInt(partnerships.size()));
                    if (!randomPartnerships.contains(randomPartnership)) {
                        randomPartnerships.add(randomPartnership);
                    }
                }

                campaign.setPartnerships(randomPartnerships);
                campaigns.add(campaign);
            }
        }
        campaignRepository.saveAll(campaigns);


        System.out.println("Data seeding completed successfully!");
    }
}

