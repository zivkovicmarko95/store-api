package com.store.storeproductapi.integrations;

import java.util.Date;
import java.util.Optional;

import com.store.storeproductapi.models.AccountModel;
import com.store.storeproductapi.services.AccountService;
import com.store.storesharedmodule.constants.EventsChannels;
import com.store.storesharedmodule.constants.FlowParams;
import com.store.storesharedmodule.models.UserEntity;
import com.store.storesharedmodule.multiobjectpayload.MultiObjectPayload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.stereotype.Component;

@Component
public class AccountsIntegrationFlows {
    
    private final AccountService accountService;

    @Autowired
    public AccountsIntegrationFlows(AccountService accountService) {
        this.accountService = accountService;
    }

    @Bean
    public IntegrationFlow createAccountFlow() {

        return IntegrationFlows.from(EventsChannels.AUTH_LOGIN_CHANNEL.getEventKey())
                .log(LoggingHandler.Level.INFO, new LiteralExpression("Entered [Create Account Flow]"))
                .handle(UserEntity.class, (event, header) -> {
                    
                    final String username = event.getUsername();
                    final String subjectId = event.getSubjectId();
                    final Date loginDate = event.getLoginDate();

                    final MultiObjectPayload payload = new MultiObjectPayload();
                    payload.addParam(FlowParams.SUBJECT_ID, subjectId);
                    payload.addParam(FlowParams.USERNAME, username);
                    payload.addParam(FlowParams.LOGIN_DATE, loginDate);

                    return payload;
                })
                .handle(MultiObjectPayload.class, (payload, header) -> {

                    final String username = payload.getObjectByKey(FlowParams.USERNAME);
                    final String subjectId = payload.getObjectByKey(FlowParams.SUBJECT_ID);
                    final Date loginDate = payload.getObjectByKey(FlowParams.LOGIN_DATE);

                    final Optional<AccountModel> optionalAccount = accountService.findByUsername(username);

                    if (optionalAccount.isPresent()) {
                        
                        final String id = optionalAccount.get().getId();
                        accountService.updateLoginDate(id, loginDate);
                    } else {
                        accountService.save(subjectId, username);
                    }

                    return payload;
                })
                .log(LoggingHandler.Level.INFO, new LiteralExpression("Executed [Create Account Flow] sucessful ... OK"))
                .get();
    }

}
