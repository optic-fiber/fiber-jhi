package com.cheroliv.fiber.config

import com.cheroliv.fiber.domain.Authority
import com.cheroliv.fiber.domain.Planning
import com.cheroliv.fiber.domain.User
import com.cheroliv.fiber.repository.AuthorityRepository
import com.cheroliv.fiber.repository.PlanningRepository
import com.cheroliv.fiber.repository.UserRepository
import com.cheroliv.fiber.security.AuthoritiesConstants
import com.cheroliv.fiber.service.UserService
import com.cheroliv.fiber.service.dto.UserDTO
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import javax.transaction.Transactional
import java.time.ZoneId
import java.time.ZonedDateTime

@Slf4j
@CompileStatic
@Transactional
@Component
class DatabaseInitializer {
    final UserService userService
    final AuthorityRepository authorityRepository
    final UserRepository userRepository
    final PlanningRepository planningRepository

    @Autowired
    DatabaseInitializer(UserService userService,
                        AuthorityRepository authorityRepository,
                        UserRepository userRepository,
                        PlanningRepository planningRepository) {
        this.userService = userService
        this.authorityRepository = authorityRepository
        this.userRepository = userRepository
        this.planningRepository = planningRepository
    }

    @PostConstruct
    void InitializeDatabase() {
        createDefaultAuth()
        createDefaultUsers()
    }

    void createDefaultPlanning() {
        planningRepository.save new Planning(
            user: userRepository.findOneByLogin("user")?.get(),
            dateTimeCreation: ZonedDateTime.now(ZoneId.of("Europe/Paris")),
            initialTech: "UU",
            open: Boolean.TRUE,
            firstNameTech: "user",
            lastNameTech: "user")
    }

    void createDefaultAuth() {
        if (!authorityRepository.findById(AuthoritiesConstants.USER).present)
            authorityRepository.save(new Authority(name: AuthoritiesConstants.USER))
        if (!authorityRepository.findById(AuthoritiesConstants.ADMIN).present)
            authorityRepository.save(new Authority(name: AuthoritiesConstants.ADMIN))
        if (!authorityRepository.findById(AuthoritiesConstants.ANONYMOUS).present)
            authorityRepository.save(new Authority(name: AuthoritiesConstants.ANONYMOUS))
    }

    void createDefaultUsers() {
        Optional<User> optionalUser = userService
            .getUserWithAuthoritiesByLogin("user")
        Optional<User> optionalAdminUser = userService
            .getUserWithAuthoritiesByLogin("admin")
        Optional<User> optionalSystemUser =
            userService.getUserWithAuthoritiesByLogin("system")
        Optional<User> optionalAnonymousUser = userService.getUserWithAuthoritiesByLogin("anonymoususer")

        if (!optionalUser.present &&
            !optionalAdminUser.present &&
            !optionalSystemUser.present &&
            !optionalAnonymousUser.present) {
            userService.registerUser(new UserDTO(new User(
                login: "user",
                firstName: "User",
                lastName: "User",
                email: "user@localhost")),
                "user")
            User user = userRepository.findOneByLogin("user")?.get()
            user.activated = true
            user.authorities = [
                authorityRepository.findById(AuthoritiesConstants.USER)?.get()
            ] as Set
            userRepository.save(user)
            createDefaultPlanning()
        }




        if (!optionalAdminUser.present) {
            userService.registerUser(new UserDTO(new User(
                login: "admin",
                firstName: "Administrator",
                lastName: "Administrator",
                email: "admin@localhost")),
                "admin")
            User user = userRepository
                .findOneByLogin("admin")?.get()
            user.activated = true
            user.authorities = [
                authorityRepository
                    .findById(AuthoritiesConstants.USER)?.get(),
                authorityRepository
                    .findById(AuthoritiesConstants.ADMIN)?.get()
            ] as Set
            userRepository.save(user)
        }



        if (!optionalSystemUser.present) {
            userService.registerUser(new UserDTO(new User(
                login: "system",
                firstName: "System",
                lastName: "System",
                email: "system@localhost")),
                "system")
            User user = userRepository.findOneByLogin("system")?.get()
            user.activated = true
            user.authorities = [authorityRepository
                                    .findById(AuthoritiesConstants
                                        .ADMIN)?.get(),
                                authorityRepository
                                    .findById(AuthoritiesConstants
                                        .USER)?.get()] as Set
            userRepository.save(user)
        }

        if (!optionalAnonymousUser.present) {
            userService.registerUser(new UserDTO(new User(
                login: "anonymoususer",
                firstName: "Anonymous",
                lastName: "User",
                email: "anonymous@localhost")),
                "anonymoususer")
            User user = userRepository.findOneByLogin("anonymoususer")?.get()
            user.activated = true
            user.authorities = [] as Set
            userRepository.save(user)
        }


    }
}