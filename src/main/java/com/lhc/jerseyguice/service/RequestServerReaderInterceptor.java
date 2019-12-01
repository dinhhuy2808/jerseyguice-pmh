package com.lhc.jerseyguice.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
@Provider
public class RequestServerReaderInterceptor implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		// TODO Auto-generated method stub
		if (ctx.getLanguage() != null && "EN".equals(ctx.getLanguage()
		          .getLanguage())) {
		  
		            ctx.abortWith(Response.status(Response.Status.FORBIDDEN)
		              .entity("Cannot access")
		              .build());
		        }
	}
	
	
}