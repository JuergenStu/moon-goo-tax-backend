package valkyrie.moon.goo.tax.marketData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import valkyrie.moon.goo.tax.marketData.dtos.UniverseGroups;

public abstract class EsiFetcher {

	protected static final String NAME_URLS = "https://esi.evetech.net/latest/universe/names/?datasource=tranquility"; // requires post

	public UniverseGroups getUniverseGroups(URL url) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		UniverseGroups group = mapper.readValue(in, UniverseGroups.class);
		return group;
	}

	public <T> List<T> fetchNames(String types, URL url, Class<T> refinedMoonOreNameClass) {
		try {
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);

			try (OutputStream os = con.getOutputStream()) {
				byte[] input = types.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
				ObjectMapper mapper = new ObjectMapper();
				List<T> ts = mapper.readValue(br, mapper.getTypeFactory().constructCollectionType(List.class, refinedMoonOreNameClass));
				return ts;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


}
