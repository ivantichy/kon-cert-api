package cz.ivantichy.koncentrator.simple.certgen;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import cz.ivantichy.fileutils.FileWork;
import cz.ivantichy.httpapi.executors.CommandExecutor;
import cz.ivantichy.supersimple.restapi.staticvariables.Static;

public class CreateCa extends CommandExecutor {
	private static final Logger log = LogManager.getLogger(CreateCa.class
			.getName());

	public static synchronized String createCaTapAdvanced(JSONObject json)
			throws Exception {
		log.info("CreateCa started");
		clear();
		String type = json.getString("subvpn_type");
		String name = json.getString("subvpn_name");
		String source = location + slash + Static.GENERATEFOLDER + slash + type;

		String destination = location + slash + Static.INSTANCESFOLDER + slash
				+ type + slash + name;

		json.put("source", source.replaceAll("//", "/"));
		json.put("destination", destination.replaceAll("//", "/"));

		log.info("CreateCa json data parsed");
		FileWork.checkFolder(source, destination);
		FileWork.copyFolder(source, destination);
		log.info("Folders created and copied");

		appendLine("set -e");
		appendLine("cd {destination}");
		appendLine("source ./vars");
		appendLine("export KEY_EXPIRE={ca_valid_days}");
		appendLine("export KEY_COUNTRY=CZ");
		appendLine("export KEY_PROVINCE=CZ");
		appendLine("export KEY_CITY=");
		appendLine("export KEY_ORG={domain}");
		appendLine("export KEY_EMAIL=");
		appendLine("export KEY_CN={subvpn_name}");
		appendLine("export KEY_NAME=");
		appendLine("export KEY_OU=");
		appendLine("export PKCS11_MODULE_PATH=");
		appendLine("export PKCS11_PIN=");
		appendLine("./createca.sh {subvpn_type} {subvpn_name}");
		log.debug("JSON: " + json);
		exec(json);

		log.info("Executed");

		readFileToJSON(json, destination + "/keys/ca.crt", "ca");
		readFileToJSON(json, destination + "/keys/ta.key", "ta");
		readFileToJSON(json, destination + "/keys/dh2048.pem", "dh");
		log.info("Output files read");
		storeJSON(json, destination + slash + name + ".json");
		log.info("JSON stored");
		log.debug("Stored JSON: " + json.toString());

		return json.toString();

	}

	public static synchronized String createCaTunBasic(JSONObject json)
			throws Exception {

		String orgname = json.getString("subvpn_name");
		json.put("subvpn_name", "tun-basic-node-" + json.getInt("node"));

		log.debug("Subvpn_name changed to " + "tun-basic-node-"
				+ json.getInt("node"));
		if (!json.keySet().contains("override")) {
			throw new IOException(
					"Invalid operation. Cannot create CA for tun-basic.");
		}

		createCaTapAdvanced(json);
		json = new JSONObject(json);

		json.put("subvpn_name", orgname);

		return json.toString();

	}

}
