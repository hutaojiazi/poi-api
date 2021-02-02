package com.store.demo.service.encryptor;

import com.store.demo.service.property.EncryptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class UserCredentialEncryptor
{
	private static TextEncryptor encryptor;

	@Autowired
	public void initEnvironment(final EncryptionProperties encryptionProperties)
	{
		initializeEncryptor(encryptionProperties);
	}

	private static void initializeEncryptor(final EncryptionProperties encryptionProperties)
	{
		final String secretKey = encryptionProperties.getCryptoPassword();
		final String salt = encryptionProperties.getCryptoSalt();
		encryptor = Encryptors.text(secretKey, salt);
	}

	public String encrypt(final String password)
	{
		return encryptor.encrypt(password);
	}

	public String decrypt(final String hash)
	{
		return encryptor.decrypt(hash);
	}

}
