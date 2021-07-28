package com.example.demo.entities;

import javax.persistence.Id;

public class utilisateur {
	
	@Id
	private Long id;
	private String userName;
	private String email;
	private String password;

}
