package valkyrie.moon.goo.tax.auth.dto;

import org.springframework.data.annotation.Id;

public class ClientCredentials {

	@Id
	public String id;

	public String applicationId;
	public String refreshToken;

	public ClientCredentials() {
	}

	public ClientCredentials(String id, String applicationId, String refreshToken) {
		this.id = id;
		this.applicationId = applicationId;
		this.refreshToken = refreshToken;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "ClientCredentials{" + "id='" + id + '\'' + ", applicationId='" + applicationId + '\'' + ", refreshToken='" + refreshToken + '\'' +'}';
	}
}
