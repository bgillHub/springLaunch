package com.springStarter.security;

import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication().withUser("in28Minutes").password("dummy")
				.roles("USER", "ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/login").permitAll()
				.antMatchers("/", "/*todo*/**").access("hasRole('USER')").and()
				.formLogin();
		FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
		    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		    .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
		    .build();

		FirebaseApp.initializeApp(options);
	}
	
	//Our Javascript Code
//	  const firebase = require('firebase');
//	  return (dispatch) => {
//	    dispatch({ type: USER_LOGIN});
//	  firebase.auth().signInWithEmailAndPassword(email, password)
//	  .then(user => loginSuccess(dispatch, user))
//	  .catch((err) => {
//	    console.log("ERR: ", err)
//	    firebase.auth().createUserWithEmailAndPassword(email, password)
//	    .then(user => loginSuccess(dispatch, user))
//	    .catch((err) =>{
//	      console.log("ERR: ", err);
//	      loginFail(dispatch);
//	    });
//	  });
//	};
	
}