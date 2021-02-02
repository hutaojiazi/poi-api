package com.store.demo.service.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

/**
 * Encapsulates configuration properties required for generating user credential hash.
 */
@Component
@ConfigurationProperties(prefix = "encryption")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncryptionProperties
{
	@NotBlank
	private String cryptoPassword;

	@NotBlank
	private String cryptoSalt;
}
