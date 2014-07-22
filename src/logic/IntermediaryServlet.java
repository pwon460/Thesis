package logic;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
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
		RequestDispatcher rd = request.getRequestDispatcher("/test.jsp");
		// there's potential that the file hasn't been finished downloaded yet
		TDXFile = (File) getServletContext().getAttribute("mostRecentData");
		c = (Calendar) getServletContext().getAttribute("timeOfRetrieval");

		 if (TDXFile == null || c == null) {
			// handle with error message
			String msg = "Server error, please check again later for updates";
			request.setAttribute("status", msg);
		} else {
			// TODO: version check goes here, maybe phone will send version/date
			// send url of file to phone, but for now just displaying it to jsp
//			URL url = new URL(request.getScheme(), request.getServerName(),
//					request.getServerPort(), request.getContextPath());
			request.setAttribute("status", TDXFile.getAbsolutePath());
			request.setAttribute("timestamp",
					c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));
		}

		rd.forward(request, response);

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
