package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.security.GithubAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
public class GithubResource {
	
	@Value("${client_id}")
	private String clientId;
	
	@Value("${github.authorize.url}")
	private String githubAuthorizeUrl;
	
	@Value("${github.token.url}")
	private String githubTokenUrl;

	@RequestMapping(value = "/github/authorize", method = RequestMethod.GET)
	public String redirectGithubLogin() {
		return "redirect:" + githubAuthorizeUrl;
	}
	
	@RequestMapping(value = "/github/token", method = RequestMethod.GET)
	public String loginWithToken(String code) {
		if (code == null){
			return "redirect:/login";
		}
		RestTemplate restTemplate = new RestTemplate();
		String url = githubTokenUrl + "&code="+code;
		ResponseEntity<String> postForEntity = restTemplate.postForEntity(url, null, String.class);
		String responseText = postForEntity.getBody();
		
		if (responseText.matches("access_token=.+")){
			SecurityContextHolder.getContext().setAuthentication(new GithubAuthentication());
		}
		
		return "redirect:/";
	}

	
}
