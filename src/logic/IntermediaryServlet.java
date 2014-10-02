package logic;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Path;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
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
	private static Path TDXData = null;
	private static Calendar c = null;

	public IntermediaryServlet() {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("/test.jsp");
		// there's potential that the file hasn't been finished downloaded yet
		TDXData = (Path) getServletContext().getAttribute("mostRecentData");
		c = (Calendar) getServletContext().getAttribute("timeOfRetrieval");

		// handle with error message
		if (TDXData == null || c == null) {
			response.setHeader("status", "error");
			response.setContentType("text/html");
			
			PrintWriter writer = response.getWriter();
			writer.write("<p>Please wait... </p>");
			
//			String msg = "please check again later for updates";
//			request.setAttribute("status", msg);
		} else {
			// TODO: version check goes here, maybe phone will send version/date
			// send url of file to phone, but for now just displaying it to jsp
			ServletContext ctx = getServletContext();

			URL resURL = ctx.getResource("/Downloads/"
					+ TDXData.getFileName().toString());

			System.out.println(resURL.getPath());
			String path = resURL.getPath();
			String[] array = path.split("^/\\w+", 2);
			path = array[1];

			response.setHeader("status", "ready");
			response.setContentType("text/html");
			
			PrintWriter writer = response.getWriter();
			writer.write("<p>" + path + "</p>");
			
//			request.setAttribute("status", path);
//			request.setAttribute("timestamp",
//					c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));
		}

//		rd.forward(request, response);
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
