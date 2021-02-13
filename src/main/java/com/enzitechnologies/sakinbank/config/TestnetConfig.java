package com.enzitechnologies.sakinbank.config;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources(
        {
                @PropertySource("classpath:testnet.properties")
        }
)
public class TestnetConfig {

}
