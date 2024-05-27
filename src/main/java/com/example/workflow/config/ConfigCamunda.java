package com.example.workflow.config;

import com.example.workflow.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfigCamunda implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${APPROVER_EMAIL}")
    private String approverEmail;

    private final ProcessEngine processEngine;
    private final IdentityService identityService;

    public ConfigCamunda(ProcessEngine processEngine, IdentityService identityService) {
        this.processEngine = processEngine;
        this.identityService = identityService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        configureGroupsAndUsers();
    }

    public void configureGroupsAndUsers() {
        // Define os detalhes do grupo
        String groupName = "Approvers";
        String groupType = "WORKFLOW";

        // Verifica se o grupo já existe
        Group group = identityService.createGroupQuery().groupId(Constants.GROUP_ID).singleResult();
        if (group == null) {
            // Cria o grupo
            group = identityService.newGroup(Constants.GROUP_ID);
            group.setName(groupName);
            group.setType(groupType);
            identityService.saveGroup(group);
            log.info("Group created: {}", Constants.GROUP_ID);
        } else {
            log.info("Group already exists: {}", Constants.GROUP_ID);
        }

        // Define os detalhes do usuário
        String userId = "johnDoe";
        String firstName = "John";
        String lastName = "Doe";
        String email = approverEmail;
        String password = "password";

        // Verifica se o usuário já existe
        User user = identityService.createUserQuery().userId(userId).singleResult();
        if (user == null) {
            // Cria o usuário
            user = identityService.newUser(userId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(password);
            identityService.saveUser(user);
            log.info("User created: {}", userId);
        } else {
            log.info("User already exists: {}", userId);
        }

        // Adiciona o usuário ao grupo
        if (identityService.createGroupQuery().groupMember(userId).groupId(Constants.GROUP_ID).count() == 0){
            identityService.createMembership(userId, Constants.GROUP_ID);
            log.info("User {} added to group {}", userId, Constants.GROUP_ID);
        } else {
            log.info("User {} is already in group {}", userId, Constants.GROUP_ID);
        }
    }
}