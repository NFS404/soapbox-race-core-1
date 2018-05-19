package com.soapboxrace.core.api;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.soapboxrace.core.bo.ParameterBO;

@Path("/GetFileHashes")
public class GetFileHashes {

	@EJB
	private ParameterBO parameterBO;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getServerInformation() {
		String fileHashesPath = parameterBO.getStrParam("FILE_HASHES_PATH");
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(fileHashesPath));
			return new String(encoded, Charset.defaultCharset());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return "";
	}
}
