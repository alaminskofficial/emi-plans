package com.alamin.emi.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang3.StringUtils;

import lombok.SneakyThrows;

public class ModifyResponseBodyWrapper extends HttpServletResponseWrapper {
	/**
	 * Original response object
	 */
	private HttpServletResponse originalResponse;
	/**
	 * Output stream of cache response body (low level stream)
	 */
	private ByteArrayOutputStream baos;
	/**
	 * output response body (high level)
	 */
	private ServletOutputStream out;
	/**
	 * output Character stream of response body
	 */
	private PrintWriter writer;

	@SneakyThrows
	public ModifyResponseBodyWrapper(HttpServletResponse resp) {
		super(resp);
		this.originalResponse = resp;
		this.baos = new ByteArrayOutputStream();
		this.out = new SubServletOutputStream(baos);
		this.writer = new PrintWriter(new OutputStreamWriter(baos));
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return out;
	}

	@Override
	public PrintWriter getWriter() {
		return writer;
	}

	public String getResponseBody(String charset) throws IOException {
		/**
		 * The application layer uses ServletOutputStream or PrintWriter character
		 * stream to output the response The data in these two streams need to be forced
		 * to be flushed to the stream ByteArrayOutputStream, otherwise the response
		 * data cannot be retrieved or the data is incomplete
		 */
		out.flush();
		writer.flush();
		return new String(baos.toByteArray(), StringUtils.isBlank(charset) ? this.getCharacterEncoding() : charset);
	}

	class SubServletOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream baos;

		public SubServletOutputStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}

		@Override
		public void write(int b) {
			baos.write(b);
		}

		@Override
		public void write(byte[] b) {
			baos.write(b, 0, b.length);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {

		}
	}

}