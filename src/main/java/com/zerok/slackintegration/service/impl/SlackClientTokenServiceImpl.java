package com.zerok.slackintegration.service.impl;

import com.zerok.slackintegration.entities.SlackClientIntegration;
//import com.zerok.slackintegration.repository.SlackClientTokenRepository;
import com.zerok.slackintegration.service.SlackClientTokenService;
import org.springframework.stereotype.Service;

@Service
public class SlackClientTokenServiceImpl implements SlackClientTokenService {


//    private final SlackClientTokenRepository slackTokenRepository;
//
//    @Autowired
//    public SlackClientTokenServiceImpl(SlackClientTokenRepository slackTokenRepository) {
//        this.slackTokenRepository = slackTokenRepository;
//    }

    public void saveSlackToken(SlackClientIntegration slackToken) {
//        slackTokenRepository.save(slackToken);
    }

    public SlackClientIntegration getSlackTokenByClientId(String clientId) {
//        return slackTokenRepository.findByClientId(clientId);
        return null;
    }

}

