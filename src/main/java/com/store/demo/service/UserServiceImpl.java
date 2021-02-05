package com.store.demo.service;

import com.store.demo.dto.UserDto;
import com.store.demo.model.User;
import com.store.demo.repository.UserRepository;
import com.store.demo.service.encryptor.UserCredentialEncryptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService
{
	private final UserRepository userRepository;
	private final UserCredentialEncryptor credentialEncryptor;

	public UserServiceImpl(final UserRepository userRepository, final UserCredentialEncryptor credentialEncryptor)
	{
		this.userRepository = userRepository;
		this.credentialEncryptor = credentialEncryptor;
	}

	@Override
	@Transactional(readOnly = true)
	public String validate(final String email, final String password)
	{
		final User user = userRepository.findByEmail(email);
		if (Objects.isNull(user))
		{
			return null;
		}
		final String dbPassword = credentialEncryptor.decrypt(user.getHash());
		return StringUtils.equals(password, dbPassword) ? user.getId() : null;
	}

	@Override
	@Transactional
	public String create(UserDto dto)
	{
		final User entity = User.builder()
				.name(dto.getName())
				.email(dto.getEmail())
				.hash(credentialEncryptor.encrypt(dto.getPassword()))
				.build();

		return userRepository.save(entity).getId();
	}
}