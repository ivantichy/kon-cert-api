package cz.ivantichy.supersimple.restapi.server;


public class Response {
	
	String text;
	boolean ok;
	public Response(String text, boolean ok) {
		super();
		this.text = text;
		this.ok = ok;
	}
	
	public String toString(){
		
		return text;
	}

}
