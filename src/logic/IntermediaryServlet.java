package logic;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.servlet.ServletContext;
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
	private static ArrayList<Path> TDXData = null;

	public IntermediaryServlet() {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		RequestDispatcher rd = request.getRequestDispatcher("/test.jsp");
		// there's potential that the file hasn't been finished downloaded yet
		TDXData = (ArrayList<Path>) getServletContext().getAttribute(
				"mostRecentData");
		
		// handle with error message
		if (TDXData == null) {
			response.setHeader("status", "error");
			response.setContentType("text/html");

			PrintWriter writer = response.getWriter();
			writer.write("<p>Please wait... </p>");

			// String msg = "please check again later for updates";
			// request.setAttribute("status", msg);
		} else {
			// send url of file to phone
			ServletContext ctx = getServletContext();

			Path weeklyDataPath = TDXData.get(0);
			Path initDBPath = TDXData.get(1);
			Path patchPath = TDXData.get(2);

			response.setHeader("status", "ready");
			response.setContentType("text/html");
			
			String path;
			PrintWriter writer = response.getWriter();
			
			path = getPath(ctx, weeklyDataPath);
			writer.write("<p>" + path + "</p>\n");

			path = getPath(ctx, initDBPath);
			// writer.write("<p id=1>/Thesis/Downloads/simo.init.zip</p>\n");
			writer.write("<p id=1>" + path + "</p>\n");

			path = getPath(ctx, patchPath);
			// writer.write("<p id=2>/Thesis/Downloads/simo.patch.20140912.zip</p>\n");
			writer.write("<p id=2>" + path + "</p>\n");

			// request.setAttribute("status", path);
		}

		// rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private String getPath(ServletContext ctx, Path p)
			throws MalformedURLException {
		URL resURL = ctx
				.getResource("/Downloads/" + p.getFileName().toString());

		String path = resURL.getPath();
		System.out.println(path); // path will be in the form
									// /<host>/path/to/file
		String[] array = path.split("^/\\w+", 2);
		// array[0] is the host eg. localhost, ip, etc
		path = array[1];

		return path;
	}

}
