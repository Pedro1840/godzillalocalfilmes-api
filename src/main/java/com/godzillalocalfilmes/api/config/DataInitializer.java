package com.godzillalocalfilmes.api.config;

import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.model.Role;
import com.godzillalocalfilmes.api.repository.ClienteRepository;
import com.godzillalocalfilmes.api.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeRolesAndAdmin(RoleRepository roleRepository, ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificar se a role ADMIN existe
            Optional<Role> adminRoleOpt = roleRepository.findByName("ROLE_ADMIN");
            Role adminRole;
            if (adminRoleOpt.isEmpty()) {
                adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            } else {
                adminRole = adminRoleOpt.get();
            }

            // Verificar se a role ADMIN existe
            Optional<Role> userRoleOpt = roleRepository.findByName("ROLE_USER");
            Role userRole;
            if (userRoleOpt.isEmpty()) {
                userRole = new Role();
                userRole.setName("ROLE_USER");
                roleRepository.save(userRole);
            } else {
                userRole = userRoleOpt.get();
            }

            // Verificar se existe um usuário admin
            Optional<Cliente> adminUserOpt = clienteRepository.findByEmail("admin@godzilla.com");
            if (adminUserOpt.isEmpty()) {
                Cliente admin = new Cliente();
                admin.setNome("Admin Godzilla");
                admin.setEmail("admin@godzilla.com");
                admin.setSenha(passwordEncoder.encode("admin123")); // Altere a senha conforme necessário
                admin.setRoles(Set.of(adminRole));
                clienteRepository.save(admin);
                System.out.println("Usuário administrador criado: admin@godzilla.com / admin123");
            }
        };
    }
}
