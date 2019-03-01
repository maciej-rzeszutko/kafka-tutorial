package github.mackoko.potoc.twitter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import github.mackoko.potoc.twitter.producer.TwitterProducer;

@Configuration
@PropertySource("classpath:twitter_secret.properties")
public class TwitterConfig {

	@Bean
	public Authentication twitterAuthentication(
			@Value("${consumerKey}") String consumerKey,
			@Value("${consumerSecret}") String consumerSecret,
			@Value("${token}") String token,
			@Value("${secret}") String secret
	) {
		return new OAuth1(consumerKey, consumerSecret, token, secret);
	}

	@Bean
	public TwitterProducer twitterProducer(Authentication twitterAuthentication) {
		return new TwitterProducer(twitterAuthentication);
	}
}