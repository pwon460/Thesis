package Intermediary;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IntermediaryServlet
 */
@WebServlet("/IntermediaryServlet")
public class IntermediaryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static File TDXFile = null;
	private static Calendar c = null;

	public IntermediaryServlet() {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// there's potential that the file hasn't been finished downloaded yet
		TDXFile = (File) getServletContext().getAttribute("mostRecentData");
		c = (Calendar) getServletContext().getAttribute("timeOfRetrieval");
		if (TDXFile == null) {
			// handle
		}

		// TODO fill in the filetype - not sure what type of file we're giving
		// to app yet
		String fileType = "text";
		response.setContentType(fileType);

		// this header probably prompts user whether they wish to dl or not
		// might remove this, not sure unless can test with phone
		response.setHeader("Content-disposition",
				"attachment; filename=" + TDXFile.getName());
		
		// send file to phone
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
